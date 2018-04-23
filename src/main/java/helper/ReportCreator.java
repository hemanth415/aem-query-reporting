package helper;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vo.DataVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Created by hemanth on 4/4/18
 */
public class ReportCreator {

    private static final Logger LOGGER = Logger.getLogger(ReportCreator.class);

    @Deprecated
    public static void writeToCSVFile(String outputFilePath, String dataString) {
        try {
            FileUtils.write(new File(outputFilePath + System.currentTimeMillis() + ".csv"), dataString, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            LOGGER.error("Exception: While creating/writing CSV. Exception is : {}", e);
        }
    }

    @Deprecated
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
            LOGGER.error("Exception: While creating/writing Excel. Exception is : {}", e);
        }
    }

    @Deprecated
    private static void loadDataWorksheetCell(Row dataRow, List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            dataRow.createCell(i).setCellValue(data.get(i));
        }
    }
    /**
     * @param outputFilePath
     * @param dataVO
     * @throws Exception
     */
    public static void writeToCSVFile(String outputFilePath, DataVO dataVO) {
        try {
            LOGGER.info("---> CSV file report creation : Started");
            File file = new File(outputFilePath + System.currentTimeMillis() + ".csv");
            // Create a File and append if it already exists.
            Writer writer = new FileWriter(file, true);
            csvWriter(writer, dataVO);
            LOGGER.info("---> CSV file report creation : Ended");
        } catch (Exception e) {
            LOGGER.error("Exception: While creating/writing CSV. Exception is : {}", e);
        }
    }

    /**
     * @param dataVO
     * @param writer
     * @throws Exception
     */
    private static void csvWriter(Writer writer, DataVO dataVO) throws Exception {
        CsvSchema schema = null;
        CsvSchema.Builder schemaBuilder = CsvSchema.builder();
        if (CollectionUtils.isNotEmpty(dataVO.getDataSet())) {
            for (String column : dataVO.getHeaderSet()) {
                schemaBuilder.addColumn(column);
            }
            schema = schemaBuilder.build().withLineSeparator(System.lineSeparator()).withHeader();
        }
        CsvMapper mapper = new CsvMapper();
        mapper.writer(schema).writeValues(writer).writeAll(dataVO.getDataSet());
        writer.flush();
    }



    public static void writeToXLSXFile(String outputFilePath, DataVO dataVO) {
        try {
            LOGGER.info("---> Excel file report creation : Started");
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Query Report");
            int cellCount = NumberUtils.INTEGER_ZERO;
            Row dataRow;
            if (CollectionUtils.isNotEmpty(dataVO.getDataSet())) {
                dataRow = sheet.createRow(NumberUtils.INTEGER_ZERO);
                for (String column : dataVO.getHeaderSet()) {
                    dataRow.createCell(cellCount++).setCellValue(column);
                }
            }
            loadDataWorksheetCell(sheet, dataVO);
            FileOutputStream outputStream = new FileOutputStream(outputFilePath + System.currentTimeMillis() + ".xlsx");
            workbook.write(outputStream);
            LOGGER.info("---> Excel file report creation : Ended");
        } catch (Exception e) {
            LOGGER.error("Exception: While creating/writing Excel. Exception is : {}", e);
        }
    }

    private static void loadDataWorksheetCell(Sheet sheet, DataVO dataVO) {
        int rowCount = NumberUtils.INTEGER_ONE;
        int cellCount;
        Row dataRow;
        for( Map<String, String> rowData: dataVO.getDataSet()){
            dataRow = sheet.createRow(rowCount++);
            cellCount = NumberUtils.INTEGER_ZERO;
            for (String key : dataVO.getHeaderSet()) {
                if (rowData.containsKey(key)) {
                    dataRow.createCell(cellCount++).setCellValue(rowData.get(key));
                } else {
                    cellCount++;
                }

            }
        }
    }







}
