package com.team4.demo.service;

import com.team4.demo.model.dto.statistic.*;
import com.team4.demo.model.entity.MenuOrder;
import com.team4.demo.model.repository.MenuOrderRepository;
import com.team4.demo.model.repository.RestaurantRepository;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    @Autowired
    private MenuOrderRepository menuOrderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public MenuOrderStatisticFromAllRestaurantRespDto getMenuOrderStatisticFromAllRestaurant() {
        MenuOrderStatisticFromAllRestaurantRespDto respDto = new MenuOrderStatisticFromAllRestaurantRespDto();

        // 查詢全平台(全部餐廳) 的餐點訂單統計資料
        // 1-1 查詢今日訂單銷售數據，且排除狀態是取消的訂單，舉例如果今天是 2024/1/17，就會查詢 2024/1/17 00:00:00 - 2024/1/17 23:59:59 的訂單
        DateTime todayStart = new DateTime().withTimeAtStartOfDay();
        DateTime todayEnd = new DateTime().withTimeAtStartOfDay().plusDays(1).minusSeconds(1);
        List<MenuOrder> todayOrderList = menuOrderRepository
                .findByStatusNotAndCreatedTimeBetween(
                        "CANCELED", todayStart.toDate(), todayEnd.toDate());

        // 計算今日訂單的數量、銷售額(餐點總共小計，不算外送費、平台費)
        Integer todayOrderCount = todayOrderList.size();
        Integer todaySales = todayOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        MenuOrderSalesNumberRespDto todaySalesNumber = MenuOrderSalesNumberRespDto.builder()
                .orderCount(todayOrderCount)
                .sales(todaySales)
                .build();

        respDto.setTodaySalesNumber(todaySalesNumber);

        // 1-2 查詢本月訂單銷售數據，舉例如果今天是 2024/1/17，就會查詢 2024/1/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        DateTime monthStart = new DateTime().withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime monthEnd = monthStart.plusMonths(1).withTimeAtStartOfDay().minusSeconds(1);
        List<MenuOrder> monthOrderList = menuOrderRepository
                .findByStatusNotAndCreatedTimeBetween(
                        "CANCELED", monthStart.toDate(), monthEnd.toDate());

        // 計算本月訂單的數量、銷售額
        Integer monthOrderCount = monthOrderList.size();
        Integer monthSales = monthOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        MenuOrderSalesNumberRespDto monthSalesNumber = MenuOrderSalesNumberRespDto.builder()
                .orderCount(monthOrderCount)
                .sales(monthSales)
                .build();

        respDto.setMonthSalesNumber(monthSalesNumber);

        // 1-3 查詢區間訂單銷售數據，預設區間是今年度，舉例如果今天是 2024/2/17，就會查詢 2024/1/1 00:00:00 - 2024/2/28 23:59:59 的訂單
        DateTime rangeStart = new DateTime().withDayOfYear(1).withTimeAtStartOfDay();
        List<MenuOrder> rangeOrderList = menuOrderRepository
                .findByStatusNotAndCreatedTimeBetween(
                        "CANCELED", rangeStart.toDate(), monthEnd.toDate());

        // 計算今年訂單的數量、銷售額(餐點總共小計，不算外送費、平台費)
        Integer rangeOrderCount = rangeOrderList.size();
        Integer rangeSales = rangeOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        MenuOrderSalesNumberRespDto rangeSalesNumber = MenuOrderSalesNumberRespDto.builder()
                .orderCount(rangeOrderCount)
                .sales(rangeSales)
                .build();

        respDto.setRangeSalesNumber(rangeSalesNumber);

        // 2 查詢時間區間內，預設是從今天開始，往前推12個月內的訂單，以月為單位統計，每個月的訂單總數量、總銷售額
        // 假如今天是 2024/1/17，就會查詢 2023/2/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        List<MenuOrderMonthSalesRespDto> monthSalesDtoList = new ArrayList<>();
        DateTime monthRangeStart = new DateTime().minusMonths(11).withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime monthRangeEnd = new DateTime().withDayOfMonth(1).withTimeAtStartOfDay().plusMonths(1).minusSeconds(1);

        List<MenuOrder> menuRangeOrderList = menuOrderRepository
                .findByStatusNotAndCreatedTimeBetween(
                        "CANCELED", monthRangeStart.toDate(), monthRangeEnd.toDate());

        // 開始計算每月各自的訂單數量、銷售額
        for (int i = 0; i <= 11; i++) {
            // 每月的起始時間、結束時間
            DateTime monthStartTemp = monthRangeStart.plusMonths(i);
            DateTime monthEndTemp = monthStartTemp.plusMonths(1);

            // 格式化要傳到前端的月份文字，使用 JodaTime 的 DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM");
            MenuOrderMonthSalesRespDto monthSalesRespDto = MenuOrderMonthSalesRespDto.builder()
                    .month(formatter.print(monthStartTemp))
                    .orderCount(0)
                    .sales(0)
                    .build();

            // 判斷訂單的建立時間，是否在每月的起始時間、結束時間之間
            for (MenuOrder order : menuRangeOrderList) {
                Integer orderCount = monthSalesRespDto.getOrderCount();
                Integer sales = monthSalesRespDto.getSales();

                // 把訂單的 createdTime，本來是 util Date，轉成 JodaTime 的 DateTime，
                // 並使用 JodaTime 的 isAfter、isBefore 方法，判斷訂單成立時間是否在每月的起始時間、結束時間之間
                DateTime createdTime = new DateTime(order.getCreatedTime());
                if (createdTime.isAfter(monthStartTemp) && createdTime.isBefore(monthEndTemp)) {
                    monthSalesRespDto.setOrderCount(orderCount + 1);
                    monthSalesRespDto.setSales(sales + order.getSubTotal());
                }
            }
            monthSalesDtoList.add(monthSalesRespDto);
        }

        respDto.setMonthSalesList(monthSalesDtoList);

        // 3 在時間區間內，根據訂單銷售額排序，查詢銷售額最高的5家餐廳
        // 預設時間區間是本月，舉例如果今天是 2024/1/17，就會查詢 2024/1/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        List<Map<String, Object>> topRestaurantBySales =
                menuOrderRepository.findTop5RestaurantByMenuOrderSales(
                        monthStart.toString("yyyy-MM-dd HH:mm:ss"), monthEnd.toString("yyyy-MM-dd HH:mm:ss"));

        List<MenuOrderTopRestaurantRespDto> topRestaurantBySalesRespDtoList = topRestaurantBySales.stream()
                .map(restaurantMap -> {
                    MenuOrderTopRestaurantRespDto topRestaurantRespDto = MenuOrderTopRestaurantRespDto.builder()
                            .restaurantId((Integer) restaurantMap.get("restaurantId"))
                            .restaurantName((String) restaurantMap.get("restaurantName"))
                            .sales((Integer) restaurantMap.get("sales"))
                            .orderCount((Integer) restaurantMap.get("orderCount"))
                            .build();
                    return topRestaurantRespDto;
                })
                .collect(Collectors.toList());

        respDto.setTopRestaurantBySalesList(topRestaurantBySalesRespDtoList);

        // 4 查詢時間區間內，根據餐點訂單總數量排序，查詢最高的5家餐廳
        // 預設時間區間是本月，舉例如果今天是 2024/1/17，就會查詢 2024/1/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        List<Map<String, Object>> topRestaurantByOrderCount =
                menuOrderRepository.findTop5RestaurantByMenuOrderCount(
                        monthStart.toString("yyyy-MM-dd HH:mm:ss"), monthEnd.toString("yyyy-MM-dd HH:mm:ss"));

        List<MenuOrderTopRestaurantRespDto> topRestaurantByOrderCountRespDtoList = topRestaurantByOrderCount.stream()
                .map(restaurantMap -> {
                    MenuOrderTopRestaurantRespDto topRestaurantRespDto = MenuOrderTopRestaurantRespDto.builder()
                            .restaurantId((Integer) restaurantMap.get("restaurantId"))
                            .restaurantName((String) restaurantMap.get("restaurantName"))
                            .sales((Integer) restaurantMap.get("sales"))
                            .orderCount((Integer) restaurantMap.get("orderCount"))
                            .build();
                    return topRestaurantRespDto;
                })
                .collect(Collectors.toList());

        respDto.setTopRestaurantByOrderCountList(topRestaurantByOrderCountRespDtoList);
        return respDto;
    }

    public MenuOrderStatisticFromOneRestaurantRespDto getMenuOrderStatisticFromOneRestaurant(Integer restaurantId) {
        MenuOrderStatisticFromOneRestaurantRespDto respDto = new MenuOrderStatisticFromOneRestaurantRespDto();

        // 根據 restaurantId 查詢餐廳，如果有查到資料就設定 Dto 屬性餐廳名稱
        restaurantRepository.findById(restaurantId)
                .ifPresent(restaurant -> respDto.setRestaurantName(restaurant.getName()));

        // 查詢全平台(全部餐廳) 的餐點訂單統計資料
        // 1-1 查詢今日訂單銷售數據，且排除狀態是取消的訂單，舉例如果今天是 2024/1/17，就會查詢 2024/1/17 00:00:00 - 2024/1/17 23:59:59 的訂單
        DateTime todayStart = new DateTime().withTimeAtStartOfDay();
        DateTime todayEnd = new DateTime().withTimeAtStartOfDay().plusDays(1).minusSeconds(1);
        List<MenuOrder> todayOrderList = menuOrderRepository
                .findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(
                        restaurantId, "CANCELED", todayStart.toDate(), todayEnd.toDate());

        // 計算今日訂單的數量、銷售額(餐點總共小計，不算外送費、平台費)
        Integer todayOrderCount = todayOrderList.size();
        Integer todaySales = todayOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        MenuOrderSalesNumberRespDto todaySalesNumber = MenuOrderSalesNumberRespDto.builder()
                .orderCount(todayOrderCount)
                .sales(todaySales)
                .build();

        respDto.setTodaySalesNumber(todaySalesNumber);

        // 1-2 查詢本月訂單銷售數據，舉例如果今天是 2024/1/17，就會查詢 2024/1/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        DateTime monthStart = new DateTime().withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime monthEnd = monthStart.plusMonths(1).withTimeAtStartOfDay().minusSeconds(1);
        List<MenuOrder> monthOrderList = menuOrderRepository
                .findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(
                        restaurantId, "CANCELED", monthStart.toDate(), monthEnd.toDate());

        // 計算本月訂單的數量、銷售額
        Integer monthOrderCount = monthOrderList.size();
        Integer monthSales = monthOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        MenuOrderSalesNumberRespDto monthSalesNumber = MenuOrderSalesNumberRespDto.builder()
                .orderCount(monthOrderCount)
                .sales(monthSales)
                .build();

        respDto.setMonthSalesNumber(monthSalesNumber);

        // 1-3 查詢區間訂單銷售數據，預設區間是今年度，舉例如果今天是 2024/2/17，就會查詢 2024/1/1 00:00:00 - 2024/2/28 23:59:59 的訂單
        DateTime rangeStart = new DateTime().withDayOfYear(1).withTimeAtStartOfDay();
        List<MenuOrder> rangeOrderList = menuOrderRepository
                .findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(
                        restaurantId, "CANCELED", rangeStart.toDate(), monthEnd.toDate());

        // 計算今年訂單的數量、銷售額
        Integer rangeOrderCount = rangeOrderList.size();
        Integer rangeSales = rangeOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        MenuOrderSalesNumberRespDto rangeSalesNumber = MenuOrderSalesNumberRespDto.builder()
                .orderCount(rangeOrderCount)
                .sales(rangeSales)
                .build();

        respDto.setRangeSalesNumber(rangeSalesNumber);

        // 2 查詢時間區間內，預設是從今天開始，往前推12個月內的訂單，以月為單位統計，每個月的訂單總數量、總銷售額
        // 假如今天是 2024/1/17，就會查詢 2023/2/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        List<MenuOrderMonthSalesRespDto> monthSalesDtoList = new ArrayList<>();
        DateTime monthRangeStart = new DateTime().minusMonths(11).withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime monthRangeEnd = new DateTime().withDayOfMonth(1).withTimeAtStartOfDay().plusMonths(1).minusSeconds(1);

        List<MenuOrder> menuRangeOrderList = menuOrderRepository
                .findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(
                        restaurantId,"CANCELED", monthRangeStart.toDate(), monthRangeEnd.toDate());

        // 開始計算每月各自的訂單數量、銷售額
        for (int i = 0; i <= 11; i++) {
            // 每月的起始時間、結束時間
            DateTime monthStartTemp = monthRangeStart.plusMonths(i);
            DateTime monthEndTemp = monthStartTemp.plusMonths(1);

            // 格式化要傳到前端的月份文字，使用 JodaTime 的 DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM");
            MenuOrderMonthSalesRespDto monthSalesRespDto = MenuOrderMonthSalesRespDto.builder()
                    .month(formatter.print(monthStartTemp))
                    .orderCount(0)
                    .sales(0)
                    .build();

            // 判斷訂單的建立時間，是否在每月的起始時間、結束時間之間
            for (MenuOrder order : menuRangeOrderList) {
                Integer orderCount = monthSalesRespDto.getOrderCount();
                Integer sales = monthSalesRespDto.getSales();

                // 把訂單的 createdTime，本來是 util Date，轉成 JodaTime 的 DateTime，
                // 並使用 JodaTime 的 isAfter、isBefore 方法，判斷訂單成立時間是否在每月的起始時間、結束時間之間
                DateTime createdTime = new DateTime(order.getCreatedTime());
                if (createdTime.isAfter(monthStartTemp) && createdTime.isBefore(monthEndTemp)) {
                    monthSalesRespDto.setOrderCount(orderCount + 1);
                    monthSalesRespDto.setSales(sales + order.getSubTotal());
                }
            }
            monthSalesDtoList.add(monthSalesRespDto);
        }

        respDto.setMonthSalesList(monthSalesDtoList);

        // 3 本月 Top5 銷售額餐點
        List<Map<String, Object>> topMenuList1 = menuOrderRepository.findTop5MenuBySales(
                restaurantId, monthStart.toString("yyyy-MM-dd HH:mm:ss"), monthEnd.toString("yyyy-MM-dd HH:mm:ss"));

        List<MenuOrderTopMenuRespDto> topMenuBySalesList = topMenuList1.stream()
                .map(menuMap -> {
                    MenuOrderTopMenuRespDto dto = MenuOrderTopMenuRespDto.builder()
                            .menuId((Integer) menuMap.get("menuId"))
                            .menuName((String) menuMap.get("menuName"))
                            .sales((Integer) menuMap.get("sales"))
                            .quantity((Integer) menuMap.get("quantity"))
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());

        respDto.setTopMenuBySalesList(topMenuBySalesList);

        // 4 本月 Top5 銷售量餐點
        List<Map<String, Object>> topMenuList2 = menuOrderRepository.findTop5MenuByQuantity(
                restaurantId, monthStart.toString("yyyy-MM-dd HH:mm:ss"), monthEnd.toString("yyyy-MM-dd HH:mm:ss"));

        List<MenuOrderTopMenuRespDto> topMenuByQuantityList = topMenuList2.stream()
                .map(menuMap -> {
                    MenuOrderTopMenuRespDto dto = MenuOrderTopMenuRespDto.builder()
                            .menuId((Integer) menuMap.get("menuId"))
                            .menuName((String) menuMap.get("menuName"))
                            .sales((Integer) menuMap.get("sales"))
                            .quantity((Integer) menuMap.get("quantity"))
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());

        respDto.setTopMenuByQuantityList(topMenuByQuantityList);
        return respDto;
    }

    public MenuOrderSalesNumberRespDto getMenuOrderSalesNumber(Integer restaurantId, Long startTime, Long endTime) {
        MenuOrderSalesNumberRespDto respDto = new MenuOrderSalesNumberRespDto();

        DateTime rangeStart = new DateTime(startTime).withTimeAtStartOfDay();
        DateTime rangeEnd = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusSeconds(1);

        // 查詢全平台、全部餐廳銷售資料
        List<MenuOrder> menuOrderList = new ArrayList<>();
        if (restaurantId == null) {
            menuOrderList = menuOrderRepository.findByStatusNotAndCreatedTimeBetween(
                    "CANCELED", rangeStart.toDate(), rangeEnd.toDate());
        }
        // 查詢單一餐廳銷售資料
        else {
            menuOrderList = menuOrderRepository.findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(
                    restaurantId, "CANCELED", rangeStart.toDate(), rangeEnd.toDate());
        }

        // 計算區間訂單總數量、總銷售額(餐點總共小計，不算外送費、平台費)
        Integer orderCount = menuOrderList.size();
        Integer sales = menuOrderList.stream()
                .mapToInt(MenuOrder::getSubTotal)
                .sum();

        respDto.setOrderCount(orderCount);
        respDto.setSales(sales);
        return respDto;
    }

    public List<MenuOrderMonthSalesRespDto> getMenuOrderMonthSales(Integer restaurantId, Long startTime, Long endTime) {
        List<MenuOrderMonthSalesRespDto> dtoList = new ArrayList<>();

        DateTime monthRangeStart = new DateTime(startTime).withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime monthRangeEnd = new DateTime(endTime).withDayOfMonth(1).plusMonths(1).withTimeAtStartOfDay().minusSeconds(1);
        // 計算區間起始時間、區間結束時間，兩者之間相差幾個月
        // 舉例來說，monthRangeStart 是 2023/2/1 00:00:00，monthRangeEnd 是 2024/1/31 23:59:59，相差11個月
        int monthsBetween = Months.monthsBetween(monthRangeStart, monthRangeEnd).getMonths();

        // 查詢全平台、全部餐廳月份銷售資料
        List<MenuOrder> menuOrderList = new ArrayList<>();
        if (restaurantId == null) {
            menuOrderList = menuOrderRepository.findByStatusNotAndCreatedTimeBetween(
                    "CANCELED", monthRangeStart.toDate(), monthRangeEnd.toDate());
        }
        // 查詢單一餐廳月份銷售資料
        else {
            menuOrderList = menuOrderRepository.findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(
                    restaurantId, "CANCELED", monthRangeStart.toDate(), monthRangeEnd.toDate());
        }

        // 計算每月各自的訂單數量、銷售額
        for (int i = 0; i <= monthsBetween; i++) {
            // 每月的起始時間、結束時間
            DateTime monthStartTemp = monthRangeStart.plusMonths(i);
            DateTime monthEndTemp = monthStartTemp.plusMonths(1);

            // 格式化要傳到前端的月份文字，使用 JodaTime 的 DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy/MM");
            MenuOrderMonthSalesRespDto respDto = MenuOrderMonthSalesRespDto.builder()
                    .month(formatter.print(monthStartTemp))
                    .orderCount(0)
                    .sales(0)
                    .build();

            // 判斷訂單的建立時間，是否在每月的起始時間、結束時間之間
            for (MenuOrder order : menuOrderList) {
                Integer orderCount = respDto.getOrderCount();
                Integer sales = respDto.getSales();

                // 把訂單的 createdTime，本來是 util Date，轉成 JodaTime 的 DateTime，
                // 並使用 JodaTime 的 isAfter、isBefore 方法，判斷訂單成立時間是否在每月的起始時間、結束時間之間
                DateTime createdTime = new DateTime(order.getCreatedTime());
                if (createdTime.isAfter(monthStartTemp) && createdTime.isBefore(monthEndTemp)) {
                    respDto.setOrderCount(orderCount + 1);
                    respDto.setSales(sales + order.getSubTotal());
                }
            }
            dtoList.add(respDto);
        }

        return dtoList;
    }

    public List<MenuOrderTopRestaurantRespDto> getMenuOrderTopRestaurant(String orderBy, Long startTime, Long endTime) {
        DateTime rangeStart = new DateTime(startTime).withTimeAtStartOfDay();
        DateTime rangeEnd = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusSeconds(1);

        List<Map<String, Object>> topRestaurantList = new ArrayList<>();
        // 在時間區間內，根據訂單銷售額排序，查詢銷售額最高的5家餐廳
        if ("sales".equals(orderBy)) {
            topRestaurantList = menuOrderRepository.findTop5RestaurantByMenuOrderSales(
                    rangeStart.toString("yyyy-MM-dd HH:mm:ss"), rangeEnd.toString("yyyy-MM-dd HH:mm:ss"));
        }
        // 在時間區間內，根據訂單數量排序，查詢訂單總數量最高的5家餐廳
        else if ("orderCount".equals(orderBy)) {
            topRestaurantList= menuOrderRepository.findTop5RestaurantByMenuOrderCount(
                    rangeStart.toString("yyyy-MM-dd HH:mm:ss"), rangeEnd.toString("yyyy-MM-dd HH:mm:ss"));
        }

        List<MenuOrderTopRestaurantRespDto> dtoList = topRestaurantList.stream()
                .map(restaurantMap -> {
                    MenuOrderTopRestaurantRespDto respDto = MenuOrderTopRestaurantRespDto.builder()
                            .restaurantId((Integer) restaurantMap.get("restaurantId"))
                            .restaurantName((String) restaurantMap.get("restaurantName"))
                            .sales((Integer) restaurantMap.get("sales"))
                            .orderCount((Integer) restaurantMap.get("orderCount"))
                            .build();
                    return respDto;
                })
                .collect(Collectors.toList());

        return dtoList;
    }

    public List<MenuOrderTopMenuRespDto> getMenuOrderTopMenu(Integer restaurantId, String orderBy) {
        // 區間範圍，這邊固定為本月，舉例如果今天是 2024/1/17，就會查詢 2024/1/1 00:00:00 - 2024/1/31 23:59:59 的訂單
        DateTime monthStart = new DateTime().withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime monthEnd = monthStart.plusMonths(1).withTimeAtStartOfDay().minusSeconds(1);

        List<Map<String, Object>> topMenuList = new ArrayList<>();
        // 查詢本月餐點訂單，根據餐點的銷售額排序，該餐廳 Top5 銷售額餐點
        if ("sales".equals(orderBy)) {
            topMenuList = menuOrderRepository.findTop5MenuBySales(
                    restaurantId, monthStart.toString("yyyy-MM-dd HH:mm:ss"), monthEnd.toString("yyyy-MM-dd HH:mm:ss"));
        }
        // 查詢本月餐點訂單，根據餐點的銷售量排序，該餐廳 Top5 銷售量餐點
        else if ("quantity".equals(orderBy)) {
            topMenuList = menuOrderRepository.findTop5MenuByQuantity(
                    restaurantId, monthStart.toString("yyyy-MM-dd HH:mm:ss"), monthEnd.toString("yyyy-MM-dd HH:mm:ss"));
        }

        List<MenuOrderTopMenuRespDto> dtoList = topMenuList.stream()
                .map(menuMap -> {
                    MenuOrderTopMenuRespDto respDto = MenuOrderTopMenuRespDto.builder()
                            .menuId((Integer) menuMap.get("menuId"))
                            .menuName((String) menuMap.get("menuName"))
                            .sales((Integer) menuMap.get("sales"))
                            .quantity((Integer) menuMap.get("quantity"))
                            .build();
                    return respDto;
                })
                .collect(Collectors.toList());

        return dtoList;
    }

}
