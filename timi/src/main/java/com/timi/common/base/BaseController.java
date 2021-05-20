package com.timi.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.timi.common.bean.ResponseBean;
import com.timi.common.util.TimiUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
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
        return ResponseBean.builder().content(pageDto).build();
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


}
