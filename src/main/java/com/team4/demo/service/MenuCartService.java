package com.team4.demo.service;

import com.team4.demo.model.dto.menuCart.*;
import com.team4.demo.model.entity.*;
import com.team4.demo.model.repository.MemberRepository;
import com.team4.demo.model.repository.MenuCartItemRepository;
import com.team4.demo.model.repository.MenuCartRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuCartService {

    @Autowired
    private MenuCartRepository menuCartRepository;

    @Autowired
    private MenuCartItemRepository menuCartItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    /**
     * 查詢會員於此餐廳的餐點購物車資料，包含訂單類型外送或外帶自取、外送費用、平台費用，以及購物車的餐點明細資料(餐點名稱、價錢、圖片)
     * @param memberId     會員 ID
     * @param restaurantId 餐廳 ID
     * @return {@link MenuCartSearchRespDto} 餐點購物車資料封裝物件
     */
    public MenuCartSearchRespDto getCartByMemberAndRestaurant(Integer memberId, Integer restaurantId) {
        MenuCartSearchRespDto cartDto = new MenuCartSearchRespDto();

        // 先查詢會員在該餐廳，有沒有餐點購物車
        MenuCart menuCart = menuCartRepository
                .findByMember_MemberIdAndRestaurant_RestaurantId(memberId, restaurantId)
                .orElse(null);

        // 會員於該餐廳，沒有購物車(也就是沒有將任何餐點加入購物車)
        if (menuCart == null) {
            return null;
        }

        // 會員於該餐廳，有購物車，再查詢購物車中的餐點明細 List<MenuCartItem>
        // 並結合 Menu 資料表，組合出前端需要的資料屬性
        Integer cartId = menuCart.getCartId();
        List<MenuCartItem> itemList = menuCart.getItemList();

        List<MenuCartItemSearchRespDto> itemDtoList = itemList.stream()
                // 依照加入購物車時間，降冪排序
                .sorted(Comparator.comparing(MenuCartItem::getCreatedTime, Comparator.reverseOrder()))
                .map(item -> {
                    Menu menu = item.getMenu();
                    MenuCartItemSearchRespDto itemDto = new MenuCartItemSearchRespDto();
                    itemDto.setItemId(item.getItemId());
                    itemDto.setCartId(cartId);
                    itemDto.setMenuId(menu.getMenuId());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setName(menu.getName());
                    itemDto.setPrice(menu.getPrice());
                    itemDto.setPhoto(menu.getPhoto());
                    return itemDto;
                })
                .collect(Collectors.toList());

        // 將購物車資料(如外送或外帶自取、外送費用、平台費用)，以及購物車的餐點明細資料(餐點名稱、價錢、圖片)
        // 封裝成 MenuCartSearchRespDto，回傳給前端，作為載入購物車資料使用
        // 使用 BeanUtils.copyProperties()，把 menuCart 物件的屬性值，複製給 MenuCartSearchRespDto，回傳前端
        BeanUtils.copyProperties(menuCart, cartDto);
        cartDto.setItemList(itemDtoList);

        return cartDto;
    }

    /**
     * 新增購物車餐點 MenuCartItem，有兩種情境
     * 情境1：新增購物車餐點，會員沒有該餐廳的購物車，先新增購物車，再新增餐點
     * 情境2：新增購物車餐點，會員已經有該餐廳的購物車，直接新增一筆餐點
     *
     * @param createDto
     * @return {@link MenuCartItemSearchRespDto} 新增成功的購物車餐點資料
     */
    public MenuCartItemSearchRespDto createItem(MenuCartItemCreateDto createDto) {
        MenuCartItemSearchRespDto respDto = new MenuCartItemSearchRespDto();
        Integer memberId = createDto.getMemberId();
        Integer restaurantId = createDto.getRestaurantId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 memberId => " + memberId + " 不存在"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 restaurantId => " + restaurantId + " 不存在"));

        Menu menu = restaurant.getMenuList().stream()
                .filter(m -> m.getMenuId().equals(createDto.getMenuId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 menuId => " + createDto.getMenuId() + " 不存在"));

        // 先查詢會員在該餐廳，有沒有餐點購物車
        MenuCart menuCart = menuCartRepository
                .findByMember_MemberIdAndRestaurant_RestaurantId(memberId, restaurantId)
                .orElse(null);

        // 會員於該餐廳，沒有購物車(也就是沒有將任何餐點加入購物車)，新增購物車，再新增餐點
        if (menuCart == null) {
            // 建立購物車
            MenuCart newCart = MenuCart.builder()
                    .member(member)
                    .restaurant(restaurant)
                    .orderType(createDto.getOrderType())
                    .deliveryFee(createDto.getDeliveryFee())
                    .platformFee(createDto.getPlatformFee())
                    .build();

            // 建立餐點
            MenuCartItem item = MenuCartItem.builder()
                    .quantity(createDto.getQuantity())
                    .createdTime(new Date())
                    .menuCart(newCart)
                    .menu(menu)
                    .build();

            // 把餐點加入購物車
            List<MenuCartItem> itemList = new ArrayList<>();
            itemList.add(item);
            newCart.setItemList(itemList);

            // 新增購物車 MenuCart，同時新增餐點 MenuCartItem
            menuCartRepository.save(newCart);

            // 新增成功的菜單資料，組合需要的屬性，封裝成 MenuCartItemSearchRespDto 回傳前端
            respDto.setCartId(newCart.getCartId());
            respDto.setItemId(item.getItemId());
            respDto.setQuantity(item.getQuantity());
        }
        // 會員於該餐廳，已經有購物車了，直接新增一筆餐點
        else {
            // 建立餐點
            MenuCartItem item = MenuCartItem.builder()
                    .quantity(createDto.getQuantity())
                    .createdTime(new Date())
                    .menuCart(menuCart)
                    .menu(menu)
                    .build();

            // 新增餐點
            menuCartItemRepository.save(item);

            // 新增成功的菜單資料，組合需要的屬性，封裝成 MenuCartItemSearchRespDto 回傳前端
            respDto.setCartId(menuCart.getCartId());
            respDto.setItemId(item.getItemId());
            respDto.setQuantity(item.getQuantity());
        }

        respDto.setMenuId(menu.getMenuId());
        respDto.setName(menu.getName());
        respDto.setPrice(menu.getPrice());
        respDto.setPhoto(menu.getPhoto());
        return respDto;
    }

    public MenuCartItemUpdateQtyRespDto updateItemQuantity(MenuCartItemUpdateQtyDto updateDto) {
        MenuCartItem item = menuCartItemRepository.findById(updateDto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 itemId => " + updateDto.getItemId() + " 不存在"));

        item.setQuantity(updateDto.getQuantity());
        menuCartItemRepository.save(item);

        MenuCartItemUpdateQtyRespDto respDto = new MenuCartItemUpdateQtyRespDto();
        // BeanUtils.copyProperties()，把 MenuCartItem 物件的屬性值，複製給 MenuCartItemUpdateQtyRespDto
        BeanUtils.copyProperties(item, respDto);

        return respDto;
    }

    public void deleteItem(MenuCartItemDeleteDto deleteDto) {
        Integer itemId = deleteDto.getItemId();
        MenuCartItem item = menuCartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 itemId => " + itemId + " 不存在"));

        // 取得餐點購物車
        MenuCart menuCart = item.getMenuCart();
        menuCart.getItemList().remove(item);

        // 檢查購物車如果移除餐點之後，購物車是否為空，空代表沒有任何餐點，就要同時刪除購物車 MenuCart、購物車餐點 MenuCartItem
        // 購物車為空，刪除購物車 MenuCart，因為聯集刪除的關係 cascade = {CascadeType.ALL}，會同時刪除購物車的餐點 MenuCartItem
        if (menuCart.getItemList().isEmpty()) {
            menuCartRepository.delete(menuCart);
        }
        else {
            menuCartRepository.save(menuCart);
        }
    }

    public MenuCartUpdateRespDto updateCart(MenuCartUpdateDto updateDto) {
        Integer cartId = updateDto.getCartId();
        MenuCart menuCart = menuCartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 cartId => " + cartId + " 不存在"));

        menuCart.setOrderType(updateDto.getOrderType());
        menuCart.setDeliveryFee(updateDto.getDeliveryFee());
        menuCart.setPlatformFee(updateDto.getPlatformFee());
        menuCartRepository.save(menuCart);

        MenuCartUpdateRespDto respDto = new MenuCartUpdateRespDto();
        BeanUtils.copyProperties(menuCart, respDto);
        return respDto;
    }

    public MenuCartCheckoutRespDto getCheckoutInfo(Integer memberId, Integer restaurantId) {
        MenuCartCheckoutRespDto respDto = new MenuCartCheckoutRespDto();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 memberId => " + memberId + " 不存在"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 restaurantId => " + restaurantId + " 不存在"));

        // 組合需要的會員資料，回傳給前端
        MemberCheckoutDto memberDto = MemberCheckoutDto.builder()
                .memberId(member.getMemberId())
                .address(member.getAddress())
                // TODO: 會員信用卡資料
//                .creditCardInfo(member.getCreditCardInfo())
                .build();

        respDto.setMember(memberDto);

        // 組合需要的餐廳資料，回傳給前端
        RestaurantCheckoutDto restaurantDto = RestaurantCheckoutDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .address(restaurant.getCounty() + restaurant.getDistrict() + restaurant.getAddress())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
                .openTime(restaurant.getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .closeTime(restaurant.getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .build();

        // 取得餐廳圖片
        byte[] photo = restaurant.getRestaurantPhotosList()
                .stream()
                .filter(p -> p.getPhotoMain() == 1)
                .findFirst()
                .map(RestaurantPhoto::getPhotoFile)
                .orElse(null);
        restaurantDto.setPhoto(photo);
        respDto.setRestaurant(restaurantDto);

        // 查詢、組合餐點購物車 MenuCart、餐點 MenuCartItem 資料
        // 先查詢會員在該餐廳，有沒有餐點購物車
        MenuCart menuCart = menuCartRepository
                .findByMember_MemberIdAndRestaurant_RestaurantId(memberId, restaurantId)
                .orElse(null);

        // 會員於該餐廳，沒有購物車(也就是沒有將任何餐點加入購物車)
        if (menuCart == null) {
            return respDto;
        }

        // 會員於該餐廳，有餐點購物車，取得購物車中的餐點明細 List<MenuCartItem>
        // 並結合 Menu 資料表，組合出前端需要的資料屬性
        List<MenuCartItem> itemList = menuCart.getItemList();
        List<MenuCartItemCheckoutRespDto> itemDtoList = itemList.stream()
                // 依照加入購物車時間，降冪排序
                .sorted(Comparator.comparing(MenuCartItem::getCreatedTime, Comparator.reverseOrder()))
                .map(item -> {
                    Menu menu = item.getMenu();
                    MenuCartItemCheckoutRespDto itemDto = new MenuCartItemCheckoutRespDto();
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setName(menu.getName());
                    itemDto.setPrice(menu.getPrice());
                    return itemDto;
                })
                .collect(Collectors.toList());

        // 使用 BeanUtils.copyProperties()，把 MenuCart 物件的屬性值，複製給 CartCheckoutDto
        CartCheckoutDto cartDto = new CartCheckoutDto();
        BeanUtils.copyProperties(menuCart, cartDto);
        cartDto.setItemList(itemDtoList);
        respDto.setCart(cartDto);

        return respDto;
    }

}
