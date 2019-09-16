package cn.com.bluemoon.demo.common.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * @author shiweisen
 * @since 2018-11-07
 */
@Slf4j
@Component
public class GenerateId implements IdentifierGenerator,Configurable {

    public SnowflakeIdWorker snowFlakeIdWorker;


    /**
     * hibernate自定义主键生成规则必须实现 IdentifierGenerator  generate 为默认方法
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        Long id = snowFlakeIdWorker.nextId();
        if (id != null) {
            return id;
        }else {
            return null;
        }
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        //加载配置文件
        InputStream is = GenerateId.class.getResourceAsStream("/static/snowflake.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("加载snowflake文件异常" + e.getMessage());
        }
        String workid = properties.getProperty("workid");
        String dataid = properties.getProperty("dataid");
        if (StringUtils.hasText(dataid) && StringUtils.hasText(workid)) {
            snowFlakeIdWorker = new SnowflakeIdWorker(Long.valueOf(workid), Long.valueOf(dataid));
        }
    }
}
