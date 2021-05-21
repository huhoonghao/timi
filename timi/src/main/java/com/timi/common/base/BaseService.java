package com.timi.common.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.timi.common.annotation.*;
import com.timi.common.bean.HashKeyValue;
import com.timi.common.cache.BaseOperationEnum;
import com.timi.common.cache.CacheHelper;
import com.timi.common.code.ResponseCode;
import com.timi.common.constant.TimiConstant;
import com.timi.common.exception.BusinessException;
import com.timi.common.exception.BusinessExceptionBuilder;
import com.timi.common.util.ApplicationContextUtils;
import com.timi.common.util.TimiAssert;
import com.timi.common.util.TimiUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.timi.common.code.ResponseCode.INSERT_FAILURE;
import static com.timi.common.code.ResponseCode.UPDATE_FAILURE;

/**
 * @author hhh
 * @date 2021/5/20
 */
public interface BaseService<P extends PageParam, T extends BaseEntity> {
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
                                queryWrapper.in(column, (List) value);
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


    /**
     * 根据id查询记录
     *
     * @param id
     * @return
     */
    default T selectOne(Long id) {
        T t = getMapper().selectById(id);
        TimiAssert.notNull(t, ResponseCode.RECORD_NO_EXIST);
        return wrapperEntity(t);
    }

    /**
     * 包装返回实体
     *
     * @param entity
     * @return
     */
    default T wrapperEntity(T entity) {
        return entity;
    }


    /**
     * 保存数据
     *
     * @param entity
     * @return
     * @throws BusinessException 业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    default Integer insert(T entity) throws BusinessException {
        //保存前处理
        processBeforeOperation(entity, BaseOperationEnum.INSERT);

        //校验唯一索引
        checkUniqueField(entity, false);
        //保存
        Integer result = getMapper().insert(entity);

        //保存失败
        if (result == null || result < 1) {
            throw BusinessExceptionBuilder.build(INSERT_FAILURE);
        }

        //保存缓存字段
        refreshCacheFields(entity, BaseOperationEnum.INSERT);
        //保存后处理
        processAfterOperation(entity, BaseOperationEnum.INSERT);

        return result;
    }

    /**
     * 更新数据
     *
     * @param entity
     * @return
     * @throws BusinessException 业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    default Integer update(T entity) throws BusinessException {
        //更新前处理
        processBeforeOperation(entity, BaseOperationEnum.UPDATE);
        //校验唯一索引
        checkUniqueField(entity, true);

        if (entity instanceof BaseEntity) {
            BaseEntity gaeaBaseEntity = (BaseEntity) entity;
            T dbEntity = getById(gaeaBaseEntity.getId());
            refreshCacheFields(dbEntity, BaseOperationEnum.DELETE);
        }

        //更新
        Integer result = getMapper().updateById(entity);

        //更新失败
        if (result == null || result < 1) {
            throw BusinessExceptionBuilder.build(UPDATE_FAILURE);
        }

        //保存缓存字段
        refreshCacheFields(entity, BaseOperationEnum.UPDATE);
        //更新后处理
        processAfterOperation(entity, BaseOperationEnum.UPDATE);
        return result;
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default Integer deleteById(Serializable id) {
        T t = getById(id);
        if (t == null) {
            throw BusinessExceptionBuilder.build(ResponseCode.RECORD_NO_EXIST);
        }
        //删除前处理
        processBeforeOperation(t, BaseOperationEnum.DELETE);
        Integer result = getMapper().deleteById(id);

        //删除失败
        if (result == null || result < 1) {
            throw BusinessExceptionBuilder.build(ResponseCode.DELETE_FAILURE);
        }
        //删除缓存字段 如果没有翻译需求值则不需要
        refreshCacheFields(t, BaseOperationEnum.DELETE);

        //删除后处理
        processAfterOperation(t, BaseOperationEnum.DELETE);
        return result;
    }


    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean deleteByIds(Collection<? extends Serializable> idList) {
        List<T> list = getMapper().selectBatchIds(idList);
        processBatchBeforeOperation(list, BaseOperationEnum.DELETE_BATCH);
        boolean result = SqlHelper.retBool(getMapper().deleteBatchIds(idList));
        if (result) {
            //刷新缓存
            //保存缓存
            list.stream().forEach(entity -> {
                //保存缓存字段
                refreshCacheFields(entity, BaseOperationEnum.DELETE_BATCH);
            });
            processBatchAfterOperation(list, BaseOperationEnum.DELETE_BATCH);
        }
        return result;
    }

    /**
     * ResponseBean
     * 根据ID查询记录
     *
     * @param id
     * @return
     */
    default T getById(Serializable id) {
        return getMapper().selectById(id);
    }

