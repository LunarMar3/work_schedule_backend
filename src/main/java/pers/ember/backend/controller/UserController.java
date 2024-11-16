package pers.ember.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.ember.backend.dao.UserDao;
import pers.ember.backend.service.UserService;
import pers.ember.backend.vo.CommonResponse;

@RestController
@RequestMapping("/user")
public class UserController {

        @Autowired
        private UserService userService;

        @PostMapping("/login")
        public CommonResponse login(@RequestBody UserDao userDao) {
            return userService.login(userDao);
        }

        @PostMapping("/register")
        public CommonResponse register(@RequestBody UserDao userDao) {
            return userService.register(userDao);
        }

        @PostMapping("/sms")
        public CommonResponse sms(@RequestBody UserDao userDao) {
            return userService.sms(userDao);
        }
        @PostMapping("/update")
        public CommonResponse update(@RequestBody UserDao userDao) {
            return userService.update(userDao);
        }
}
