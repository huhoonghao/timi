package com.timi.common.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 *
 * 程序启动初始化一些东西
 * @author hhh
 * @date 2021/6/18
 */
@Slf4j
public class ApplicationInitRunner  implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
      //TODO 初始化redis一些东西
      log.info("=====================程序初始化配置完毕启动成功====================");

    }
}
