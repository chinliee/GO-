package com.team4.demo.config;

// @CrossOrigin，解決瀏覽器阻擋跨域請求錯誤(Corns Error)
// 假設我從 VSCode 的本地前端本地頁面 http://127.0.0.1:5500/XXX/test.html
// 要發請求給 Spring Boot 後端 API http://localhost:8080/products
// 兩者之間，只有任何一個URI項目不相同，瀏覽器就會為了安全性阻擋，例如：協定 http和https、domain name域名 127.0.0.1和localhost、埠號port number 5500和8080
// 前端網頁錯誤訊息：Access to XMLHttpRequest at 'http://localhost:8080/products' from origin 'http://127.0.0.1:5500' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
// 一般而言，發生阻擋跨域的錯誤，需要在後端程式設定，來放行讓前端可以收到 Response，不會被阻擋

// 所以這邊新增設定檔類別 CornsCig，並實作介面 WebMvcConfigurer 的 addCorsMappings 方法，來設定允許全局跨域的細項
// 還有其他方法也可以設定全局跨域，例如：新增攔截器CrosFilter、實作介面 ResponseBodyAdvice 中的 beforeBodyWrite 方法等


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許哪些 API 路徑，這樣寫是允許所有 API 路徑

                // Spring 2.4.0之後版本引入，allowedOriginPatterns()，使用通配符 * 来匹配多個 URL 路徑
                // allowedOrigins() 是之前的方法，用在字串匹配，只能指定具體的 URL 路徑，例如 allowedOrigins("http://127.0.0.1:5501", "https://www.test.com")
                .allowedOriginPatterns("*")

                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 允許的請求方法
                .allowedHeaders("*") // 允許哪些 Request Header 資訊
                .exposedHeaders("*") // 允許取得哪些 Request Header 資訊，原本預設跨網域不能取得全部 Header 資訊
                .allowCredentials(true); // 允許跨域攜帶 cookie 資訊，原本預設跨網域請求是不攜帶 cookie 資訊
    }

}
