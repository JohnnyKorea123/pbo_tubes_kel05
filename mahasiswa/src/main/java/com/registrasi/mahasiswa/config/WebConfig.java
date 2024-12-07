package com.registrasi.mahasiswa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.registrasi.mahasiswa.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register", "/error", "/css/**", "/js/**", "/images/**");
    }
    @Override 
    public void addResourceHandlers(ResourceHandlerRegistry registry) { 
        registry.addResourceHandler("/uploads/profile-pictures/**") .addResourceLocations("file:uploads/profile-pictures/"); 
    }
}
