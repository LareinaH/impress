package com.conton.impress.config;


import com.conton.impress.web.interceptor.CheckLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FlmWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    CheckLoginInterceptor checkLoginInterceptor(){
    return new CheckLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(checkLoginInterceptor()).addPathPatterns("/**").
                excludePathPatterns("/user/login/**");
        super.addInterceptors(registry);
    }
}
