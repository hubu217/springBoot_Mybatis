package com.example.service.impl;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:0xOO
 * @Date: 2018/9/26 0026
 * @Time: 15:23
 */


@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    UserMapper userMapper;


    @Override
    public User Sel(int id) {
        return userMapper.Sel(id);
    }


}
