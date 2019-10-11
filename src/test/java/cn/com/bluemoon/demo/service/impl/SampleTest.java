package cn.com.bluemoon.demo.service.impl;

import cn.com.bluemoon.demo.controller.DemoController;
import cn.com.bluemoon.demo.entity.AurRole;
import cn.com.bluemoon.demo.entity.MpUser;
import cn.com.bluemoon.demo.mapper.AurRoleMapper;
import cn.com.bluemoon.demo.mapper.MpUserMapper;
import cn.com.bluemoon.demo.service.MpUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    private static Logger log = LoggerFactory.getLogger(SampleTest.class);

    @Autowired
    private MpUserMapper userMapper;

    @Autowired
    private AurRoleMapper aurRoleMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<MpUser> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testRole() {
        System.out.println(("----- selectAll method test ------"));
        List<AurRole> userList = aurRoleMapper.selectList(null);
        //Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Autowired
    private MpUserService mpUserService;

    @Test
    public void test1() {
        // 插入新记录
        MpUser mpUser = new MpUser();
        //mpUser.setId(1L);
        mpUser.setEmail("wm@baomidou.com");
        mpUser.setAge(28);
        mpUser.setName("王蒙");
        mpUserService.save(mpUser);
        // 或者
        //mpUser.insertOrUpdate();
        // 更新完成后，mpUser对象的id会被补全
        log.info("mpUser={}", mpUser.toString());

    }

    @Test
    public void test2() {
        // 通过主键id查询
        MpUser mpUser = mpUserService.getById(1);
        log.info("mpUser={}", mpUser.toString());
    }

    @Test
    public void test3() {
        // 条件查询，下面相当于xml中的 select * from mp_user where address = '"广东深圳' and username = 'David Hong' limit 1
        MpUser mpUser = mpUserService.getOne(new QueryWrapper<MpUser>().eq("name", "Tom").eq("age", "28").last("limit 1"));
        log.info("mpUser={}", mpUser.toString());
        // 批量查询
        List<MpUser> mpUserList = mpUserService.list();
        System.out.println("------------------------------all");
        mpUserList.forEach(System.out::println);
        // 分页查询
        int pageNum = 1;
        int pageSize = 10;
        IPage<MpUser> mpUserIPage = mpUserService.page(new Page<>(pageNum, pageSize), new QueryWrapper<MpUser>().gt("age", "20"));
        // IPage to List
        List<MpUser> mpUserList1 = mpUserIPage.getRecords();
        System.out.println("------------------------------page");
        mpUserList1.forEach(System.out::println);
        // 总页数
        long allPageNum = mpUserIPage.getPages();
        System.out.println("------------------------------allPageNum");
        System.out.println(allPageNum);
    }

    @Test
    public void test4() {
        MpUser mpUser = mpUserService.getById(2);
        // 修改更新
        mpUser.setName("广东广州");
        //mpUserService.updateById(mpUser);
        // 或者
        mpUser.insertOrUpdate();
        // 通过主键id删除
        mpUserService.removeById(1);
        // 或者
        //mpUser.deleteById();
    }

    @Test
    public void test5() {
        Page<MpUser> mpUserPage = new Page<>(1,2);
        IPage<MpUser> iPage = mpUserService.selectUserPage(mpUserPage,22);
        System.out.println("总页数："+iPage.getPages());
        System.out.println("总记录数："+iPage.getTotal());
        List<MpUser> mpUserList1 = iPage.getRecords();
        mpUserList1.forEach(System.out::println);
    }


}
