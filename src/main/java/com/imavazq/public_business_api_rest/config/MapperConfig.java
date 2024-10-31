package com.imavazq.public_business_api_rest.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //para indicar que es una clase que contiene beans
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}
