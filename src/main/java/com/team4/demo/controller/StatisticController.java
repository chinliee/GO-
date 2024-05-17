package com.team4.demo.controller;

import com.team4.demo.model.dto.response.ResponseDto;
import com.team4.demo.model.dto.statistic.*;
import com.team4.demo.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    /**
     * 查詢全平台(全部餐廳) 的餐點訂單統計資料，包含
     * 1. 今日訂單數量與銷售額、本月訂單數量與銷售額、預設區間訂單數量與銷售額，
     *    預設區間是本年度，舉例如果今天是 2024/2/17，就會查詢 2024/1/1 00:00:00 - 2024/2/28 23:59:59 的訂單
     * 2. 時間區間內，以月為單位統計，每個月的訂單總數量、總銷售額
     *    預設是過去一年，舉例如果今天是 2024/1/17，就會查詢 2023/2/1 00:00:00 - 2024/1/31 23:59:59 的訂單
     * 3. 時間區間內，預設是本月(1-31號)，根據餐點訂單總數量排序，查詢最高的5家餐廳
     * 4. 時間區間內，預設是本月(1-31號)，根據餐點訂單銷售額排序，查詢最高的5家餐廳
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/statistic/menu-order/all-restaurant")
    public ResponseEntity<ResponseDto> getMenuOrderStatisticFromAllRestaurant() {
        log.info("StatisticController - getMenuOrderStatisticFromAllRestaurant ...");
        MenuOrderStatisticFromAllRestaurantRespDto respDto = statisticService.getMenuOrderStatisticFromAllRestaurant();
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    /**
     * 查詢單一餐廳的餐點訂單統計資料，包含
     * 1. 今日訂單數量與銷售額、本月訂單數量與銷售額、預設區間訂單數量與銷售額，
     *    預設區間是本年度，舉例如果今天是 2024/2/17，就會查詢 2024/1/1 00:00:00 - 2024/2/28 23:59:59 的訂單
     * 2. 時間區間內，以月為單位統計，每個月的訂單總數量、總銷售額
     *    預設是過去一年，舉例如果今天是 2024/1/17，就會查詢 2023/2/1 00:00:00 - 2024/1/31 23:59:59 的訂單
     * 3. 本月 Top5 銷售額餐點
     * 4. 本月 Top5 銷售量餐點
     * @param restaurantId，餐廳ID
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/statistic/menu-order/one-restaurant")
    public ResponseEntity<ResponseDto> getMenuOrderStatisticFromOneRestaurant(@RequestParam Integer restaurantId) {
        log.info("StatisticController - getMenuOrderStatisticFromOneRestaurant ... restaurantId => {}", restaurantId);
        MenuOrderStatisticFromOneRestaurantRespDto respDto = statisticService.getMenuOrderStatisticFromOneRestaurant(restaurantId);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    /**
     * 查詢時間區間內，餐點訂單的訂單總數量、總銷售額。可以細分為查詢全平台(全部餐廳)、單一餐廳
     * @param restaurantId，restaurantId 如果為 null，就查詢全平台、全部餐廳銷售資料；如果有值，就查詢該餐廳的銷售資料
     * @param startTime，區間起始時間，年/月/日
     * @param endTime，區間結束時間，年/月/日
     * @return {@link ResponseEntity}<{@link ResponseDto}> 訂單總數量、總銷售額
     */
    @GetMapping("/statistic/menu-order/sales-number")
    public ResponseEntity<ResponseDto> getMenuOrderSalesNumber(@RequestParam(required = false) Integer restaurantId,
                                                               @RequestParam Long startTime,
                                                               @RequestParam Long endTime) {
        log.info("StatisticController - getMenuOrderSalesNumber ... restaurantId => {}, startTime => {}, endTime => {}", restaurantId, startTime, endTime);
        MenuOrderSalesNumberRespDto respDto = statisticService.getMenuOrderSalesNumber(restaurantId, startTime, endTime);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), respDto));
    }

    /**
     * 查詢時間區間內，以月為單位統計，每個月的訂單總數量、總銷售額。可以細分為查詢全平台(全部餐廳)、單一餐廳
     * @param restaurantId，restaurantId 如果為 null，就查詢全平台、全部餐廳銷售資料；如果有值，就查詢該餐廳的銷售資料
     * @param startTime，區間起始時間，年/月
     * @param endTime，區間結束時間，年/月
     * @return {@link ResponseEntity}<{@link ResponseDto}>，每個月的訂單總數量、總銷售額
     */
    @GetMapping("/statistic/menu-order/month-sales")
    public ResponseEntity<ResponseDto> getMenuOrderMonthSales(@RequestParam(required = false) Integer restaurantId,
                                                              @RequestParam Long startTime,
                                                              @RequestParam Long endTime) {
        log.info("StatisticController - getMenuOrderSalesNumber ... restaurantId => {}, startTime => {}, endTime => {}", restaurantId, startTime, endTime);
        List<MenuOrderMonthSalesRespDto> dtoList = statisticService.getMenuOrderMonthSales(restaurantId, startTime, endTime);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), dtoList));
    }

    /**
     * 查詢時間區間內，根據餐點訂單總銷售額、或訂單總數量排序，查詢最高的5家餐廳
     * @param orderBy，排序方式， 銷售額排序 sales、訂單數量排序 orderCount
     * @param startTime，區間起始時間，年/月/日
     * @param endTime，區間結束時間，年/月/日
     * @return {@link ResponseEntity}<{@link ResponseDto}> 餐廳ID、餐廳名稱、訂單總數量、總銷售額
     */
    @GetMapping("/statistic/menu-order/top-restaurant")
    public ResponseEntity<ResponseDto> getMenuOrderTopRestaurant(@RequestParam String orderBy,
                                                                 @RequestParam Long startTime,
                                                                 @RequestParam Long endTime) {
        log.info("StatisticController - getMenuOrderTopRestaurant ... orderBy => {}, startTime => {}, endTime => {}", orderBy, startTime, endTime);
        List<MenuOrderTopRestaurantRespDto> dtoList = statisticService.getMenuOrderTopRestaurant(orderBy, startTime, endTime);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), dtoList));
    }

    /**
     * 針對單一餐廳，本月餐點訂單，分別依據餐點的銷售額、銷售量排序，查詢 Top5 銷售額餐點、Top5 銷售量餐點
     * @param restaurantId，餐廳ID
     * @param orderBy，排序方式，餐點銷售額排序 sales、餐點銷售量排序 quantity
     * @return {@link ResponseEntity}<{@link ResponseDto}>
     */
    @GetMapping("/statistic/menu-order/top-menu")
    public ResponseEntity<ResponseDto> getMenuOrderTopMenu(@RequestParam Integer restaurantId,
                                                           @RequestParam String orderBy) {
        log.info("StatisticController - getMenuOrderTopMenu ... restaurantId => {}, orderBy => {}", restaurantId, orderBy);
        List<MenuOrderTopMenuRespDto> dtoList = statisticService.getMenuOrderTopMenu(restaurantId, orderBy);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK.value(), dtoList));
    }

}
