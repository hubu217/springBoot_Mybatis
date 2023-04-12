package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.exception.MyException;
import com.example.service.IExcelService;
import com.example.utils.ExcelUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author:0xOO
 * @Date: 20220804
 * @Time: 15:23
 */


@Slf4j
@Service
public class ExcelServiceImpl implements IExcelService {


    @Override
    public String processModel(MultipartFile modelFile) throws Exception {

        List<String> cellList = new ArrayList<String>();
        List<List<String>> rowList = new ArrayList<List<String>>();
        boolean isFile = false;
        String modelFilename = modelFile.getOriginalFilename();
        if (StringUtils.isBlank(modelFilename)) {
            throw new MyException("100001", "未选择模板，请重新选择模板！");
        }
        // 文件格式校验
        isFile = ExcelUtils.validateFileType(modelFilename);
        if (!isFile) {
            throw new MyException("100002", "excel模板命名异常，请重新命名后上传！");
        }

        // 获取excel对象
        Workbook workbook = null;
        try {
            workbook = ExcelUtils.getWorkBookFromMultipartFile(modelFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("异常信息为：" + e.getMessage());
        }

        int number = 0;
        if (workbook != null) {
            // 获取电子表格数量
            number = workbook.getNumberOfSheets();
        }

        for (int i = 0; i < number; i++) {
            // 获取当前sheet表对象
            Sheet sheet = workbook.getSheetAt(i);
            Row row = null;
            // 获取表的最大行数
            int lastRowNum = sheet.getLastRowNum();
            for (int y = 0; y <= lastRowNum; y++) {
                // 表头跳过
                if (y == 0) {
                    continue;
                }
                // 获取行数
                row = sheet.getRow(y);
                if (row == null) {
                    throw new Exception("excel表内容为空！");
                }
                short num = row.getLastCellNum();
                log.info("单元格数量为：" + num);
                // 获取每一列值
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // 获取每一行的单元格值
                    Cell cell = row.getCell(j);
                    // 单元格转换成String类型
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    if (StringUtils.isNotBlank(value) && !"".equals(value)) {
                        cellList.add(value);
                        log.info(value + "|");
                    }
                }
                rowList.add(cellList);
                log.info("excel表格行信息为：" + row);
            }
        }


        return null;
    }


}