    /**
     * 批处理操作前处理
     *
     * @Title: processBatchBeforeOperation
     * @Description:
     * @Author hhh
     * @Date 2021/5/20 17:49
     * @Param [entities, operationEnum]操作类型,阻止程序继续执行或回滚事务
     * @Return void
     */
    default void processBatchBeforeOperation(List<T> entities, BaseOperationEnum operationEnum) throws BusinessException {
    }

    /**
     * 批处理操作后续处理
     *
     * @param entities
     * @param operationEnum 操作类型
     * @throws BusinessException 阻止程序继续执行或回滚事务
     */
    default void processBatchAfterOperation(List<T> entities, BaseOperationEnum operationEnum) throws BusinessException {
    }

    /**
     * 操作前处理
     *
     * @param entity        前端传递的对象
     * @param operationEnum 操作类型
     * @throws BusinessException 阻止程序继续执行或回滚事务
     */
    default void processBeforeOperation(T entity, BaseOperationEnum operationEnum) throws BusinessException {
    }

    /**
     * 操作后续处理
     *
     * @param entity
     * @param operationEnum 操作类型
     * @throws BusinessException 阻止程序继续执行或回滚事务
     */
    default void processAfterOperation(T entity, BaseOperationEnum operationEnum) throws BusinessException {
    }

