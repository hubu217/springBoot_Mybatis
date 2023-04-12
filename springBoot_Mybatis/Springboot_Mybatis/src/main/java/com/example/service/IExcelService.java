package com.example.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:0xOO
 * @Date: 20220804
 * @Time: 15:23
 */

public interface IExcelService {


    public String processModel(MultipartFile modelFile) throws Exception;


}
