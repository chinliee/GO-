package com.team4.demo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// 使用 @Configuration + @Bean
// 在 Spring Container 容器中，創建 RestTemplate bean
// 後續其他類別需要使用 RestTemplate bean，就搭配 @Autowired 注入使用

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate initRestTemplate() {
        // 以下都可以產生 RestTemplate 物件回傳
        return new RestTemplateBuilder().build();
//        return new RestTemplate();
    }

}
