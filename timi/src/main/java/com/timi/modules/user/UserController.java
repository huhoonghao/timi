package com.timi.modules.user;

import org.springframework.web.bind.annotation.*;

/**
 * @author hhh
 * @date 2021/1/19
 */
@RestController
@RequestMapping("user")
public class UserController {
    @GetMapping("query")
    public String test(){
        System.out.println("test1111111");
        return "This is a Test";
    }
}
