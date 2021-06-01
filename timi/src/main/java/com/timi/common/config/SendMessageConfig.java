package com.timi.common.config;

import lombok.Data;

/**
 * @author hhh
 * @date 2021/5/31
 */
@Data
public class SendMessageConfig {
    private String url="https://dfsns.market.alicloudapi.com";
    private String path = "/data/send_sms";
    private String method="path";
    private String appCode="cf274906dfc147b48e10dcdf9accf95e";

}
