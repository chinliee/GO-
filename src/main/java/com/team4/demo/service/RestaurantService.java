package com.team4.demo.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.team4.demo.model.entity.RestaurantPhoto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.team4.demo.model.dto.menu.MenuCreateDto;
import com.team4.demo.model.dto.menu.MenuDeleteDto;
import com.team4.demo.model.dto.menu.MenuSearchRespDto;
import com.team4.demo.model.dto.menu.MenuUpdateDto;
import com.team4.demo.model.dto.restaurant.MenuDataDto;
import com.team4.demo.model.dto.restaurant.RestaurantAdmin;
import com.team4.demo.model.dto.restaurant.RestaurantContactInfo;
import com.team4.demo.model.dto.restaurant.RestaurantDataRespDto;
import com.team4.demo.model.dto.restaurant.RestaurantFindAllDto;
import com.team4.demo.model.dto.restaurant.RestaurantIntroduce;
import com.team4.demo.model.dto.restaurant.RestaurantRegisterDto;
import com.team4.demo.model.dto.restaurant.RestaurantUpdateInfoDto;
import com.team4.demo.model.dto.restaurant.Restaurantlogin;
import com.team4.demo.model.entity.Menu;
import com.team4.demo.model.entity.MenuCart;
import com.team4.demo.model.entity.Restaurant;
import com.team4.demo.model.repository.MenuRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import com.team4.demo.util.SaltGenerator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    public RestaurantDataRespDto getRestaurantData(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 restaurantId => " + restaurantId + " 不存在"));

        RestaurantDataRespDto respDto = RestaurantDataRespDto.builder()
                .name(restaurant.getName())
                .address(restaurant.getCounty() + restaurant.getDistrict() + restaurant.getAddress())
                .introduce(restaurant.getIntroduce())
                .build();

		// 取得餐廳營業時間
        if (restaurant.getOpenTime() != null) {
            respDto.setOpenTime(restaurant.getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        if (restaurant.getCloseTime() != null) {
            respDto.setCloseTime(restaurant.getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }

		// 取得餐廳圖片
		byte[] photo = restaurant.getRestaurantPhotosList()
				.stream()
				.filter(p -> p.getPhotoMain() == 1)
				.findFirst()
				.map(RestaurantPhoto::getPhotoFile)
				.orElse(null);
		respDto.setPhoto(photo);

		// 取得餐點資料
        List<Menu> menuList = restaurant.getMenuList();
        List<MenuDataDto> menuDtoList = new ArrayList<>();
        // 使用 Spring BeanUtils.copyProperties()，把 Menu 物件的屬性值，複製給 MenuSearchRespDto 物件
        menuList.forEach(menu -> {
            MenuDataDto menuDto = new MenuDataDto();
            BeanUtils.copyProperties(menu, menuDto);
            menuDtoList.add(menuDto);
        });

        respDto.setMenuList(menuDtoList);
        return respDto;
    }

    public List<MenuSearchRespDto> getRestaurantMenu(Integer restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 restaurantId => " + restaurantId + " 不存在"));

        List<Menu> menuList = restaurant.getMenuList();

        // 使用 Spring BeanUtils.copyProperties()，把 Menu 物件的屬性值，複製給 MenuSearchRespDto 物件
        List<MenuSearchRespDto> menuDtoList = new ArrayList<>();
        menuList.forEach(menu -> {
            MenuSearchRespDto menuDto = new MenuSearchRespDto();
            BeanUtils.copyProperties(menu, menuDto);
            menuDtoList.add(menuDto);
        });

        return menuDtoList;
    }

    public MenuSearchRespDto createMenu(MenuCreateDto createDto) {
        Integer restaurantId = createDto.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 restaurantId => " + restaurantId + " 不存在"));

        Menu menu = Menu.builder()
                .name(createDto.getName())
                .price(createDto.getPrice())
                .description(StringUtils.isBlank(createDto.getDescription()) ? null : createDto.getDescription())
                .restaurant(restaurant)
                .build();

        // readImageFile()，自己寫的方法
        // 接收上傳檔案的 MultipartFile 物件，轉成圖片的 byte[] 資料，存進資料庫
        menu.setPhoto(readImageFile(createDto.getPhoto()));

        // 當執行 save 方法之後，menu 物件在 JPA　的生命週期，會變成 managed 狀態，此時的 menu 物件，會和資料庫的該筆資料同步
        // 所以當新增成功後，和資料庫新增的資料同步，menu 物件就有了自增主鍵 menuId，可以直接使用 menu 進行後續操作，就不需要再用變數 Menu createdMenu = menuRepository.save(menu) 來儲存新增成功的資料
        menuRepository.save(menu);

        MenuSearchRespDto respDto = new MenuSearchRespDto();
        BeanUtils.copyProperties(menu, respDto);
        return respDto;
    }

    public MenuSearchRespDto updateMenu(MenuUpdateDto updateDto) {
        Integer menuId = updateDto.getMenuId();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 menuId => " + menuId + " 不存在"));

        menu.setName(updateDto.getName());
        menu.setPrice(updateDto.getPrice());
        menu.setDescription(StringUtils.isBlank(updateDto.getDescription()) ? null : updateDto.getDescription());
        menu.setPhoto(readImageFile(updateDto.getPhoto()));
        menuRepository.save(menu);

        MenuSearchRespDto respDto = new MenuSearchRespDto();
        BeanUtils.copyProperties(menu, respDto);
        return respDto;
    }

    public void deleteMenu(MenuDeleteDto deleteDto) {
        Restaurant restaurant = restaurantRepository.findById(deleteDto.getRestaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 restaurantId => " + deleteDto.getRestaurantId() + " 不存在"));

        // 先刪除菜單
        menuRepository.deleteAllById(deleteDto.getMenuIds());

        // 刪除菜單 Menu，會同步刪除餐點購物車的餐點明細 MenuCartItem，因為有設定級聯 cascade
        // 這時候要檢查有沒有餐點購物車 MenuCart 為空，如果 MenuCart 的屬性 itemList 為空，就要把 MenuCart 也刪除
        // 從餐廳 Restaurant 找到餐點購物車 MenuCart
        // 並移除餐點購物車中，餐點明細 MenuCartItem 為空的購物車
        List<MenuCart> menuCartList = restaurant.getMenuCartList();

        // removeIf()，移除集合中的元素，移除空的餐點購物車，會直接修改原本的集合
        menuCartList.removeIf(menuCart -> menuCart.getItemList().isEmpty());

        // 藉由更新餐廳，來刪除空的餐點購物車
        restaurantRepository.save(restaurant);
    }

    public byte[] readImageFile(MultipartFile file) {
        byte[] photo = null;
        if (file.getSize() > 0) {
            try {
                photo = file.getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return photo;
    }
//    登入
	public String findbyuniformnumbers(Restaurantlogin dto) {
	    String uniformNumbers = dto.getUniformNumbers();
	    Optional<Restaurant> restaurantOptional = restaurantRepository.findByuniformNumbers(uniformNumbers);
//	   	確認有此人後，進行密碼驗證
	    if (restaurantOptional.isPresent()) {
	         Restaurant restaurant = restaurantOptional.get();
	         String password = dto.getPassword(); 
	         if (restaurant.getPassword().equals(password)) { 
//	        	 返回餐廳ID存在前端
	             return Integer.toString(restaurant.getRestaurantId()); 
	         } else {
	             return "密碼不正確"; 
	         }
	    } else {
	         return "查無此統編"; 
	    }               
	}
	
	public List<RestaurantAdmin> AdminFindAll(){
	 
	 List<Restaurant> restaurantsList = restaurantRepository.findAll();
	
	 List<RestaurantAdmin> restaurantAdminList = new ArrayList<>();
	
	for(Restaurant restaurant: restaurantsList) {
		RestaurantAdmin restaurantAdmin = new RestaurantAdmin();
		
		restaurantAdmin.setRestaurantId(restaurant.getRestaurantId());
		restaurantAdmin.setName(restaurant.getName());
		restaurantAdmin.setUniformNumbers(restaurant.getUniformNumbers());
		restaurantAdmin.setPassword(restaurant.getPassword());
		restaurantAdmin.setCounty(restaurant.getCounty());
		restaurantAdmin.setDistrict(restaurant.getDistrict());
		
		restaurantAdminList.add(restaurantAdmin);
	}
	 
	
	return restaurantAdminList;
	}
//	單筆查詢(餐廳修改用)
	@Transactional
	public RestaurantFindAllDto findById(Integer RestaurantId) {
		Restaurant restaurant = restaurantRepository.findById(RestaurantId)
				.orElse(null);
	if (restaurant != null) {
    RestaurantFindAllDto dto = new RestaurantFindAllDto();
		    dto.setRestaurantId(restaurant.getRestaurantId());
		    dto.setName(restaurant.getName());
		    dto.setUniformNumbers(restaurant.getUniformNumbers());
		    dto.setCounty(restaurant.getCounty());
		    dto.setDistrict(restaurant.getDistrict());
		    dto.setAddress(restaurant.getAddress());
		    dto.setPhone(restaurant.getPhone());
		    dto.setOpenTime(restaurant.getOpenTime());
		    dto.setCloseTime(restaurant.getCloseTime());
		    dto.setStyle(restaurant.getStyle());
		    dto.setIntroduce(restaurant.getIntroduce());	       	        
		        return dto;
		    } else {
		        return null; 
		    }
		
	
	}
//	修改地址、電話
	 public Restaurant updateRestaurantContactInfo(Integer restaurantId, RestaurantContactInfo dto) {
	        
		 Restaurant restaurant = restaurantRepository.findById(restaurantId)
	                                                    .orElse(null);
	        restaurant.setCounty(dto.getCounty());
	        restaurant.setDistrict(dto.getDistrict());
	        restaurant.setAddress(dto.getAddress());
	        restaurant.setPhone(dto.getPhone());
	        return restaurantRepository.save(restaurant); 
	    }
//	 修改時間 風格
	 public Restaurant updateRestaurantInfo(Integer restaurantId, RestaurantUpdateInfoDto dto) {
	        
		 Restaurant restaurant = restaurantRepository.findById(restaurantId)
	                                                    .orElse(null);
	        restaurant.setOpenTime(dto.getOpenTime());
	        restaurant.setCloseTime(dto.getCloseTime());
	        restaurant.setStyle(dto.getStyle());
	        return restaurantRepository.save(restaurant); 
	    }
//	修改介紹
	public Restaurant updateRestaurantIntroduce(Integer restaurantId, RestaurantIntroduce dto) {
		
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
															.orElse(null);
		
		restaurant.setIntroduce(dto.getIntroduce());
		
		return restaurantRepository.save(restaurant);
		
	}
	 	 
	
//	更改餐廳名
	 public Restaurant updateRestaurantName(Integer restaurantId, String name) {
	        Restaurant restaurant = restaurantRepository.findById(restaurantId)
	                                                    .orElse(null);
	        restaurant.setName(name); 
	        return restaurantRepository.save(restaurant); 
	    }
//   餐廳加鹽
	 public Restaurant insertRestaurant(RestaurantRegisterDto dto) {
	        if (restaurantRepository.existsByUniformNumbers(dto.getUniformNumbers())) {
	            throw new RuntimeException("UniformNumbers 已被使用");
	        }
	       
	        String salt = SaltGenerator.generateSalt();
	        Restaurant restaurant = new Restaurant();
	        restaurant.setName(dto.getName());
	        restaurant.setUniformNumbers(dto.getUniformNumbers());
	        restaurant.setCounty(dto.getCounty());
	        restaurant.setDistrict(dto.getDistrict());
	        restaurant.setAddress(dto.getAddress());
	        restaurant.setPhone(dto.getPhone());

	        restaurant.setSalt(salt);
	        String hashedPassword = SaltGenerator.hashPassword(dto.getPassword(), salt);
	        restaurant.setPassword(hashedPassword);

	        return restaurantRepository.save(restaurant);
	    }
//	 認證
	 public List<String> authenticateAndGetRestaurantId(String username, String password) {
		    // 根據用戶名從資料庫中檢索使用者資訊
		    Restaurant restaurant = restaurantRepository.findByUniformNumbers(username);
		    
		    // 如果找不到使用者，驗證失敗
		    if (restaurant == null) {
		        return null; // 或者你可以拋出一個異常或者返回一個錯誤消息
		    }
		    
		    // 取得資料庫中儲存的密碼和鹽值
		    String storedPassword = restaurant.getPassword();
		    String storedSalt = restaurant.getSalt();
		    
		    // 使用相同的鹽值對輸入的密碼進行加密
		    String hashedPassword = SaltGenerator.hashPassword(password, storedSalt);
		    
		    System.out.println(hashedPassword);
		    System.out.println(storedPassword);
		    // 比較資料庫中儲存的密碼和輸入密碼的雜湊值
		    if (hashedPassword.equals(storedPassword)) {
		    	ArrayList<String> restaurantBack = new ArrayList<>();
		        // 如果驗證成功，返回餐廳 ID
		       String restaurantId = Integer.toString(restaurant.getRestaurantId());
		       String restaurantName = restaurant.getName();
		       restaurantBack.add(restaurantId);
		       restaurantBack.add(restaurantName);
		    	return restaurantBack;
		    } else {
		        return null; // 或者你可以拋出一個異常或者返回一個錯誤消息
		    }
		}
	
	 
	
//	刪除
	public void deleteById(Integer Restaurantid) {
		restaurantRepository.deleteById(Restaurantid);
	}
	

}
