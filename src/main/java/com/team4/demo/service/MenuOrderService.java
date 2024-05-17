package com.team4.demo.service;

import com.team4.demo.model.dto.menuOrder.*;
import com.team4.demo.model.dto.websocket.WebSocketMessage;
import com.team4.demo.model.entity.*;
import com.team4.demo.model.repository.*;
import com.team4.demo.model.specification.MenuOrderSpecification;
import com.team4.demo.util.PositionHelper;
import com.team4.demo.util.QRCodeUtil;
import com.team4.demo.util.linepay.LinePayService;
import jakarta.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MenuOrderService {

    @Autowired
    private MenuOrderRepository menuOrderRepository;

    @Autowired
    private MenuCartRepository menuCartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private LinePayService linePayService;

    @Autowired
    private HttpServletRequest request;

    public List<MenuOrderForMemberDto> getOrderByMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 memberId => " + memberId + " 不存在"));

        List<MenuOrder> menuOrerList = member.getMenuOrderList();

        // 把訂單依照建立時間，由新到舊排序，並將需要的資料封裝成 MenuOrderForMemberDto 回傳前端
        List<MenuOrderForMemberDto> dtoList = menuOrerList.stream()
                .sorted(Comparator.comparing(MenuOrder::getCreatedTime, Comparator.reverseOrder()))
                .map(menuOrder -> {
                    Restaurant restaurant = menuOrder.getRestaurant();
                    MenuOrderForMemberDto dto = MenuOrderForMemberDto.builder()
                            .restaurantId(restaurant.getRestaurantId())
                            .restaurantName(restaurant.getName())
                            .orderId(menuOrder.getOrderId())
                            .orderType(menuOrder.getOrderType())
                            .orderStatus(menuOrder.getStatus())
                            .total(menuOrder.getTotal())
                            .reservationTime(menuOrder.getReservationTime().getTime())
                            .build();

                    // 取得餐廳圖片
                    byte[] photo = restaurant.getRestaurantPhotosList()
                            .stream()
                            .filter(p -> p.getPhotoMain() == 1)
                            .findFirst()
                            .map(RestaurantPhoto::getPhotoFile)
                            .orElse(null);
                    dto.setRestaurantPhoto(photo);

                    return dto;
                })
                .collect(Collectors.toList());

        return dtoList;
    }

    public MenuOrderDataForMemberRespDto getOrderData(Integer orderId) {
        MenuOrder menuOrder = menuOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 orderId => " + orderId + " 不存在"));

        MenuOrderDataForMemberRespDto respDto = new MenuOrderDataForMemberRespDto();

        Restaurant restaurant = menuOrder.getRestaurant();
        Courier courier = menuOrder.getCourier();
        List<MenuOrderDetail> orderDetailList = menuOrder.getOrderDetailList();

        // 組合餐廳資料
        RestaurantDataDto restaurantDto = RestaurantDataDto.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .address(restaurant.getCounty() + restaurant.getDistrict() + restaurant.getAddress())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
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

        // 組合外送員資料
        if (courier != null) {
            CourierDataDto courierDto = CourierDataDto.builder()
                    .courierId(courier.getCourierId())
                    .name(courier.getName())
                    .longitude(courier.getLongitude())
                    .latitude(courier.getLatitude())
                    .build();

            respDto.setCourier(courierDto);
        }

        // 組合訂單資料
        OrderDataDto orderDto = OrderDataDto.builder()
                .orderId(menuOrder.getOrderId())
                .orderType(menuOrder.getOrderType())
                .orderStatus(menuOrder.getStatus())
                .paymentType(menuOrder.getPaymentType())
                .reservationTime(menuOrder.getReservationTime().getTime())
                .deliveryAddress(menuOrder.getDeliveryAddress())
                .deliveryFee(menuOrder.getDeliveryFee())
                .platformFee(menuOrder.getPlatformFee())
                .subTotal(menuOrder.getSubTotal())
                .total(menuOrder.getTotal())
                .build();

        orderDto.setQrcode(menuOrder.getQrcode());
        respDto.setOrder(orderDto);

        // 組合訂單明細資料
        List<OrderDetailDataDto> orderDetailDtoList = orderDetailList.stream()
                .map(orderDetail -> {
                    Menu menu = orderDetail.getMenu();
                    OrderDetailDataDto dto = OrderDetailDataDto.builder()
                            .orderDetailId(orderDetail.getOrderDetailId())
                            .menuId(menu.getMenuId())
                            .menuName(menu.getName())
                            .price(orderDetail.getPrice())
                            .quantity(orderDetail.getQuantity())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());

        respDto.setOrderDetailList(orderDetailDtoList);
        return respDto;
    }

    @Transactional
    public MenuOrderCreateRespDto createOrder(MenuOrderCreateDto createDto) {
        MenuOrderCreateRespDto respDto = new MenuOrderCreateRespDto();

        MenuCart menuCart = menuCartRepository.findById(createDto.getCartId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 cartId => " + createDto.getCartId() + " 不存在"));

        Member member = menuCart.getMember();
        Restaurant restaurant = menuCart.getRestaurant();
        List<MenuCartItem> itemList = menuCart.getItemList();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date reservationTime = null;
        try {
            reservationTime = sdf.parse(createDto.getReservationTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        MenuOrder menuOrder = MenuOrder.builder()
                .member(member)
                .restaurant(restaurant)
                .courier(null)
                .orderType(menuCart.getOrderType())
                .deliveryAddress(createDto.getDeliveryAddress())
                .deliveryFee(menuCart.getDeliveryFee())
                .platformFee(menuCart.getPlatformFee())
                .paymentType(createDto.getPaymentType())
                .status("CREATED")
                .reservationTime(reservationTime)
                .qrcode(null)
                .createdTime(new Date())
                .build();

        // 計算餐點小計
        // Lambda 運算式中，無法改變外部變數的值，所以這裡使用 AtomicInteger 物件，來達成累加的效果
        AtomicInteger subTotalAtomic = new AtomicInteger(0);
        List<MenuOrderDetail> orderDetailList = itemList.stream()
                .map(item -> {
                    Menu menu = item.getMenu();
                    MenuOrderDetail menuOrderDetail = MenuOrderDetail.builder()
                            .menuOrder(menuOrder)
                            .menu(menu)
                            .price(menu.getPrice())
                            .quantity(item.getQuantity())
                            .build();

                    // 餐點小計，累加 (餐點價格 * 數量)
                    subTotalAtomic.addAndGet(menu.getPrice() * item.getQuantity());

                    return menuOrderDetail;
                })
                .collect(Collectors.toList());

        // 取得最後餐點小計的值
        Integer subTotal = subTotalAtomic.get();
        // 訂單總金額
        Integer total = subTotal + menuCart.getDeliveryFee() + menuCart.getPlatformFee();

        menuOrder.setSubTotal(subTotal);
        menuOrder.setTotal(total);
        menuOrder.setOrderDetailList(orderDetailList);

        // 新增訂單 MenuOrder、與訂單明細 MenuOrderDetail
        menuOrderRepository.save(menuOrder);

        // 刪除餐點購物車 MenuCart、餐點購物車明細 MenuCartItem
        menuCartRepository.delete(menuCart);

        // 產生 QR Code，QR Code 的 url 是更新訂單狀態的 API，例如：http://192.168.0.103:8080/menu-order/update/status
        // 要上面先新增 MenuOrder 成功後，才可以更新 QR Code 欄位，因為 QR Code 的內容，需要 orderId 作為請求參數
        // 模擬用手機掃描，更新訂單狀態成 FINISHED
        String scheme = request.getScheme();
        String ip = getWiFiIPv4Address();
        int port = request.getServerPort();

//        String content  = "http://192.168.0.103:8080/menu-order/update/status?orderId=" + menuOrder.getOrderId() + "&status=FINISHED";
        String content  = scheme + "://" + ip + ":" + port + "/menu-order/update/status?orderId=" + menuOrder.getOrderId() + "&status=FINISHED";
        byte[] qrcode = QRCodeUtil.generateQRCode(content);
        menuOrder.setQrcode(qrcode);

        // 如果是外送訂單，呼叫 assignCourier 方法分配外送員
        if ("DELIVERY".equals(menuOrder.getOrderType())) {
            Courier courier = assignCourier(restaurant);
            menuOrder.setCourier(courier);
        }

        // 更新訂單，包含更新 QR Code、更新外送員
        menuOrderRepository.save(menuOrder);

        // 如果訂單付款方式選擇 LINE Pay
        // 則發送請求給 LINE Pay Request API，取得 Line Pay 網頁付款連結，回傳給前端導向到付款頁面
        String webPaymentUrl = null;
        if ("LINE_PAY".equals(menuOrder.getPaymentType())) {
            // confirmUrl，設定消費者授權付款後，要跳轉商家的哪個頁面 URL，此為會員的查看餐點訂單頁面
            String confirmUrl = createDto.getConfirmUrl();
            // cancelUrl，設定消費者通過 LINE Pay 付款頁面，取消付款後要跳轉到的 URL，如果不設定，就傳入空字串 ""
            String cancelUrl = createDto.getCancelUrl();

            // productFormName，Line Pay 網頁付款頁面上，顯示的表單名稱，例如：「餐廳名稱」、「餐點訂單」、「商城訂單」...
            String productFormName = "";
            if ("DELIVERY".equals(menuOrder.getOrderType())) {
                productFormName = restaurant.getName() + " - 外送餐點訂單";
            } else if ("TAKEOUT".equals(menuOrder.getOrderType())) {
                productFormName = restaurant.getName() + " - 外帶自取餐點訂單";
            }

            webPaymentUrl = linePayService.getWebPaymentUrl(menuOrder.getOrderId(), menuOrder.getTotal(), confirmUrl, cancelUrl, productFormName);
        }

        respDto.setLinePayWebPaymentUrl(webPaymentUrl);
        return respDto;
    }

    /**
     * 訂單分配外送員，分配邏輯有兩個決定因素：
     * 1. 外送員與餐廳的距離(公里)，權重 0.4
     * 2. 外送員本月的訂單數量，權重 0.6
     * -> 加權平均：距離 * 0.4 + 訂單數量 * 0.6，找出加權平均最小的外送員，分配訂單
     * @param restaurant 餐廳，要取得餐廳的經緯度
     * @return {@link Courier} 加權平均值最小的外送員
     */
    public Courier assignCourier(Restaurant restaurant) {
        // 餐廳的經緯度
        BigDecimal restaurantLongitude = restaurant.getLongitude();
        BigDecimal restaurantLatitude = restaurant.getLatitude();

        // 帳號正常使用中(經過認證、沒有被停權)、且有上線(有登入)外送員
        List<Courier> courierList = courierRepository.findByStatusAndOnlineStatus(1, 1);

        List<Map<String, Object>> assignCourierList = courierList.stream()
                .map(courier -> {
                    HashMap<String, Object> courierMap = new HashMap<>();
                    // 外送員的經緯度
                    BigDecimal courierLongitude = courier.getLongitude();
                    BigDecimal courierLatitude = courier.getLatitude();

                    // 自己寫的方法，計算兩個經緯度地點之間的距離，單位是公里 km
                    // 這邊是計算餐廳與每個外送員的距離
                    Double distance = PositionHelper.getDistance(restaurantLongitude.doubleValue(), restaurantLatitude.doubleValue(),
                            courierLongitude.doubleValue(), courierLatitude.doubleValue());

                    // 計算外送員本月份的訂單數量
                    Integer orderCount = 0;
                    List<MenuOrder> courierOrderList = courier.getMenuOrderList();
                    if (courierOrderList != null) {
                        orderCount = courierOrderList.stream()
                                .filter(menuOrder -> {
                                    // 每個外送員，每一張訂單的創建時間
                                    Date createdTime = menuOrder.getCreatedTime();

                                    // 取得訂單的年份、月份，把 java.util Date 轉成 Joda-Time 的 DateTime
                                    DateTime orderDateTime = new DateTime(createdTime);
                                    int orderYear = orderDateTime.getYear();
                                    int orderMonth = orderDateTime.getMonthOfYear();

                                    // 現在時間的年份、月份
                                    DateTime now = new DateTime();
                                    int nowYear = now.getYear();
                                    int nowMonth = now.getMonthOfYear();

                                    // 如果訂單時間的年份、月份，與現在時間的年份、月份相同，代表是本月份的訂單
                                    return orderYear == nowYear && orderMonth == nowMonth;
                                })
                                .collect(Collectors.counting())
                                .intValue();
                    }

                    courierMap.put("courier", courier);
                    courierMap.put("distance", distance);
                    courierMap.put("orderCount", orderCount);
                    return courierMap;
                })
                .collect(Collectors.toList());

        // 訂單分配外送員，分配邏輯有兩個決定因素：
        // 1. 外送員與餐廳的距離(公里)，權重 0.4
        // 2. 外送員本月的訂單數量，權重 0.6
        // -> 加權平均：距離 * 0.4 + 訂單數量 * 0.6，找出加權平均最小的外送員，分配訂單
        // 使用 stream min，找到加權平均最小的外送員
        Optional<Courier> courierOptional = assignCourierList.stream()
                .min(Comparator.comparingDouble(courierMap -> {
                    Double distance = (Double) courierMap.get("distance");
                    Integer orderCount = (Integer) courierMap.get("orderCount");
                    return (distance * 0.4) + (orderCount * 0.6);
                }))
                .map(courierMap -> (Courier) courierMap.get("courier"));

        // 如果加權平均值最小的外送員存在，則回傳該外送員
        return courierOptional.orElse(null);
    }

    // 一般情境，更新餐點訂單 MenuOrder 的狀態，可能是消費者、餐廳後台、系統後台取消訂單
    public void updateOrderStatus(MenuOrderUpdateStatusDto dto) {
        MenuOrder menuOrder = menuOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 orderId => " + dto.getOrderId() + " 不存在"));

        menuOrder.setStatus(dto.getStatus());
        menuOrderRepository.save(menuOrder);
    }

    // 外送員手機掃描顧客的 QR Code 之後，模擬訂單完成，修改訂單狀態為 FINISHED
    // 並使用 WebSocket 推送訂單完成消息給前端
    public void updateOrderStatusFromQrcode(Integer orderId, String status) {
        MenuOrder menuOrder = menuOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "此 orderId => " + orderId + " 不存在"));

        // 原本的訂單狀態
        String originStatus = menuOrder.getStatus();

        // 原本的訂單狀態，是 "CREATED"、或 "PROCESSING" -> 代表是未完成的訂單，要更新狀態
        // 且這次要更新訂單的狀態成 "FINISHED" -> 代表外送員掃描 QR Code，模擬完成訂單
        // 以上兩個條件都符合，代表是外送員掃描 QR Code，完成訂單，發送 WebSocket 通知給前端，讓消費者知道訂單已完成
         if ( ("CREATED".equals(originStatus) || "PROCESSING".equals(originStatus) ) && "FINISHED".equals(status)) {

             // 更新訂單狀態
             menuOrder.setStatus(status);
             menuOrderRepository.save(menuOrder);

             // WebSocket 要發送到前端的目標路徑，也就是前端前端訂閱的頻道(端點)路徑
             String endPointUrl = "/member/menu-order/" + orderId + "/status/finished";

             // 自定義的 WebSocket 傳送通知、訊息的 Dto
             WebSocketMessage message = new WebSocketMessage();
             message.setContent("FINISHED");

             // 發送 WebSocket 到前端
             webSocketService.sendMenuOrderFinishedMessage(endPointUrl, message);
        }
    }

    public Page<MenuOrderForRestaurantRespDto> searchOrder(MenuOrderSearchForRestaurantDto searchDto, Pageable pageable) {
        // 把前端傳來的複合查詢參數，封裝成 MenuOrderSpecification 物件
        // 目的是針對每一個查詢條件，創建 Predicate 物件
        MenuOrderSpecification specification = MenuOrderSpecification.builder()
                .restaurantId(searchDto.getRestaurantId())
                .restaurantName(searchDto.getRestaurantName())
                .memberName(searchDto.getMemberName())
                .memberId(searchDto.getMemberId())
                .status(searchDto.getStatus())
                .orderType(searchDto.getOrderType())
                .orderCreatedTimeStart(searchDto.getOrderCreatedTimeStart())
                .orderCreatedTimeEnd(searchDto.getOrderCreatedTimeEnd())
                .build();

        // finaAll() 方法，第一個參數是 MenuOrderSpecification 查詢條件封裝的物件，第二個參數是 Pageable 分頁物件
        Page<MenuOrder> orderPage = menuOrderRepository.findAll(specification, pageable);

        // 把 MenuOrder 轉換成 MenuOrderForRestaurantRespDto，並放入 List 集合中
        List<MenuOrderForRestaurantRespDto> orderRespDtoList = orderPage.getContent().stream()
                .map(menuOrder -> {
                    MenuOrderForRestaurantRespDto orderRespDto = MenuOrderForRestaurantRespDto.builder()
                            .orderId(menuOrder.getOrderId())
                            .orderType(menuOrder.getOrderType())
                            .status(menuOrder.getStatus())
                            .total(menuOrder.getTotal())
                            .deliveryFee(menuOrder.getDeliveryFee())
                            .platformFee(menuOrder.getPlatformFee())
                            .subTotal(menuOrder.getSubTotal())
                            .paymentType(menuOrder.getPaymentType())
                            .reservationTime(menuOrder.getReservationTime().getTime())
                            .createdTime(menuOrder.getCreatedTime().getTime())
                            .deliveryAddress(menuOrder.getDeliveryAddress())
                            .build();

                    // 會員資料
                    Member member = menuOrder.getMember();
                    MemberDataDto memberDataDto = new MemberDataDto();
                    BeanUtils.copyProperties(member, memberDataDto);
                    orderRespDto.setMember(memberDataDto);

                    // 餐廳資料
                    Restaurant restaurant = menuOrder.getRestaurant();
                    RestaurantSimpleDto restaurantDto = RestaurantSimpleDto.builder()
                            .restaurantId(restaurant.getRestaurantId())
                            .name(restaurant.getName())
                            .address(restaurant.getAddress())
                            .build();
                    orderRespDto.setRestaurant(restaurantDto);

                    // 每一筆訂單的訂單明細資料
                    List<MenuOrderDetail> menuOrderDetailList = menuOrder.getOrderDetailList();
                    List<OrderDetailDataDto> orderDetailDtoList = menuOrderDetailList.stream()
                            .map(menuOrderDetail -> {
                                Menu menu = menuOrderDetail.getMenu();
                                OrderDetailDataDto orderDetailDto = OrderDetailDataDto.builder()
                                        .orderDetailId(menuOrderDetail.getOrderDetailId())
                                        .menuId(menu.getMenuId())
                                        .menuName(menu.getName())
                                        .price(menuOrderDetail.getPrice())
                                        .quantity(menuOrderDetail.getQuantity())
                                        .build();
                                return orderDetailDto;
                            })
                            .collect(Collectors.toList());

                    orderRespDto.setOrderDetailList(orderDetailDtoList);

                    // 處理完需要的資料後，回傳整筆訂單 Dto
                    return orderRespDto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(orderRespDtoList, orderPage.getPageable(), orderPage.getTotalElements());
    }

    public String getWiFiIPv4Address() {
        String ip = null;

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 检查介面名稱，是不是 "wlan" 開頭，代表是 WiFi
                if (networkInterface.getName().startsWith("wlan")) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        // IPv4 位址沒有冒號
                        if (!inetAddress.getHostAddress().contains(":")) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ip;
    }

}
