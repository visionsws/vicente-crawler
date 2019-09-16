package cn.com.bluemoon.demo.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * 参考：https://www.cnblogs.com/lori/p/9400510.html
 */
public class DtoToBeanUtil {

    //两个实体属性字段几乎完全相同
    public static void copyProperties(Object source, Object target){
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 两个字体有部分字段相同
     * @param source
     * @param target
     */
    public static void dtoPart2Bean(Object source, Object target){
         /*
         通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
         Include.Include.ALWAYS 默认
         Include.NON_DEFAULT 属性为默认值不序列化
         Include.NON_EMPTY 属性为 空（“”）  或者为 NULL 都不序列化
         Include.NON_NULL 属性为NULL 不序列化
         */
        ObjectMapper objectMapper = new ObjectMapper();
        //配置该objectMapper在反序列化时，忽略目标对象没有的属性。凡是使用该objectMapper反序列化时，都会拥有该特性。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //读入需要更新的目标实体
        ObjectReader objectReader = objectMapper.readerForUpdating(target);
        //将源实体的值赋值到目标实体上
        try {
            objectReader.readValue(JSON.toJSONString(source));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 源实体只有部分字段赋值，目标实体有完整的值
     * 本情况主要对于从dto到entity转换过程中出现 ，比如一个put操作，前端可能只修改某几个属性，而在后端处理时也只希望处理这几个被赋值的属性，这时我们使用下面的方法:
     * @param source
     * @param target
     */
    public static void dto2Bean(Object source, Object target){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        String outJson = null;
        try {
            outJson = objectMapper.writeValueAsString(source);
            //上面代码里，outJson的值将会过滤掉只有默认值的属性
            ObjectReader objectReader = objectMapper.readerForUpdating(target);
            objectReader.readValue(outJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
