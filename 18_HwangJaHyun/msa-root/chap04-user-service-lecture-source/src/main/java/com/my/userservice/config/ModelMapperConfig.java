package com.my.userservice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * ModelMapper 설정 클래스
 * DTO와 Entity 간의 매핑을 담당하는 ModelMapper 빈을 등록함
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 매핑 전략 설정
        // STRICT: 필드명이 정확히 일치해야 매핑됨 (실수 방지)
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
        // private 필드에도 접근 가능하도록 설정
        .setFieldAccessLevel(
                org.modelmapper.config.Configuration.AccessLevel.PRIVATE
        )
        .setFieldMatchingEnabled(true);
        return modelMapper;
    }
}
