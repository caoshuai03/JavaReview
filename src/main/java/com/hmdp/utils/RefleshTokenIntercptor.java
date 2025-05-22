package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author caoshuai
 * @version 1.0
 */
//拦截器，给MvcConfig.java用
public class RefleshTokenIntercptor implements HandlerInterceptor {
    //不能使用spirng的注入,在配置拦截器的地方注入

    private StringRedisTemplate stringRedisTemplate;

    public RefleshTokenIntercptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token以及用户
        String token = request.getHeader("authorization");
        //判断用户token是否存在
        if (StrUtil.isBlank(token)) {
            return true;
        }
//        HttpSession session = request.getSession();
//        Object user = session.getAttribute("user");

        //判断user 不存在就拦截
//        if (user == null) {
//            response.setStatus(401);
//            return false;
//        }
        // 将查询的数据转换成userDTO
        //通过token得到值，是一个map对象
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash()
                .entries(key);
        //判断token是否有效
        if (userMap == null) {
            return true;
        }

        //map转对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        //token有效，就保存user到Threadlocal，即连接池获得一次连接
        UserHolder.saveUser(userDTO);

        // 刷新token有效期
        stringRedisTemplate.expire(key, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //最后释放s
        UserHolder.removeUser();
    }
}
