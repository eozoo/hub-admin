package com.cowave.hub.admin.infra.sys2.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys2.entity.SysAttach;
import com.cowave.hub.admin.domain.sys2.repository.SysAttachRepository;
import com.cowave.hub.admin.infra.sys2.mapper.SysAttach2Mapper;
import com.cowave.zoo.framework.access.Access;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysAttach2Dao extends ServiceImpl<SysAttach2Mapper, SysAttach> implements SysAttachRepository {
    private static final String USER_MODULE = "module_user";
    private static final String AVATAR_TYPE = "avatar";

    @Override
    public SysAttach queryLatestUserAvatar(String userId) {
        return lambdaQuery()
                .eq(SysAttach::getOwnerModule, USER_MODULE)
                .eq(SysAttach::getOwnerId, userId)
                .eq(SysAttach::getAttachType, AVATAR_TYPE)
                .eq(SysAttach::getIsDeleted, 0)
                .orderByDesc(SysAttach::getCreateTime, SysAttach::getAttachId)
                .last("LIMIT 1")
                .one();
    }

    @Transactional
    @Override
    public void reserveUserAvatars(String userId, int reserve) {
        List<SysAttach> avatars = lambdaQuery()
                .eq(SysAttach::getOwnerModule, USER_MODULE)
                .eq(SysAttach::getOwnerId, userId)
                .eq(SysAttach::getAttachType, AVATAR_TYPE)
                .eq(SysAttach::getIsDeleted, 0)
                .orderByDesc(SysAttach::getCreateTime, SysAttach::getAttachId)
                .list();
        for (int i = Math.max(reserve, 0); i < avatars.size(); i++) {
            lambdaUpdate()
                    .eq(SysAttach::getAttachId, avatars.get(i).getAttachId())
                    .eq(SysAttach::getIsDeleted, 0)
                    .set(SysAttach::getIsDeleted, 1)
                    .set(SysAttach::getUpdateBy, Access.userAccount())
                    .set(SysAttach::getUpdateTime, new Date())
                    .update();
        }
    }
}
