package com.team4.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper initModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // 也可以對 ModelMapper 的設定進行其他調整

        return modelMapper;
    }

}
