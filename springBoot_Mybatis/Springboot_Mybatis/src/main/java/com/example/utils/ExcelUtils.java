package com.example.utils;


import com.example.exception.MyException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author hb
 * @ClassName: ExcelUtils
 * @Description: 解析excel文件 [标准格式]
 * @Description: 上传/下载 文件
 * @date 2022/4/8 15:25
 */
public class ExcelUtils {


    // 2003- 版本的excel
    private final static String excel2003L = ".xls";
    // 2007+ 版本的excel
    private final static String excel2007U = ".xlsx";

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();


    static {
        numberFormat.setGroupingUsed(false);
    }

    /**
     * 校验文件格式
     *
     * @param filename
     * @return
     */
    public static boolean validateFileType(String filename) {

        if (!filename.contains(".")) {
            return false;
        } else {

            String fileType = filename.substring(filename.lastIndexOf("."));
            switch (fileType) {
                case excel2003L:
                    return true;
                case excel2007U:
                    return true;
                default:
                    return false;
            }
        }
    }


    /**
     * 根据版本获取excel对象
     *
     * @param url
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static Workbook getWork(String url, InputStream inputStream) throws IOException {

        Workbook wb = null;
        String str = url.substring(url.lastIndexOf("."));

        if (excel2003L.equals(str)) {
            wb = new HSSFWorkbook(inputStream);  // 2003-
        } else if (excel2007U.equals(str)) {
            wb = new XSSFWorkbook(inputStream);  // 2007+
        } else {
            throw new IOException("解析文件格式有误!");
        }
        return wb;
    }


    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {

        Object result = "";
        if (cell != null) {

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    // 在excel里,日期也是数字,在此要进行判断
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = cell.getDateCellValue();
                        result = sdf.format(date);
                    } else {
                        // 数字也按照字符串类型获取值
                        // DecimalFormat df = new DecimalFormat("#");
                        // result = df.format(cell.getNumericCellValue());
                        // cell.setCellType(Cell.CELL_TYPE_STRING);
                        // result = cell.getStringCellValue();
                        double d = cell.getNumericCellValue();
                        result = numberFormat.format(d);
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:// 布尔
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:// 公式
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK: // null
                    break;
                default:
                    break;
            }
        }

        return result.toString().trim();
    }


    public static boolean validateExcelHeader(Sheet sheet, int firstRowNum, List<String> columnInfoList) {

        Row row = sheet.getRow(firstRowNum);
        List<Boolean> flags = new ArrayList<>();
        for (int i = 0; i < columnInfoList.size(); i++) {
            boolean flag = true;
            if (!StringUtils.equals(columnInfoList.get(i).toLowerCase(), ExcelUtils.getCellValue(row.getCell(i)).toLowerCase())) {
                flag = false;
                break;
            }
            flags.add(flag);
        }

        boolean headerFlag = false;
        for (Boolean flag : flags) {
            headerFlag = (headerFlag || flag);
        }
        return headerFlag;
    }


    public static Workbook getWorkBookFromMultipartFile(MultipartFile multipartFile) throws Exception {

        Workbook work = null;
        try {
            work = ExcelUtils.getWork(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new Exception("获取excel表对象异常！");
        } finally {
            if (null != work) {
               // IOUtils.closeQuietly(work);
            }
        }

        return work;
    }


    public static void doExcel() throws IOException, InvalidFormatException {

        File file = new File("E:/哈尔滨统计-test3.xlsx");
        boolean flag = file.exists();
        if (!flag) {
            throw new MyException("100005", "文件不存在！");
        }
        FileInputStream fis = new FileInputStream(file);

        //将输出的流对象引入到解析excel文件的对象
        //HSSFWorkbook wb = new HSSFWorkbook(fis);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
//         Workbook wb = WorkbookFactory.create(fis);
//         Workbook wb = ExcelUtils.getWorkBookFromMultipartFile();

        int sheetsNum = wb.getNumberOfSheets(); //excel的sheet大小
        int rowNum = 0; //excel的总行数
        int columnNum = 0; //excel的总列数
        for (int i = 0; i < sheetsNum; i++) {
            System.out.println("页数: 第" + i + "页! ===============================================================");
// 		             HSSFSheet sheet = wb.getSheetAt(i);
            XSSFSheet sheet = wb.getSheetAt(i);
            //            Sheet sheet = wb.getSheetAt(i);

            rowNum = sheet.getLastRowNum() + 1;
            for (int y = 0; y < rowNum; y++) {
                Row row = sheet.getRow(y);    //行
                if (null != row) {
                    //获取每一列值
                    columnNum = row.getLastCellNum();
                    for (int j = 0; j < columnNum; j++) {
                        Cell cell = row.getCell(j); //列/单元格
                        if (cell == null) {
                            continue;
                        }
                        //获取单元格/列 的值
                        String value = getValue(cell);
                        System.out.print(value + " | ");
                    }
                    System.out.println();
                }
            }
            System.out.println("sheetsNum总页数=" + sheetsNum + ";  rowNum总行数=" + rowNum + ";  columnNum总列数=" + columnNum);
            System.out.println();

        }


    }


    private static String getValue(Cell hssfCell) {

        if (hssfCell == null) {
            return null;
        }
        if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            // 返回数字类型的值
            if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                SimpleDateFormat sdf = null;
                if (hssfCell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                    sdf = new SimpleDateFormat("HH:mm");
                } else {// 日期
                    sdf = new SimpleDateFormat("yyyy/MM/dd");
                }
                Date date = hssfCell.getDateCellValue();
                return sdf.format(date);
            } else if (hssfCell.getCellStyle().getDataFormat() == 58) {
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                double value = hssfCell.getNumericCellValue();
                Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                return sdf.format(date);
            } else {
                double value = hssfCell.getNumericCellValue();
                CellStyle style = hssfCell.getCellStyle();
                DecimalFormat format = new DecimalFormat();
                String temp = style.getDataFormatString();
                // 单元格设置成常规
                if (temp.equals("General")) {
                    format.applyPattern("#");
                }
                return format.format(value);
            }
        } else {
            // 返回字符串类型的值
            hssfCell.setCellType(hssfCell.CELL_TYPE_STRING);
            return hssfCell.toString();
        }
    }


    public static void demo2() {

        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid=" + uuid + "; length=" + uuid.length());

    }


    //此为main方法仅用作测试，可忽略，
    public static void main(String[] args) {

        try {


            doExcel();
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
            String code = e.getCode();
            String msg = e.getMsg();
            System.out.println("code=" + code + "; msg=" + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
