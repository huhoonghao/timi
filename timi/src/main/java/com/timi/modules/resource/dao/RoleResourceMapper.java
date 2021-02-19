package com.timi.modules.resource.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.timi.modules.resource.dao.entity.RoleResourceEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 资源数据库操作
 */
@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResourceEntity> {



    /**
     * 保存角色和资源的对应关系
     * @param roleCode
     * @param resCodes
     */
    @Insert("    INSERT INTO anji_role_resource(\n" +
            "            role_code,\n" +
            "            res_code,\n" +
            "            create_by,\n" +
            "            create_time\n" +
            "            )\n" +
            "               VALUES\n" +
            "            <foreach collection=\"resCodes\" separator=\",\" item=\"resCode\">\n" +
            "            (#{roleCode},#{resCode},#{createBy},now())\n" +
            "        </foreach>")
    public void saveRoleResource(String roleCode, List<String> resCodes);

}
