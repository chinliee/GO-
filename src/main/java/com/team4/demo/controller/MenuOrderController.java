package com.team4.demo.controller;

import com.team4.demo.model.dto.menuOrder.*;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.service.MenuOrderService;
import com.team4.demo.util.linepay.LinePayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class MenuOrderController {

    @Autowired
    private MenuOrderService menuOrderService;

    @Autowired
    private LinePayService linePayService;

    /**
     * 會員查看餐點訂單頁面，查詢該會員的所有的餐點訂單
     * @param memberId 會員 ID
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/menu-order/list/member")
    public ResponseEntity<ResponseDto> listOrderForMember(@RequestParam Integer memberId) {
        log.info("MenuOrderController - listOrderForMember ... memberId => {}", memberId);
        List<MenuOrderForMemberDto> orderList = menuOrderService.getOrderByMember(memberId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), orderList));
    }

    /**
     * 會員查看某一筆訂單資料，包含訂單、訂單明細、餐廳、外送員(如果是進行中的外送訂單)
     * @param orderId 餐點訂單 ID
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/menu-order/data")
    public ResponseEntity<ResponseDto> getOrderDataForMember(@RequestParam Integer orderId) {
        log.info("MenuOrderController - getOrderDataForMember ... orderId => {}", orderId);
        MenuOrderDataForMemberRespDto respDto = menuOrderService.getOrderData(orderId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    @PostMapping("/menu-order/create")
    public ResponseEntity<ResponseDto> createOrder(@RequestBody MenuOrderCreateDto createDto) {
        log.info("MenuOrderController - createOrder ... createDto => {}", createDto);
        MenuOrderCreateRespDto respDto = menuOrderService.createOrder(createDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.CREATED.value(), respDto));
    }

    /**
     * 一般情境，更新餐點訂單 MenuOrder 的狀態，可能是消費者、餐廳後台、系統後台取消訂單，使用 POST 方法
     * @param dto
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @PostMapping("/menu-order/update/status")
    public ResponseEntity<ResponseDto> updateOrderStatus(@RequestBody MenuOrderUpdateStatusDto dto) {
        log.info("MenuOrderController - updateOrderStatus ... dto => {}", dto);
        menuOrderService.updateOrderStatus(dto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    /**
     * 新增餐點訂單之後，會產生 QR Code，QR Code 的內容就是這支 API 的請求 url
     * 當外送員手機掃描顧客的 QR Code 之後，模擬訂單完成，修改訂單狀態為 FINISHED
     * 因為是 QR Code 的 url，要夾帶請求參數，所以這邊的更新餐點訂單 MenuOrder 狀態，使用 GET 方法
     * @param orderId
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/menu-order/update/status")
    public ResponseEntity<ResponseDto> updateOrderStatusFromQrcode(@RequestParam Integer orderId, @RequestParam String status) {
        log.info("MenuOrderController - updateOrderStatusFromQrcode ... orderId => {}, status => {}", orderId, status);
        menuOrderService.updateOrderStatusFromQrcode(orderId, status);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    /**
     * 餐廳後台、或管理員後台，複合查詢餐廳的餐點訂單
     * @param pageable 分頁、排序參數
     * @param searchDto 複合查詢參數，餐廳ID、會員姓名、訂單類型、訂單狀態、訂單時間區間，如果沒有傳入任何搜尋參數、或為 null、空字串等，就查詢全部餐點訂單
     * @return {@link ResponseEntity}<{@link ResponseDto}> 回傳帶有分頁的 Page 資料
     **/
    @PostMapping("/menu-order/search")
    public ResponseEntity<ResponseDto> searchOrder(
            @PageableDefault(page = 0, size = 10, sort = {"orderId"}, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestBody MenuOrderSearchForRestaurantDto searchDto) {
        log.info("MenuOrderController - searchOrder ... searchDto => {}, pageable => {}", searchDto, pageable);
        Page<MenuOrderForRestaurantRespDto> dtoPage = menuOrderService.searchOrder(searchDto, pageable);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), dtoPage));
    }

    // 測試，取得 Line Pay 網頁付款連結
    @GetMapping("/menu-order/linepay")
    public ResponseEntity<ResponseDto> getLinePayWebPaymentUrl() {
        log.info("MenuOrderController - getLinePayWebPaymentUrl ... ");
        String confirmUrl = "http://127.0.0.1:5502/member-menu-order.html";
        String cancelUrl = "http://127.0.0.1:5502/menu-checkout.html";
        String webPaymentUrl = linePayService.getWebPaymentUrl(500, 824, confirmUrl, cancelUrl, "各式各樣的訂單");
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), webPaymentUrl));
    }

}
