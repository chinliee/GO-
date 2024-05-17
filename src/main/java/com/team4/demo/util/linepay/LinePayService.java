package com.team4.demo.util.linepay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.demo.util.linepay.domain.CheckoutPaymentRequestForm;
import com.team4.demo.util.linepay.domain.ProductForm;
import com.team4.demo.util.linepay.domain.ProductPackageForm;
import com.team4.demo.util.linepay.domain.RedirectUrls;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
public class LinePayService {

    // Line Pay Request API URL
    private static final String LINE_PAY_REQUEST_API_URL = "https://sandbox-api-pay.line.me/v3/payments/request";

    // Sandbox 環境的 Channel ID
    private static final String LINE_PAY_CHANNEL_ID = "2003058338";

    // Sandbox 環境的 Channel Secret Key
    private static final String LINE_PAY_CHANNEL_SECRET_KEY = "7275d62d18f3be61381b49a2e95e36f9";

    // 不能直接透過 @Autowired 注入 Bean 使用，Spring Container 容器中預設沒有 RestTemplate Bean
    // 所以有在 RestTemplateConfig，創建出 Bean 供其他類別注入使用
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 發送請求給 Line Pay Request API，取得 Line Pay 網頁付款連結
     * @param orderId，訂單編號
     * @param orderTotal，訂單總金額
     * @param confirmUrl，使用者授權付款後，跳轉到商家 URL
     * @param cancelUrl，使用者通過 Line Pay 付款頁，取消付款後跳轉到該 URL，如果不設定就傳入空字串 ""
     * @param productFormName，Line Pay 網頁付款頁面上，顯示的表單名稱
     * @return {@link String} Line Pay 網頁付款連結
     */
    public String getWebPaymentUrl(Integer orderId, Integer orderTotal,
                                   String confirmUrl, String cancelUrl, String productFormName) {
        log.info("LinePayService - getPaymentUrl ... orderId => {}, orderTotal => {}, confirmUrl => {}, cancelUrl => {}, productFormName => {}", orderId, orderTotal, confirmUrl, cancelUrl, productFormName);

        String webPaymentUrl = null;

        // 組合 Line Pay Request API 所需的參數
        CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
        form.setAmount(new BigDecimal(orderTotal));
        form.setCurrency("TWD");
        form.setOrderId(orderId.toString());

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id");
        productPackageForm.setName("shop_name");
        productPackageForm.setAmount(new BigDecimal(orderTotal));

        ProductForm productForm = new ProductForm();
        productForm.setId("product_id");
        productForm.setName(productFormName);
        productForm.setImageUrl("");
        productForm.setPrice(new BigDecimal(orderTotal));
        productForm.setQuantity(new BigDecimal(1));

        productPackageForm.setProducts(Arrays.asList(productForm));
        form.setPackages(Arrays.asList(productPackageForm));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setConfirmUrl(confirmUrl);
        redirectUrls.setCancelUrl(cancelUrl);
        form.setRedirectUrls(redirectUrls);

        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();

        try {

            // 把 CheckoutPaymentRequestForm 物件，轉換成 JSON 字串，就是 POSTMAN request body 中的 JSON 參數
            String requestBodyJson = objectMapper.writeValueAsString(form);
            String signature = encrypt(LINE_PAY_CHANNEL_SECRET_KEY, LINE_PAY_CHANNEL_SECRET_KEY + requestUri + requestBodyJson + nonce);

            log.info("LinePayService - getPaymentUrl ... Request Header, nonce => {}, signature => {}", nonce, signature);

            // 從 Spring Boot，使用 RestTemplate 發送 POST 請求給 Line Pay Request API
            // 設定 Line Pay Request API，所需要的 request header 驗證
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-LINE-ChannelId", LINE_PAY_CHANNEL_ID);
            headers.add("X-LINE-Authorization-Nonce", nonce);
            headers.add("X-LINE-Authorization", signature);

            // 創建 HttpEntity 物件，當作參數傳入 exchange 方法中
            // RestTemplate，當 POST、PUT...請求，request body 有 JSON 字串當作參數發送請求時
            // 會自動把 Java Object -> JSON 字串，不用自己轉換
            // 所以這邊第一個參數，代表 request body 中的 JSON 參數，直接放入 CheckoutPaymentRequestForm 物件就可以了
            HttpEntity<CheckoutPaymentRequestForm> requestEntity = new HttpEntity<>(form, headers);

            // 可以透過 ResponseEntity，進一步取得狀態碼... 其他資訊
            // ResponseEntity<> 的泛型型別，是 Response Body JSON 字串要轉換的 Java Bean 類別
            // 但這邊沒有額外設計 Java Bean 來對應 Response Body JSON 字串
            // 所以這邊使用 String.class，代表 Response Body JSON 字串，不需要轉換成 Java Bean
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    LINE_PAY_REQUEST_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("LinePayService - getPaymentUrl ... Response Body => {}", responseEntity.getBody());

            // ObjectMapper readTree 方法，把 Response Body 的 JSON 字串，解析成 JsonNode 物件
            // 再使用 JsonNode 物件的 get 方法，會回傳 JsonNode 物件，再呼叫對應資料型別取值的方法，例如：asText()、asInt()...
            // 如果原始 JSON 中的屬性是嵌套物件、嵌套陣列，就使用多個 get("XXX") 方法，每個 get 方法都是回傳 JsonNode 物件，直到取得想要的屬性值
            // -> 使用 JsonNode 時機是，如果不想要創建一個對應 Response Body 的 Java Bean、或只需要其中幾個屬性值而已
            // 就可以直接使用 JsonNode 物件，取得原始 JSON 字串中的屬性值
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());

            // 取得 Line Pay 網頁付款連結
            // Response Body JSON 字串中，info 物件中的 paymentUrl 物件，paymentUrl 物件中的屬性 web，就是網頁付款連結
            webPaymentUrl = jsonNode.get("info").get("paymentUrl").get("web").asText();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("LinePayService - getPaymentUrl ... webPaymentUrl => {}", webPaymentUrl);
        return webPaymentUrl;
    }

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }

    // 測試使用
    public static void main(String[] args) {
        CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();

        form.setAmount(new BigDecimal("100"));
        form.setCurrency("TWD");
        form.setOrderId("merchant_order_id");

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id");
        productPackageForm.setName("shop_name");
        productPackageForm.setAmount(new BigDecimal("100"));

        ProductForm productForm = new ProductForm();
        productForm.setId("product_id");
        productForm.setName("餐點訂單");
        productForm.setImageUrl("");
        productForm.setPrice(new BigDecimal("10"));
        productForm.setQuantity(new BigDecimal("10"));

        productPackageForm.setProducts(Arrays.asList(productForm));
        form.setPackages(Arrays.asList(productPackageForm));

        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setAppPackageName("");
        redirectUrls.setConfirmUrl("https://tw.yahoo.com/?p=us");
        redirectUrls.setCancelUrl("");
        form.setRedirectUrls(redirectUrls);

        String httpUrl = "https://sandbox-api-pay.line.me/v3/payments/request";
        String ChannelSecret = "7275d62d18f3be61381b49a2e95e36f9";
        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = null;
        try {
            requestBodyJson = objectMapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Line Pay Request API，消費者付款後，會導回商家設定的 confirm url
        String signature = encrypt(ChannelSecret, ChannelSecret + requestUri + requestBodyJson + nonce);
        System.out.println("ChannelSecret:" + ChannelSecret);
        System.out.println("requestUri:" + requestUri);
        System.out.println("nonce:" + nonce);
        System.out.println("requestBodyJson:" + requestBodyJson);
        System.out.println("signature:" + signature);
    }

}
