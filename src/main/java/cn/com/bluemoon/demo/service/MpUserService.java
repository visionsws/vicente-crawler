package cn.com.bluemoon.demo.service;

import cn.com.bluemoon.demo.entity.MpUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author weisen
 * @since 2019-10-09
 */
public interface MpUserService extends IService<MpUser> {

    IPage<MpUser> selectUserPage(Page<MpUser> page, Integer state);

}
