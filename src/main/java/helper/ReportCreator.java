package helper;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by hemanth on 4/4/18
 */
public class ReportCreator {

    private static final Logger LOGGER = Logger.getLogger(ProcessJSON.class);

    public static void writeToCSVFile(String outputFilePath, String csvString){
        try {
            FileUtils.write(new File(outputFilePath), csvString, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            LOGGER.error("IOException: While writing CSV. Exception: {}", e);
        }
    }
}
