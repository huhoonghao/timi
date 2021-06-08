package com.timi.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timi.common.annotation.TimiLog;
import com.timi.common.bean.ResponseBean;
import com.timi.common.util.TimiUtils;
import com.timi.modules.user.holder.UserContentHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author hhh
 * @date 2021/5/20
 */
public abstract class BaseController<P extends PageParam, T extends BaseEntity, D extends BaseDTO> {

    /**
     * 记录日志
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取实际服务类
     *
     * @return
     */
    public abstract BaseService<P, T> getService();

    /**
     * 获取当前Controller数据传输DTO
     *
     * @return
     */
    public abstract D getDTO();

    /**
     * 获取当前Controller数据库实体Entity
     *
     * @return
     */
    public abstract T getEntity();

    /**
     * 分页模板
     *
     * @param param
     * @return
     */
    @TimiLog(pageTitle = "查询")
    @GetMapping("/pageList")
    public ResponseBean pageList(P param) {
        logger.info("入参{}",param);
        IPage<T> iPage = getService().page(param);
        List<T> records = iPage.getRecords();
        //entity转换成DTO
        List<D> list = (List<D>) TimiUtils.copyList(records, getDTO().getClass());
        //处理dto返回结果
        pageResultHandler(list);
        Page<D> pageDto = new Page<>();
        pageDto.setCurrent(iPage.getCurrent())
                .setRecords(list)
                .setPages(iPage.getPages())
                .setTotal(iPage.getTotal())
                .setSize(iPage.getSize());
        return  ResponseBean.builder().data(pageDto).build();
    }


    /**
     * 根据ID查询相关记录
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean detail(@PathVariable("id") Long id) {
        logger.info("{}根据ID查询服务开始，id为：{}", this.getClass().getSimpleName(), id);
        T result = getService().selectOne(id);

        D dto = getDTO();
        TimiUtils.copyAndFormatter(result, dto);

        //对返回值建处理
        detailResultHandler(dto);
        ResponseBean responseBean = ResponseBean.builder().data(resultDtoHandle(dto)).build();
        logger.info("{}根据ID查询结束，结果：{}", this.getClass().getSimpleName(), TimiUtils.toJSONString(responseBean));
        return responseBean;
    }


    /**
     * 插入
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseBean insert(@Validated @RequestBody D dto) {
        logger.info("{}新增服务开始，参数：{}", this.getClass().getSimpleName(), TimiUtils.toJSONString(dto));

        ResponseBean responseBean = responseSuccess();
        T entity = getEntity();
        //dto转为数据库实体
        BeanUtils.copyProperties(dto, entity);
        //插入
        getService().insert(entity);

        logger.info("{}新增服务结束，结果：{}", this.getClass().getSimpleName(), TimiUtils.toJSONString(responseBean));
        return responseBean;
    }

    /**
     * 根据ID修改对应记录
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping
    public ResponseBean update(@Validated @RequestBody D dto) {
        String username = UserContentHolder.getContext().getUsername();
        logger.info("{}更新服务开始,更新人：{}，参数：{}", this.getClass().getSimpleName(), username, TimiUtils.toJSONString(dto));
        T entity = getEntity();
        //dto转换entity
        BeanUtils.copyProperties(dto, entity);
        entity.setUpdateBy(username);
        entity.setUpdateTime(new Date());
        getService().update(entity);
        logger.info("{}更新服务结束，结果：{}", this.getClass().getSimpleName(), TimiUtils.toJSONString(entity));
        return responseSuccess();
    }

    /**
     * 根据ID删除指定记录,这里被删除的记录会进入删除记录表
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean deleteById(@PathVariable("id") Long id) {
        logger.info("{}删除服务开始，参数ID：{}", this.getClass().getSimpleName(), id);
        getService().deleteById(id);
        logger.info("{}删除服务结束", this.getClass().getSimpleName());
        return responseSuccess();
    }

    /**
     * 删除批量ID对应的记录
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete/batch")
    public ResponseBean deleteBatchIds(@RequestBody List<Serializable> ids) {
        logger.info("{}批量删除服务开始，批量参数Ids：{}", this.getClass().getSimpleName(), TimiUtils.toJSONString(ids));
        boolean deleteCount = getService().deleteByIds(ids);

        ResponseBean responseBean = ResponseBean.builder().data(deleteCount).build();

        logger.info("{}批量删除服务结束，结果：{}", this.getClass().getSimpleName(), TimiUtils.toJSONString(responseBean));
        return responseBean;
    }


    /**
     * 构建成功响应实例
     *
     * @return
     */
    public ResponseBean responseSuccess() {
        return ResponseBean.builder().build();
    }

    /**
     * 对详情返回DTO进行处理
     * @param detail
     * @return
     */
    public D detailResultHandler(D detail) {
        return detail;
    }
    /**
     * 对明细结果进行处理,子类可以覆盖
     * @param d
     * @return
     */
    protected D resultDtoHandle(D d) {
        return d;
    }

    /**
     * 对分页dto返回处理
     *
     * @param list
     * @return
     */
    public List<D> pageResultHandler(List<D> list) {
        return list;
    }

    /**
     * 构建成功响应实例
     *
     * @param data
     * @return
     */
    public ResponseBean responseSuccessData(Object data) {
        return ResponseBean.builder().data(data).build();
    }

}
