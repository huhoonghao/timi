package com.timi.common.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timi.common.annotation.Query;
import com.timi.common.constant.TimiConstant;
import com.timi.common.mybatisPlus.BaseEntity;
import com.timi.common.mybatisPlus.PageParam;
import com.timi.common.util.TimiUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author hhh
 * @date 2021/5/20
 */
public interface BaseService <P extends PageParam, T extends BaseEntity>{
    /**
     * 获取直接操作数据库接口
     *
     * @return
     */
    BaseMapper<T> getMapper();

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    default IPage<T> page(P pageParam) {
        return page(pageParam, null);
    }

    /**
     * 分页，指定查询条件即忽略扩展的条件
     *
     * @param pageParam
     * @param wrapper   指定参数
     * @return
     */
    default IPage<T> page(P pageParam, Wrapper<T> wrapper) {
        Page<T> page = new Page<>();
        page.setCurrent(pageParam.getPageNumber());
        page.setSize(pageParam.getPageSize());

        //设置排序
        if (StringUtils.equals(TimiConstant.ASC, pageParam.getOrder()) && StringUtils.isNotBlank(pageParam.getOrder())) {
            page.addOrder(OrderItem.asc(pageParam.getSort()));
        } else if (StringUtils.equals(TimiConstant.DESC, pageParam.getOrder()) && StringUtils.isNotBlank(pageParam.getOrder())) {
            page.addOrder(OrderItem.desc(pageParam.getSort()));
        }
        //当有自定义条件时，去掉参数组成的查询条件
        if (wrapper != null) {
            return resultHandler(getMapper().selectPage(page, wrapper));
        }

        return resultHandler(getMapper().selectPage(page, extensionWrapper(pageParam, getWrapper(pageParam))));
    }


    /**
     * 排序结果处理(作用：需要对排序结果进行处理时，使用)
     *
     * @param iPage
     * @return
     */
    default IPage<T> resultHandler(IPage<T> iPage) {
        return iPage;
    }
    /**
     * 扩展查询条件
     *
     * @param param   查询参数
     * @param wrapper 基本查询条件
     * @return
     */
    default Wrapper<T> extensionWrapper(P param, QueryWrapper<T> wrapper) {
        return wrapper;
    }
    /**
     * 抽象查询条件
     *
     * @param param 查询条件
     * @return
     */
    default QueryWrapper<T> getWrapper(P param) {

        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //条件的值
        Field[] fields = param.getClass().getDeclaredFields();

        Arrays.stream(fields).filter(field -> {
            if (field.isAnnotationPresent(Query.class)) {
                Query query = field.getAnnotation(Query.class);
                return query.where();
            }
            return true;
        }).forEach(field -> {
            try {
                boolean flag;
                field.setAccessible(true);
                //列名
                String column;
                if (field.isAnnotationPresent(Query.class) && StringUtils.isNotBlank(field.getAnnotation(Query.class).column())) {
                    column = field.getAnnotation(Query.class).column();
                } else {
                    column = TimiUtils.camelToUnderline(field.getName());
                }

                if (field.get(param) instanceof String) {
                    flag = StringUtils.isNoneBlank((String) field.get(param));
                } else {
                    flag = field.get(param) != null;
                }
                if (!flag) {
                    return;
                }

                //判断是否是模糊查询
                if (field.isAnnotationPresent(Query.class)) {
                    switch (field.getAnnotation(Query.class).value()) {
                        case LIKE:
                            queryWrapper.like(column, field.get(param));
                            break;
                        case IN:
                            Object value = field.get(param);
                            if (value instanceof List) {
                                queryWrapper.in(column, (List)value);
                            } else if (value instanceof String) {
                                String[] split = ((String) value).split(TimiConstant.SPLIT);
                                List<String> list = Arrays.asList(split);
                                queryWrapper.in(column, list);
                            }
                            break;
                        case GT:
                            queryWrapper.gt(column, field.get(param));
                            break;
                        case LT:
                            queryWrapper.lt(column, field.get(param));
                            break;
                        case BWT:
                            String[] split = field.get(param).toString().split(TimiConstant.SPLIT);
                            if (split.length == 2) {
                                queryWrapper.between(column, split[0], split[1]);
                            }
                            break;
                        default:
                            queryWrapper.eq(column, field.get(param));
                    }

                } else {
                    queryWrapper.eq(column, field.get(param));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return queryWrapper;
    }

}
