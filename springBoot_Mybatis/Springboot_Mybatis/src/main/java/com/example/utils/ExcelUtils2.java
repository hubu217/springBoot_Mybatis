package com.example.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;


/**
 * @author hb
 * @decription 解析excel文件 [跨行跨列]
 */
@Slf4j
public class ExcelUtils2 {


    public static void main(String[] args) {

        ExcelUtils2 vo = new ExcelUtils2();
        vo.excelRegionReadStart();
    }


    public void excelRegionReadStart() {


        InputStream inputStream = null;// 输入流对象
        XSSFWorkbook xssfWorkbook = null; //工作簿
        try {
            File file = new File("E:/跨行跨列3.xlsx");






            inputStream = new FileInputStream(file);






            //定义工作簿
            xssfWorkbook = new XSSFWorkbook(inputStream);
            //获取第一个sheet;[写死只有一个sheet]
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //获取合并单元格信息的hashmap[跨行跨列的核心逻辑]
            Map<String, Integer[]> mergedRegionMap = getMergedRegionMap(sheet);
            log.info("mergedRegionMap=" + JSON.toJSONString(mergedRegionMap));
            //拿到excel的最后一行的索引; [获取excel的行数]
            int lastRowNum = sheet.getLastRowNum();
            //从excel的第二行索行开始，遍历到最后一行（第一行是标题，直接跳过不读取）
            for (int i = 0; i <= lastRowNum; i++) {
                //拿到excel的行对象
                XSSFRow row = sheet.getRow(i);
                //获取excel的行中有多个列;[ 获取excel的列数]
                int cellNum = row.getLastCellNum();
                //对每行进行列遍历，即一列一列的进行解析
                for (int j = 0; j < cellNum; j++) {
                    String value = "";
                    //拿到了excel的列对象
                    Cell cell = row.getCell(j);
                    //将列对象的行号和列号+下划线组成key去hashmap中查询，不为空说明当前的cell是合并单元列
                    Integer[] firstRowNumberAndCellNumber = mergedRegionMap.get(i + "_" + j);
                    //如果是合并单元列，就取合并单元格的首行和首列所在位置读数据，否则就是直接读数据
                    if (firstRowNumberAndCellNumber != null) {
                        XSSFRow rowTmp = sheet.getRow(firstRowNumberAndCellNumber[0]);
                        Cell cellTmp = rowTmp.getCell(firstRowNumberAndCellNumber[1]);
                        value = getCellValue(cellTmp);
                    } else {
                        value = getCellValue(cell);
                    }
                    System.out.print(value + "   ");
                }

                System.out.println();

            }

        } catch (Exception e) {
            log.error("error", e);
        } finally {

            // 关闭文件流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
            // 关闭工作簿
            if (xssfWorkbook != null) {
                try {
                    xssfWorkbook.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }

        }

    }

    //将存在合并单元格的列记录put进hashmap并返回
    public Map<String, Integer[]> getMergedRegionMap(Sheet sheet) {

        Map<String, Integer[]> resultMap = new HashMap<String, Integer[]>();
        //获取excel中的所有合并单元格信息
        int sheetMergeCount = sheet.getNumMergedRegions();
        //遍历处理
        for (int i = 0; i < sheetMergeCount; i++) {
            //拿到每个合并单元格，开始行，结束行，开始列，结束列
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            //构造一个开始行和开始列组成的数组
            Integer[] firstRowNumberAndCellNumber = new Integer[]{firstRow, firstColumn};
            //遍历，将单元格中的所有行和所有列处理成由行号和下划线和列号组成的key，然后放在hashmap中
            for (int currentRowNumber = firstRow; currentRowNumber <= lastRow; currentRowNumber++) {
                for (int currentCellNumber = firstColumn; currentCellNumber <= lastColumn; currentCellNumber++) {
                    resultMap.put(currentRowNumber + "_" + currentCellNumber, firstRowNumberAndCellNumber);
                }
            }
        }

        return resultMap;

    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell) {

        if (cell == null) return "";

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return cell.getCellFormula();

        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

            return String.valueOf(cell.getNumericCellValue());

        }

        return "";

    }

}
