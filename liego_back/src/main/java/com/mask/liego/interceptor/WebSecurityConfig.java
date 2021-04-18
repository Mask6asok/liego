package com.mask.liego.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    @Bean
    public SessionInterceptor getSessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] paths = new String[]{"/LoginUser", "/RegisterUser", "/ResetUser", "/ViewGoodsInfo",
                "/ViewGoodsInfoW", "/ViewGoodsInfosW", "/ViewDescription", "/ViewMsg", "/ViewMsgs",
                "/ViewRpl", "/ViewRpls", "/ViewUserE", "/ViewGoodsInfosByUid"};
        SessionInterceptor sessionInterceptor = new SessionInterceptor();
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**")
                .excludePathPatterns(paths);
        super.addInterceptors(registry);
    }
}
