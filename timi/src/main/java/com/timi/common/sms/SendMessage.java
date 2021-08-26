package com.timi.common.sms;

import com.timi.common.config.SendMessageConfig;
import com.timi.common.constant.SmsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hhh
 * @date 2021/5/28
 * TIMI_0003
 * 【Timi】尊敬的%{user}用户：您账号已被冻结,请联系客服:%{customerService}
 *
 *
 * TIMI_0002
 * 【Timi】尊敬的%{user}用户：您于%{time}时间登录 登录地为:%{address} IP:%{ip}
 *
 *
 * TIMI_0001
 * 【Timi】尊敬的%{user}用户：您的验证码是:%{code},有效时间%{expire}分钟
 */
@Slf4j
public class SendMessage {

    public  static  void   doSend(){
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "cf274906dfc147b48e10dcdf9accf95e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "user:18321456123,code:1234,expire:5");
        bodys.put("phone_number", "18321456123");
        bodys.put("template_id", "TIMI_0001");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(host+path+method+headers+querys+bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        doSend();
    }



    //验证码
    public static void sendSms(String phone,String code) {
        try {
            // 模板参数
            String content="user:"+phone+","+"code:"+code+","+"expire:"+5;
            String templateId= SmsConstant.TIMI_0001.getValue();
            // 异步发送短信验证码
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doSend(phone,templateId,content);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public  static  void   doSend(String phone,String templateId,String content){
        SendMessageConfig sendMessageConfig=new SendMessageConfig();
        String host = sendMessageConfig.getUrl();
        String path = sendMessageConfig.getPath();
        String method = sendMessageConfig.getMethod();
        String appcode = sendMessageConfig.getAppCode();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", content);
        bodys.put("phone_number", phone);
        bodys.put("template_id", templateId);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            log.info(response.toString());
            log.info(host+path+method+headers+querys+bodys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
