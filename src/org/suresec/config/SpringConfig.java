package org.suresec.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * 
 * @author wcc
 * @time 2019-07-04 05:16
 * @description 配置扫描路径，初始化bean
 */
@Configuration
@ComponentScan("org.suresec.controller")
public class SpringConfig {

}
