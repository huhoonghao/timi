package com.timi.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sun.tools.javac.jvm.Code;
import com.timi.common.annotation.Phone;
import com.timi.common.cache.CacheHelper;
import com.timi.common.cache.RedisKeyEnum;
import com.timi.common.code.BusinessResponse;
import com.timi.common.code.RespCommonCode;
import com.timi.common.constant.user.Enabled;
import com.timi.common.exception.BusinessExceptionBuilder;
import com.timi.common.sms.SendMessage;
import com.timi.common.util.MD5Util;
import com.timi.common.util.TimiAssert;
import com.timi.common.util.ValidatorUtils;
import com.timi.modules.user.controller.param.UserPasswordParam;
import com.timi.modules.user.controller.param.UserSignInParam;
import com.timi.modules.user.dao.UserMapper;
import com.timi.modules.user.entity.UserEntity;
import com.timi.modules.user.holder.UserContentHolder;
import com.timi.modules.user.security.utils.SecurityUtils;
import com.timi.modules.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author hhh
 * @date 2021/1/25
 */
@Service
@Slf4j
public class UserSerivceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheHelper cacheHelper;
    @Override
    public BaseMapper<UserEntity> getMapper() {
        return userMapper;
    }
    @Override
    public UserEntity getUser(){
        UserEntity user = userMapper.selectById(1042);
        return user;
    }

    @Override
    public UserEntity findByUsername(String username) throws UsernameNotFoundException{
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username);
        UserEntity userEntity = userMapper.selectOne(queryWrapper);
        if (userEntity==null){
            throw new UsernameNotFoundException(username);
        }
        return userEntity;
    }

    @Override
    public Boolean updatePassword(UserPasswordParam requestParam) {
        //参数校验
        if (!requestParam.getConfirmPassword().equals(requestParam.getPassword())) {
            //密码和确认密码不一致
            throw BusinessExceptionBuilder.build(BusinessResponse.PASSWORDS_ARE_DIFFERENT);
        }
        //新密码不能与老密码一样
        if (StringUtils.equals(requestParam.getOldPassword(), requestParam.getPassword())) {
            throw BusinessExceptionBuilder.build(BusinessResponse.USER_PASSWORD_CONFIG_PASSWORD_CANT_EQUAL);
        }

        //根据账号查询出用户
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(UserEntity::getId, UserEntity::getPassword)
                .eq(UserEntity::getUsername, requestParam.getUsername());
        UserEntity user = userMapper.selectOne(queryWrapper);
        TimiAssert.isTrue(user==null,BusinessResponse.OLD_PASSWORD_ERROR);

        //比较用户输入的密码是不是正确的-
        if (SecurityUtils.matches(user.getPassword(), SecurityUtils.getPassword(requestParam.getPassword()))) {
            throw BusinessExceptionBuilder.build(BusinessResponse.OLD_PASSWORD_ERROR);
        }
        //新密码加盐
        String md5Pwd = MD5Util.encryptBySalt(requestParam.getPassword());
        //新密码加密
        String newPassword = SecurityUtils.getPassword(md5Pwd);
        user.setPassword(newPassword);
        int flag = userMapper.updateById(user);
        //返回结果
        return flag > 0;
    }
    @Transient
    @Override
    public UserEntity signIn(UserSignInParam reqParam) {
        validatorParam(reqParam);
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getPhone, reqParam.getPhone());
        UserEntity userEntity = userMapper.selectOne(queryWrapper);
        TimiAssert.isTrue(userEntity!=null,BusinessResponse.REGISTERED);
        //设置密码保存数据
        String md5Pwd = MD5Util.encryptBySalt(reqParam.getPassword());
        String pwd = SecurityUtils.getPassword(md5Pwd);
        reqParam.setPassword(pwd);
        UserEntity entity=new UserEntity();
        BeanUtils.copyProperties(reqParam, entity);
        entity.setAccountNonExpired(Enabled.YES.getValue());
        entity.setCredentialsNonExpired(Enabled.YES.getValue());
        entity.setAccountLocked(Enabled.NO.getValue());
        entity.setEnabled(Enabled.YES.getValue());
        // 验证码
        String redisCode = cacheHelper.stringGet(RedisKeyEnum.USER_SIG_NIN_CODE.getKey() + entity.getPhone());
        //然后和redis做比较
        TimiAssert.isTrue(Objects.equals(redisCode,reqParam.getSignInCode()),BusinessResponse.SIGN_IN_CODE);
        userMapper.insert(entity);
        return entity;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserEntity::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean signInSendCode(String phone) {
        TimiAssert.notEmpty(phone, BusinessResponse.PHONE_IS_NULL);
        try {
            String valCode = RandomStringUtils.randomNumeric(6);
            log.info("手机验证码{}开始发送-------->",valCode);
            SendMessage.sendSms(phone,valCode);
            log.info("验证码发送结束--------------->");
            cacheHelper.stringSetExpire(RedisKeyEnum.USER_SIG_NIN_CODE.getKey()+phone, valCode,5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void validatorParam(UserSignInParam reqParam){
        TimiAssert.notEmpty(reqParam.getPhone(), BusinessResponse.PHONE_IS_NULL);
        TimiAssert.isFalse(Objects.equals(reqParam.getPassword(),reqParam.getConfirmPassword()), BusinessResponse.PASSWORDS_ARE_DIFFERENT);
        TimiAssert.isFalse(ValidatorUtils.isMobile(reqParam.getPhone()), BusinessResponse.PHONE_IS_NULL);

    }
    private void notifyUser() {
        CompletableFuture.runAsync(() ->{
            try {
                log.info("开始发短信");
                SendMessage.doSend();
                log.info("发短信结束");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
