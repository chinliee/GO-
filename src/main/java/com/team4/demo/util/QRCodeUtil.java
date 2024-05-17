package com.team4.demo.util;


import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.ByteArrayOutputStream;

public class QRCodeUtil {

    public static byte[] generateQRCode(String content) {
        ByteArrayOutputStream out = QRCode.from(content)
                .withCharset("UTF-8")
                .withSize(300, 300)
                .withErrorCorrection(ErrorCorrectionLevel.H)
                .to(ImageType.PNG)
                .stream();

        return out.toByteArray();
    }

    public static byte[] generateQRCode(String content, int width, int height) {
        ByteArrayOutputStream out = QRCode.from(content)
                .withCharset("UTF-8")
                .withSize(width, height)
                .withErrorCorrection(ErrorCorrectionLevel.H)
                .to(ImageType.PNG)
                .stream();

        return out.toByteArray();
    }

}
