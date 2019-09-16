package cn.com.bluemoon.demo.common.snowflake;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shiweisen
 * @since 2018-08-13
 */

@Configuration
public class SnowflakeConfig {

    @Bean
    public SnowflakeIdWorker generateSnowflake( @Value("${snowflake.inst.workerId}")long workerId,
                                     @Value("${snowflake.inst.datacenterId}") long datacenterId) {
        return new SnowflakeIdWorker(workerId, datacenterId);
    }

}
