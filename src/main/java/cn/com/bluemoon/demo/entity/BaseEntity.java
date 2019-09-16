package cn.com.bluemoon.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass //表明这是父类，可以将属性映射到子类中使用JPA生成表
//自动添加创建时间和更新时间
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "create_time",columnDefinition="DATETIME COMMENT '创建时间'")
    private Date createTime;

    @Column(name = "create_by",columnDefinition="varchar(20) COMMENT '创建人'")
    private String createBy;

    @LastModifiedDate
    @Column(name = "update_time",columnDefinition="DATETIME COMMENT '最后更新时间'")
    private Date updateTime;

    @Column(name = "update_by",columnDefinition="varchar(20) COMMENT '修改人'")
    private String updateBy;

}

