package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.IExcelService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/excelController")
public class ExcelController {


    @Autowired
    private IExcelService excelService;


    @RequestMapping(value = "/parseExcelFile", method = RequestMethod.POST)
    public String parseExcelFile(@RequestParam("file") MultipartFile multipartFile) {


        try {


            String targetStr = excelService.processModel(multipartFile);
            log.info("targetStr=" + targetStr);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
