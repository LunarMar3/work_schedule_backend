package pers.ember.backend.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pers.ember.backend.dao.UserDao;
import pers.ember.backend.entity.User;
import pers.ember.backend.mapper.UserMapper;
import pers.ember.backend.vo.CommonResponse;
import pers.ember.backend.util.JWTUtil;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.springframework.mail.SimpleMailMessage;

import javax.annotation.Resource;

@Service
public class UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public CommonResponse login(UserDao userDao) {
        String email = userDao.getEmail();
        String password = userDao.getPassword();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("email", email);
        List<User> users = userMapper.selectList(queryWrapper);
        if (users.isEmpty()) {
            return new CommonResponse(500, "用户不存在", null);
        }
        User user = users.get(0);
        if (!user.getPassword().equals(password)) {
            return new CommonResponse(500, "密码错误", null);
        }
        String token = JWTUtil.generateToken(email);
        redisTemplate.opsForValue().set("token:" + email, token, 1, TimeUnit.HOURS);
        return new CommonResponse(200, "登录成功", token);
    }

    public CommonResponse register(UserDao userDao) {
        String name = userDao.getName();
        String password = userDao.getPassword();
        String email = userDao.getEmail();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("email", email);
        String code = userDao.getCode();
        if (!code.equals(redisTemplate.opsForValue().get("code:" + email))) {
            return new CommonResponse(500, "验证码错误", null);
        }
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
        System.out.println(email);
        if (!users.isEmpty()) {
            return new CommonResponse(500, "用户已存在", null);
        }
        redisTemplate.delete("code:" + email);
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setStatus("1");
        Date date = new Date();
        user.setCreateTime(DateUtil.format(date, "yyyy-MM-dd"));
        userMapper.insert(user);
        return new CommonResponse(200, "注册成功", null);
    }


    public CommonResponse sms(UserDao userDao) {
        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        // 设置收件人
        message.setTo(userDao.getEmail());
        // 设置邮件主题
        message.setSubject("验证码");
        // 设置邮件内容
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        message.setText("您的验证码是：" + code +", 请在5分钟内输入");
        redisTemplate.opsForValue().set("code:" + userDao.getEmail(), code, 5, TimeUnit.MINUTES);
        // 发送邮件
        mailSender.send(message);
        return new CommonResponse(200, "发送成功", null);
    }

    public CommonResponse update(UserDao userDao) {
        String email = userDao.getEmail();
        String password = userDao.getPassword();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("email", email);
        List<User> users = userMapper.selectList(queryWrapper);
        String code = userDao.getCode();
        if (!code.equals(redisTemplate.opsForValue().get("code:" + email))) {
            return new CommonResponse(500, "验证码错误", null);
        }
        if (users == null) {
            return new CommonResponse(500, "用户不存在", null);
        }
        redisTemplate.delete("code:" + email);
        User user = users.get(0);
        user.setPassword(password);
        userMapper.updateById(user);
        return new CommonResponse(200, "修改成功", null);
    }
}
