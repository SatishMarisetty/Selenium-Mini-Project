package selenium;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {
    private Workbook workbook;
    private Sheet sheet;
    public ExcelUtils(String excelFilePath) {
        try {
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getCellData(int rowNumber, int columnNumber) {
        if(sheet == null) {
            return null;
        }
        Row row = sheet.getRow(rowNumber);
        if(row == null) {
            return null;
        }
        Cell cell = row.getCell(columnNumber);
        if(cell == null) {
            return null;
        }
        
        return cell.toString();
    }
    
    public void closeWorkbook() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}