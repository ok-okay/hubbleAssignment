package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class AppConfig {	
    @Bean
    public ModelAndView modelAndViewBean() {
        return new ModelAndView();
    }
}