    /**
     * 校验唯一
     *
     * @param entity   实体对象
     * @param isUpdate 是否是更新
     */
    default void checkUniqueField(T entity, boolean isUpdate) {
        //获取当前类的属性
        Field[] fields = entity.getClass().getDeclaredFields();

        //获取当前类父类的属性，先判断对应实体是否有id字段
        Field[] superFields = entity.getClass().getSuperclass().getDeclaredFields();

        Field[] allFields = ArrayUtils.addAll(fields, superFields);

        Optional<Field> idFiledOptional = Arrays.stream(allFields).filter(field -> field.isAnnotationPresent(TableId.class)).findFirst();
        //当不包含@TableId是，忽略
        if (!idFiledOptional.isPresent()) {
            return;
        }

        //主键字段
        Field idField = idFiledOptional.get();
        idField.setAccessible(true);
        //判断单一索引
        for (Field field : fields) {
            if (field.isAnnotationPresent(Unique.class)) {
                Unique unique = field.getDeclaredAnnotation(Unique.class);
                QueryWrapper<T> wrapper = Wrappers.query();
                Integer integer;
                try {
                    Object value = getFieldValue(entity, field);
                    //如果没有指定列，默认是字段的驼峰转下划线
                    String column;
                    if (StringUtils.isBlank(unique.column())) {
                        //字段，驼峰转下划线
                        column = TimiUtils.camelToUnderline(field.getName());
                    } else {
                        column = unique.column();
                    }
                    wrapper.eq(column, value);
                    if (isUpdate) {
                        wrapper.ne(idField.getAnnotation(TableId.class).value(), idField.get(entity));
                    }
                    integer = getMapper().selectCount(wrapper);
                } catch (Exception e) {
                    continue;
                }
                if (integer > 0) {
                    throw BusinessExceptionBuilder.build(unique.code(), field.getName());
                }
            }
        }

        //判断联合索引
        //用户存放各分组的聚合索引
        Map<String, QueryWrapper<T>> unionUniqueMap = new HashMap<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(UnionUnique.class)) {
                try {
                    UnionUnique[] unionUniques = field.getDeclaredAnnotationsByType(UnionUnique.class);
                    for (UnionUnique unionUnique : unionUniques) {
                        String group = unionUnique.group();
                        Object value = getFieldValue(entity, field);
                        String column;
                        if (StringUtils.isBlank(unionUnique.column())) {
                            //字段，驼峰转下划线
                            column = TimiUtils.camelToUnderline(field.getName());
                        } else {
                            column = unionUnique.column();
                        }
                        if (unionUniqueMap.containsKey(group)) {
                            QueryWrapper<T> unionWrapper = unionUniqueMap.get(group);
                            unionWrapper.eq(column, value);
                        } else {
                            QueryWrapper<T> unionWrapper = Wrappers.query();
                            unionWrapper.eq(column, value);
                            unionUniqueMap.put(group, unionWrapper);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        //遍历聚集索引
        Set<Map.Entry<String, QueryWrapper<T>>> entries = unionUniqueMap.entrySet();

        for (Map.Entry<String, QueryWrapper<T>> entry : entries) {
            QueryWrapper<T> queryWrapper = entry.getValue();
            if (isUpdate) {
                try {
                    queryWrapper.ne(idField.getAnnotation(TableId.class).value(), idField.get(entity));
                } catch (Exception e) {
                    return;
                }
            }
            //查询
            Integer result = getMapper().selectCount(queryWrapper);

            if (result > 0) {
                String group = entry.getKey();
                //错误提示
                Class<? extends BaseEntity> aClass = entity.getClass();
                UnionUniqueCode[] unionUniqueCodes = aClass.getAnnotationsByType(UnionUniqueCode.class);

                for (UnionUniqueCode unionUniqueCode : unionUniqueCodes) {
                    if (StringUtils.equals(unionUniqueCode.group(), group)) {
                        throw BusinessExceptionBuilder.build(unionUniqueCode.code());
                    }
                }
            }
        }
    }

    /**
     * 刷新对应字段的缓存
     *
     * @param entity
     * @param operationEnum 操作类型
     */
    default void refreshCacheFields(T entity, BaseOperationEnum operationEnum) {

        //更新缓存
        Class<? extends BaseEntity> entityClass = entity.getClass();
        Field[] declaredFields = entityClass.getDeclaredFields();

        Map<String, HashKeyValue> cacheMap = new HashMap<>();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                continue;
            }
            if (value == null) {
                continue;
            }

            if (field.isAnnotationPresent(HashKey.class)) {
                HashKey hashKey = field.getAnnotation(HashKey.class);
                String key = hashKey.key();

                //判断key是否存在
                if (cacheMap.containsKey(key)) {
                    HashKeyValue hashKeyValue = cacheMap.get(key);
                    hashKeyValue.setKey(String.valueOf(value));
                    hashKeyValue.setHashKey(hashKey);
                } else {
                    HashKeyValue hashKeyValue = new HashKeyValue();
                    hashKeyValue.setKey(String.valueOf(value));
                    hashKeyValue.setHashKey(hashKey);
                    cacheMap.put(key, hashKeyValue);
                }
            }

            if (field.isAnnotationPresent(HashValue.class)) {
                HashValue hashValue = field.getAnnotation(HashValue.class);
                String key = hashValue.key();
                //判断key是否存在
                if (cacheMap.containsKey(key)) {
                    HashKeyValue hashKeyValue = cacheMap.get(key);
                    hashKeyValue.setValue(String.valueOf(value));
                } else {
                    HashKeyValue hashKeyValue = new HashKeyValue();
                    hashKeyValue.setValue(String.valueOf(value));
                    cacheMap.put(key, hashKeyValue);
                }
            }
        }

        //缓存操作类
        CacheHelper cacheHelper = ApplicationContextUtils.getBean(CacheHelper.class);
        if (BaseOperationEnum.DELETE == operationEnum || BaseOperationEnum.DELETE_BATCH == operationEnum) {
            //删除缓存
            cacheMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getKey() != null && entry.getValue().getValue() != null)
                    .forEach(entry -> cacheHelper.hashDel(formatKey(entry.getKey(), entry.getValue().getHashKey().replace(), entity), entry.getValue().getKey()));
        } else {
            //刷新缓存，过滤掉HashKeyValue中，key为null或者value为null的情况
            cacheMap.entrySet().stream()
                    .filter(entry -> entry.getValue().getKey() != null && entry.getValue().getValue() != null)
                    .forEach(entry -> cacheHelper.hashSet(formatKey(entry.getKey(), entry.getValue().getHashKey().replace(), entity), entry.getValue().getKey(), entry.getValue().getValue()));
        }
    }


    /**
     * 替换占位符key
     *
     * @param key
     * @param replaceArray 替换
     * @param entity
     * @return
     */
    default String formatKey(String key, String[] replaceArray, T entity) {
        if (key.contains(TimiConstant.URL_PATTERN_MARK)) {
            Map<String, Object> keyPatternMap = new HashMap<>(2);
            for (String fieldName : replaceArray) {
                try {
                    Field declaredField = entity.getClass().getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    Object fieldValue = declaredField.get(entity);
                    keyPatternMap.put(fieldName, fieldValue);
                } catch (Exception e) {
                    continue;
                }
            }

            key = TimiUtils.replaceFormatString(key, keyPatternMap);
            if (key.contains(TimiConstant.URL_PATTERN_MARK)) {
                return null;
            }
        }

        return key;
    }


    /**
     * 获取属性值
     *
     * @param entity
     * @param field
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    default Object getFieldValue(T entity, Field field) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), entity.getClass());
        Method readMethod = propertyDescriptor.getReadMethod();
        return readMethod.invoke(entity);
    }


}
