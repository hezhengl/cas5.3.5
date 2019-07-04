package org.suresec.config;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlan;
import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.AuthenticationHandler;
import org.apereo.cas.authentication.principal.DefaultPrincipalFactory;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.suresec.handler.UsernamePasswordCaptchaAuthenticationHandler;


/**
 * 
 * @author wcc
 * @time 2019-07-04 05:17
 * @description 注册验证器(未使用--目前依然采用默认的jdbc登录验证)
 */
@Configuration("usernamePasswordCaptchaHandlerConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class UsernamePasswordCaptchaHandlerConfiguration implements AuthenticationEventExecutionPlanConfigurer {


    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    /**
     * 将自定义验证器注册为Bean
     * @return
     */
    @Bean
    public AuthenticationHandler UsernamePasswordCaptchaAuthenticationHandler() {
        UsernamePasswordCaptchaAuthenticationHandler handler = new UsernamePasswordCaptchaAuthenticationHandler(UsernamePasswordCaptchaAuthenticationHandler.class.getSimpleName(), servicesManager, new DefaultPrincipalFactory(), 1);
        return handler;
    }

    /**
     * 注册验证器
     * @param plan
     */
    @Override
    public void configureAuthenticationExecutionPlan(AuthenticationEventExecutionPlan plan) {
        plan.registerAuthenticationHandler(UsernamePasswordCaptchaAuthenticationHandler());
    }
}