package com.team4.demo.model.dto.menuOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDataDto {

    private Integer orderId;

    // 訂單類型，外送 DELIVERY、外帶自取 TAKEOUT
    private String orderType;

    // 訂單狀態
    private String orderStatus;

    // 付款方式，現金 CASH、信用卡 CREDIT_CARD
    private String paymentType;

    // 預約時間的 Long 毫秒數
    private Long reservationTime;

    // 外送地址
    private String deliveryAddress;

    // 外送費
    private Integer deliveryFee;

    // 平台費
    private Integer platformFee;

    // 平台費
    private Integer subTotal;

    // 訂單總金額
    private Integer total;

    @JsonIgnore
    private byte[] qrcode;

    // Qr Code 的 base64 編碼字串，讓前端 <img> 的 src 屬性顯示圖片用
    private String qrcodeSrc;

    public void setQrcode(byte[] qrcode) {
        // 圖片的 byte[]，當呼叫 setter 設定圖片 byte[]時，如果圖片資料不是 null
        // 下面同時再把 byte[]，轉成 Base64 編碼的字串表示(還有串接需要的字串)，並存到 qrcodeSrc 屬性傳送到前端，目的是讓前端 <img> 的 src 屬性顯示圖片用
        this.qrcode = qrcode;

        if (qrcode == null) {
            this.qrcodeSrc = null;
            return;
        }

        try {
            // 把圖片的 byte[] -> Base64 編碼的字串
            String base64String = Base64.getEncoder().encodeToString(qrcode);

            // 下面兩行，透過圖片的 byte[] 資料，辨別圖片檔案的 mime type
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrcode);
            String mimeType = URLConnection.guessContentTypeFromStream(byteArrayInputStream);

            // 把需要的字串+mime type+Base64編碼字串，進行串接，存到 photoSrc 屬性，讓前端 <img> 的 src 屬性可以直接使用
            String qrcodeBase64 = "data:%s;base64,".formatted(mimeType) + base64String;
            this.qrcodeSrc = qrcodeBase64;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
