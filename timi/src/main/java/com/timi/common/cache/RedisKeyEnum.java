package com.timi.common.cache;

/**
 * redis中的key
 *
 * @since 2020-06-20
 */
public enum RedisKeyEnum {

    /**
     * 作用：JWT本身的token失效（私钥共享）有缺陷，登出后token仍然有效
     * 故设置每个用户独享一个加密私钥，当用户登出后，改变私钥
     */
    TOKEN_JWT_EXPIRE("bms:token:EXPIRE:", "用户存储失效的token"),

    /**
     * 用户与部门的对应关系
     */
    USER_DEPARTMENT_MAPPING("bms:user:department:mapping", "用户与部门的对应关系"),


    /**
     * 作用：JWT本身的token失效（私钥共享）有缺陷，登出后token仍然有效
     * 故设置每个用户独享一个加密私钥，当用户登出后，改变私钥
     */
    TOKEN_JWT_USER("timi:user:token", "存放用户名及对应的token"),

    /**
     * 密码错误次数
     */
    USER_PASSWORD_ERROR_NUMBER("bms:user:password:errorNumber:", "密码错误次数"),


    /**
     * 保存用户对应的权限
     */
    USER_AUTHORITIES("bms:user:authorities:", "保存用户对应的权限"),

    /**
     * 数据字典
     */
    DICT_PREFIX("bms:dict:", "数据字典"),

    /**
     * 生成服务代码、仓库代码等
     */
    SERVICE_SERIAL_NUMBER("bms:service:serial:", "代码编号"),

    /**
     * 国家
     */
    BUSINESS_BASE_COUNTRY("bms:base:country", "国家"),

    /**
     * 城市
     */
    BUSINESS_BASE_CITY("bms:base:city", "城市"),

    /**
     * 启运港
     */
    BUSINESS_BASE_SEAPORT("bms:base:seaport", "港口"),

    /**
     * 车型
     */
    BUSINESS_BASE_GOODS("bms:base:goods", "车型"),

    /**
     * 车型体积
     */
    BUSINESS_BASE_GOODS_VOLUME("bms:base:goods:volume", "车型体积"),

    /**
     * 车型高度
     */
    BUSINESS_BASE_GOODS_HEIGHT("bms:base:goods:height", "车型高度"),

    /**
     * 航线
     */
    BUSINESS_BASE_ROUTE("bms:base:ship:route", "航线"),

    /**
     * 仓库或VD
     */
    BUSINESS_BASE_WAREHOUSE("bms:base:warehouse", "仓库或VDC"),

    /**
     * 船公司
     */
    BUSINESS_BASE_SHIP_COMPANY("bms:base:ship:company", "船公司"),

    /**
     * 缓存合同与港口、仓库（VDC）的对应关系（根据业务类型分）
     */
    BUSINESS_CONTRACT_SEAPORT_WAREHOUSE("bms:base:contract:seaportWarehouse:", "合同与港口、仓库（VDC）的对应关系"),

    /**
     * 缓存合同类型
     */
    BUSINESS_CONTRACT_Type("bms:base:contract:type", "缓存存放合同类型（销售或采购）"),

    /**
     * 缓存合同与公司编号
     */
    BUSINESS_CONTRACT_COMPANY("bms:base:contract:company", "合同与公司的对应关系"),

    /**
     * @Description 是否
     * @author wangxiaoliang
     * @date 2020-08-31 11:09
     */
    BUSINESS_YES_NO("bms:dict:yes_no", "是否"),

    /**
     * @Description 区域
     * @author wangxiaoliang
     * @date 2020-08-31 11:06
     */
    BUSINESS_DICT_AREA("bms:dict:dict_area", "区域"),

    /**
     * 用户与供方对应关系
     */
    USER_APPLY("bms:base:user:apply", "用户与供方对应关系");

    public String key;
    public String message;

    RedisKeyEnum(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }
}
