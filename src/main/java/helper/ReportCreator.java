package helper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by hemanth on 4/4/18
 */
public class ReportCreator {

    private static final Logger LOGGER = Logger.getLogger(ProcessJSON.class);

    public static void writeToCSVFile(String outputFilePath, String dataString) {
        try {
            FileUtils.write(new File(outputFilePath + System.currentTimeMillis() + ".csv"), dataString, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            LOGGER.error("IOException: While writing CSV. Exception: {}", e);
        }
    }


    public static void writeToXLSXFile(String outputFilePath, List<List<String>> dataList) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Query Report");
            int rowCount = NumberUtils.INTEGER_ZERO;
            Row dataRow;
            for (List<String> data : dataList) {
                dataRow = sheet.createRow(rowCount++);
                loadDataWorksheetCell(dataRow, data);
            }
            FileOutputStream outputStream = new FileOutputStream(outputFilePath + System.currentTimeMillis() + ".xlsx");
            workbook.write(outputStream);
        } catch (Exception e) {
            LOGGER.error("Exception: While writing XLXS. Exception: {}", e);
        }
    }

    private static void loadDataWorksheetCell(Row dataRow, List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            dataRow.createCell(i).setCellValue(data.get(i));
        }
    }

}
