package com.example.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fy
 * @ClassName: ExcelUtils
 * @Description: 解析excel表
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
	                IOUtils.closeQuietly(work);
	            }
	        }
	        
	        return work;
    }
    
    
    
    
    
    
    
    
    
  //此为main方法仅用作测试，可忽略，
    public static void main(String[] args) throws IOException, InvalidFormatException {
        File file = new File("D:\\data\\数据\\数据.xls");
        FileInputStream fis = new FileInputStream(file);

        //将输出的流对象引入到解析excel文件的对象
        HSSFWorkbook wb = new HSSFWorkbook(fis);
//        XSSFWorkbook wb = new XSSFWorkbook(fis);
//        Workbook wb = WorkbookFactory.create(fis);
//        Workbook wb = ExcelUtils.getWorkBookFromMultipartFile();

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
//            XSSFSheet sheet = wb.getSheetAt(i);
//            Sheet sheet = wb.getSheetAt(i);
            Row row = null;
            int lastRowNum = sheet.getLastRowNum();

            for (int y = 0; y < lastRowNum; y++) {
                row = sheet.getRow(y);
                if (null != row) {
                    //获取每一列值
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        String value = cell.getStringCellValue();
                        System.out.println(value + "|");
                    }
                    System.out.println(row);
                }
            }
        }
    }
}
