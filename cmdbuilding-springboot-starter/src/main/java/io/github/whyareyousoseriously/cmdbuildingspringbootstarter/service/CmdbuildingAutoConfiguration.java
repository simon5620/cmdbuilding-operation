package io.github.whyareyousoseriously.cmdbuildingspringbootstarter.service;

import io.github.whyareyousoseriously.cmdbuildingspringbootstarter.config.CmdbConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author chenzhen
 * Created by chenzhen on 2019/5/24.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CmdbConfig.class)
@ConditionalOnClass(CmdbuildingTableOperation.class)
@ConditionalOnProperty(
        prefix = "cmdb",
        value = "enabled",
        matchIfMissing = true
)
public class CmdbuildingAutoConfiguration {

    @Resource
    private CmdbConfig cmdbConfig;

    @Bean
    @ConditionalOnMissingBean(CmdbuildingTableOperation.class)
    public CmdbuildingTableOperation getCmdbTableClient(){
        return new CmdbuildingTableOperation(cmdbConfig);
    }
}
