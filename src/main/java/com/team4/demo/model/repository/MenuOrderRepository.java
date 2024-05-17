package com.team4.demo.model.repository;

import com.team4.demo.model.entity.MenuOrder;
import com.team4.demo.model.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder, Integer>, JpaSpecificationExecutor<MenuOrder> {

    List<MenuOrder> findByRestaurant_RestaurantIdAndStatusNotAndCreatedTimeBetween(Integer restaurantId, String status, Date startTime, Date endTime);

    List<MenuOrder> findByStatusNotAndCreatedTimeBetween(String canceled, Date startTime, Date endTime);

    /**
     * 在時間區間內，根據訂單銷售額排序，查詢銷售額最高的5家餐廳
     * @param startTime，區間開始時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @param endTime，區間結束時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @return {@link List}<{@link Map}<{@link String},{@link Object}>>
     * 用 List<Map<String,Object>> 接收查詢結果，List 中的每一個元素代表查詢的每一列資料
     * Map 中的 key 為查詢結果集的欄位名稱 (也就是用 AS 取的欄位別名)，value 為該欄位的值
     * 取出 Map 每一個欄位的值是 Object 型別，要再轉型成要接收屬性的型別
     */
    @Query(value =
            "SELECT TOP 5 " +
            " r.restaurant_id AS restaurantId, r.name AS restaurantName, SUM(mo.sub_total) AS sales, COUNT(mo.order_id) AS orderCount" +
            " FROM menu_order mo " +
            " JOIN restaurant r ON mo.restaurant_id = r.restaurant_id " +
            " WHERE mo.status != 'CANCELED' " +
            " AND mo.created_time BETWEEN ?1 AND ?2 " +
            " GROUP BY r.restaurant_id, r.name " +
            " ORDER BY sales DESC, orderCount DESC ", nativeQuery = true)
    List<Map<String,Object>> findTop5RestaurantByMenuOrderSales(String startTime, String endTime);

    /**
     * 在時間區間內，根據訂單數量排序，查詢訂單總數量最高的5家餐廳
     * @param startTime，區間開始時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @param endTime，區間結束時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @return {@link List}<{@link Map}<{@link String},{@link Object}>>
     * 用 List<Map<String,Object>> 接收查詢結果，List 中的每一個元素代表查詢的每一列資料
     * Map 中的 key 為查詢結果集的欄位名稱 (也就是用 AS 取的欄位別名)，value 為該欄位的值
     * 取出 Map 每一個欄位的值是 Object 型別，要再轉型成要接收屬性的型別
     */
    @Query(value =
            "SELECT TOP 5 " +
            " r.restaurant_id AS restaurantId, r.name AS restaurantName, SUM(mo.sub_total) AS sales, COUNT(mo.order_id) AS orderCount " +
            " FROM menu_order mo " +
            " JOIN restaurant r ON mo.restaurant_id = r.restaurant_id " +
            " WHERE mo.status != 'CANCELED' " +
            " AND mo.created_time BETWEEN ?1 AND ?2 " +
            " GROUP BY r.restaurant_id, r.name " +
            " ORDER BY orderCount DESC, sales DESC ", nativeQuery = true)
    List<Map<String,Object>> findTop5RestaurantByMenuOrderCount(String startTime, String endTime);

    /**
     * 查詢時間區間內的餐點訂單，根據餐點的銷售額排序，該餐廳 Top5 銷售額餐點
     * @param restaurantId，餐廳ID
     * @param startTime，區間開始時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @param endTime，區間結束時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @return {@link List}<{@link Map}<{@link String},{@link Object}>>
     * 用 List<Map<String,Object>> 接收查詢結果，List 中的每一個元素代表查詢的每一列資料
     * Map 中的 key 為查詢結果集的欄位名稱 (也就是用 AS 取的欄位別名)，value 為該欄位的值
     * 取出 Map 每一個欄位的值是 Object 型別，要再轉型成要接收屬性的型別
     */
    @Query(value =
            "SELECT TOP 5 " +
            " m.menu_id AS menuId, m.name AS menuName, SUM(mod.price * mod.quantity) AS sales, SUM(mod.quantity) AS quantity " +
            " FROM menu_order mo " +
            " JOIN menu_order_detail mod ON mo.order_id = mod.order_id " +
            " JOIN menu m ON mod.menu_id = m.menu_id " +
            " WHERE mo.status != 'CANCELED' " +
            " AND mo.restaurant_id = ?1 " +
            " AND mo.created_time BETWEEN ?2 AND ?3 " +
            " GROUP BY m.menu_id, m.name " +
            " ORDER BY sales DESC, quantity DESC ", nativeQuery = true)
    List<Map<String,Object>> findTop5MenuBySales(Integer restaurantId, String startTime, String endTime);

    /**
     * 查詢時間區間內的餐點訂單，根據餐點的銷售量排序，該餐廳 Top5 銷售量餐點
     * @param restaurantId，餐廳ID
     * @param startTime，區間開始時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @param endTime，區間結束時間，字串格式 yyyy-MM-dd HH:mm:ss
     * @return {@link List}<{@link Map}<{@link String},{@link Object}>>
     * 用 List<Map<String,Object>> 接收查詢結果，List 中的每一個元素代表查詢的每一列資料
     * Map 中的 key 為查詢結果集的欄位名稱 (也就是用 AS 取的欄位別名)，value 為該欄位的值
     * 取出 Map 每一個欄位的值是 Object 型別，要再轉型成要接收屬性的型別
     */
    @Query(value =
            "SELECT TOP 5 " +
            " m.menu_id AS menuId, m.name AS menuName, SUM(mod.price * mod.quantity) AS sales, SUM(mod.quantity) AS quantity " +
            " FROM menu_order mo " +
            " JOIN menu_order_detail mod ON mo.order_id = mod.order_id " +
            " JOIN menu m ON mod.menu_id = m.menu_id " +
            " WHERE mo.status != 'CANCELED' " +
            " AND mo.restaurant_id = ?1 " +
            " AND mo.created_time BETWEEN ?2 AND ?3 " +
            " GROUP BY m.menu_id, m.name " +
            " ORDER BY quantity DESC, sales DESC ", nativeQuery = true)
    List<Map<String,Object>> findTop5MenuByQuantity(Integer restaurantId, String startTime, String endTime);

}