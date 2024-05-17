package com.team4.demo.controller;

import com.team4.demo.model.dto.menuCart.*;
import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.service.MenuCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class MenuCartController {

    @Autowired
    private MenuCartService menuCartService;

    /**
     * 查詢會員於此餐廳的餐點購物車資料，包含訂單類型外送或外帶自取、外送費用、平台費用，以及購物車的餐點明細資料(餐點名稱、價錢、圖片)
     * @param memberId 會員 ID
     * @param restaurantId 餐廳 ID
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/menu-cart/list")
    public ResponseEntity<ResponseDto> listMenuCart(@RequestParam Integer memberId, @RequestParam Integer restaurantId) {
        log.info("MenuCartController - listMenuCart ... memberId => {}, restaurantId => {}", memberId, restaurantId);
        MenuCartSearchRespDto respDto = menuCartService.getCartByMemberAndRestaurant(memberId, restaurantId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    @PostMapping("/menu-cart/create/item")
    public ResponseEntity<ResponseDto> createItem(@RequestBody MenuCartItemCreateDto createDto) {
        log.info("MenuCartController - createItem ... createDto => {}", createDto);
        MenuCartItemSearchRespDto respDto = menuCartService.createItem(createDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    /**
     * 修改餐點購物車中，某一項餐點的數量，包含數量增加、數量減少
     * @param updateDto {@link MenuCartItemUpdateQtyDto}
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @PostMapping("/menu-cart/update/item/quantity")
    public ResponseEntity<ResponseDto> updateItemQuantity(@RequestBody MenuCartItemUpdateQtyDto updateDto) {
        log.info("MenuCartController - updateItemQuantity ... updateDto => {}", updateDto);
        MenuCartItemUpdateQtyRespDto respDto = menuCartService.updateItemQuantity(updateDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    /** 刪除餐點購物車中的餐點，因為餐點修改數量 -1 後，餐點數量等於0，
     *  但要檢查刪除餐點後，如果購物車中已經沒有任何餐點，就要連購物車一起刪除
     * @param deleteDto {@link MenuCartItemDeleteDto}
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @PostMapping("/menu-cart/delete/item")
    public ResponseEntity<ResponseDto> deleteItem(@RequestBody MenuCartItemDeleteDto deleteDto) {
        log.info("MenuCartController - deleteItem ... deleteDto => {}", deleteDto);
        menuCartService.deleteItem(deleteDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value()));
    }

    /**
     * 修改餐點購物車 MenuCart 資料，如訂單類型-外送或外帶自取、外送費、平台費用
     * @param updateDto {@link MenuCartUpdateDto}
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @PostMapping("/menu-cart/update/cart")
    public ResponseEntity<ResponseDto> updateCart(@RequestBody MenuCartUpdateDto updateDto) {
        log.info("MenuCartController - updateCart ... updateDto => {}", updateDto);
        MenuCartUpdateRespDto respDto = menuCartService.updateCart(updateDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    /**
     * 餐點購物車結帳頁面，取得所需資訊，會員地址、會員信用卡、餐廳地址、餐廳營業時間、餐點購物車資料
     * @param memberId 會員 ID
     * @param restaurantId 餐廳 ID
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/menu-cart/checkout")
    public ResponseEntity<ResponseDto> getCheckoutInfo(@RequestParam Integer memberId, @RequestParam Integer restaurantId) {
        log.info("MenuCartController - getCheckoutInfo ... memberId => {}, restaurantId => {}", memberId, restaurantId);
        MenuCartCheckoutRespDto respDto = menuCartService.getCheckoutInfo(memberId, restaurantId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

}
