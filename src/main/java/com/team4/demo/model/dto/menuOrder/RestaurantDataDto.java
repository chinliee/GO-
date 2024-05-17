package com.team4.demo.model.dto.menuOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDataDto {

    private Integer restaurantId;

    private String name;

    // 餐廳地址
    private String address;

    // 餐廳經度
    private BigDecimal longitude;

    // 餐廳緯度
    private BigDecimal latitude;

    @JsonIgnore
    private byte[] photo;

    private String photoSrc;

    public void setPhoto(byte[] photo) {
        // photo 屬性，是圖片的 byte[]，當呼叫 setter 設定圖片 byte[]時，如果圖片資料不是 null
        // 下面同時再把 byte[]，轉成 Base64 編碼的字串表示(還有串接需要的字串)，並存到 photoSr 屬性傳送到前端，目的是讓前端 <img> 的 src 屬性顯示圖片用
        this.photo = photo;

        if (photo == null) {
            this.photoSrc = null;
            return;
        }

        try {
            // 把圖片的 byte[] -> Base64 編碼的字串
            String base64String = Base64.getEncoder().encodeToString(photo);

            // 下面兩行，透過圖片的 byte[] 資料，辨別圖片檔案的 mime type
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(photo);
            String mimeType = URLConnection.guessContentTypeFromStream(byteArrayInputStream);

            // 把需要的字串+mime type+Base64編碼字串，進行串接，存到 photoSrc 屬性，讓前端 <img> 的 src 屬性可以直接使用
            String photoBase64 = "data:%s;base64,".formatted(mimeType) + base64String;
            this.photoSrc = photoBase64;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
