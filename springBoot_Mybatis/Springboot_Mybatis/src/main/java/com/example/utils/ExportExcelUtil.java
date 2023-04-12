package com.example.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.entity.User;


/**
 * @param <T>
 * @author vn0gv3u
 */
public class ExportExcelUtil<T> {


    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param title   表格标题名
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    @SuppressWarnings("deprecation")
    public void exportExcel(String title, List<String> headList, List<T> dataset, String pattern) throws Exception {


        String path = "E://" + title + ".xlsx";
        OutputStream out = new FileOutputStream(new File(path));
        // 声明一个工作薄
        //HSSFWorkbook workbook = new HSSFWorkbook();
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth(80);
        // 生成一个样式
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        XSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);

        if (dataset == null || dataset.size() == 0)
            return;

        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headList.size(); i++) {
            XSSFCell cell = row.createCell(i);
            //cell.setCellStyle(style);
            String headerValue = headList.get(i);
            cell.setCellValue(headerValue);

        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            Field[] fields = t.getClass().getDeclaredFields();
            List<Field> fieldsList = new ArrayList<>();
            for (Field field : fields) {
                fieldsList.add(field);
            }
            for (Field field : fieldsList) {
                XSSFCell cell = row.createCell(fieldsList.indexOf(field));
                cell.setCellStyle(style2);
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(t, new Object[]{});
                // 判断值的类型后进行强制类型转换
                formateValue(value, cell, pattern);
            }
        }
        workbook.write(out);
    }


    public void formateValue(Object value, XSSFCell cell, String pattern) {

        if (value == null) {
            cell.setCellValue("");
        }
        if (value instanceof Integer) {
            int intValue = (Integer) value;
            cell.setCellValue(intValue);
        } else if (value instanceof Float) {
            float fValue = (Float) value;
            cell.setCellValue(fValue);
        } else if (value instanceof Double) {
            double dValue = (Double) value;
            cell.setCellValue(dValue);
        } else if (value instanceof Long) {
            long longValue = (Long) value;
            cell.setCellValue(longValue);
        } else if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String textValue = sdf.format(date);
            cell.setCellValue(textValue);
        } else {
            // 其它数据类型都当作字符串简单处理
            String textValue = value == null ? "" : value.toString();
            cell.setCellValue(textValue);
        }

    }


    public static void main(String[] args) throws Exception {


        String title = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<String> headList = new ArrayList<String>();
        headList.add("列1");
        headList.add("列2");
        headList.add("列3");
        headList.add("列4");

        List<User> dataList = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setPassWord("密码-" + i);
            user.setRealName("真实姓名-" + i);
            user.setUserName("用户姓名-" + i);
            dataList.add(user);
        }

        ExportExcelUtil util = new ExportExcelUtil();
        util.exportExcel(title, headList, dataList, "yyyy-MM-dd");


    }

}