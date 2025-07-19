package utilities;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;

public class ReaderExcelData {


    public static String getCellData(String FilePath, int RowIndex, int ColIndex)
    {
        String value="";
        try{
            FileInputStream file = new FileInputStream(new File(FilePath));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(RowIndex);
            Cell cell= row.getCell(ColIndex);
            value= cell.getStringCellValue();
            file.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  value;

    }

}
