package com.team4.demo.util;

public class PositionHelper {

    public static void main(String[] args) {
        // 高雄中學
        double firstLongitude = 120.29804571534322;
        double firstLatitude = 22.639091770300254;
        // 資展國際-高雄訓練中心
        double secondLongitude = 120.29286653862724;
        double secondLatitude = 22.62875965764371;
        double distance = getDistance(firstLongitude, firstLatitude, secondLongitude, secondLatitude);
        System.out.println("兩地距離: " + distance + " 公里");
    }

    /**
     * 地球半徑，單位：公里 km
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 經緯度轉化成弧度
     * @param d 經度/緯度
     * @return 弧度
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 計算兩個經緯度座標點，之間的直線距離 (最短路徑)，單位：公里 km
     * @param firstLongitude  第一個座標的經度
     * @param firstLatitude   第一個座標的緯度
     * @param secondLongitude 第二個座標的經度
     * @param secondLatitude  第二個座標的緯度
     * @return 回傳兩點之間的距離，單位：公里 km
     */
    public static double getDistance(double firstLongitude, double firstLatitude,
                                     double secondLongitude, double secondLatitude) {
        double firstRadLng = rad(firstLongitude);
        double firstRadLat = rad(firstLatitude);
        double secondRadLng = rad(secondLongitude);
        double secondRadLat = rad(secondLatitude);

        double a = firstRadLat - secondRadLat;
        double b = firstRadLng - secondRadLng;
        double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(firstRadLat) * Math.cos(secondRadLat) * Math.pow(Math.sin(b / 2), 2))) * EARTH_RADIUS;
        double result = Math.round(cal * 10000d) / 10000d;

        return result;
    }

}
