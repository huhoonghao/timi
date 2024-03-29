package com.timi.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.timi.modules.user.holder.UserContentHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动补充插入或更新时的值
 */
@Component
public class MybatisPlusMetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String username = UserContentHolder.getContext().getUsername();
        Object createBy = this.getFieldValByName("createBy", metaObject);
        if (createBy == null) {
            this.setFieldValByName("createBy", username,metaObject);
        }

        this.setFieldValByName("createTime", new Date(),metaObject);

        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        if (updateBy == null) {
            this.setFieldValByName("updateBy", username,metaObject);
        }
        this.setFieldValByName("updateTime", new Date(),metaObject);
        this.setFieldValByName("version", Integer.valueOf(1),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String username = UserContentHolder.getContext().getUsername();
        this.setFieldValByName("updateBy", username,metaObject);
        this.setFieldValByName("updateTime", new Date(),metaObject);
        this.setFieldValByName("version",
                this.getFieldValByName("version",metaObject),metaObject);
    }
}
