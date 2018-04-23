package execute;

import helper.ProcessingJSON;
import helper.ReportCreator;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import vo.CreateCURL;
import vo.DataVO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by hemanth on 4/2/18
 */
public class ExecuteCurl {

    private static final Logger LOGGER = Logger.getLogger(ExecuteCurl.class);

    public static final String CURL = "curl";

    public static final String USER = "-u";


    public static void main(String[] args) {

        String username = "admin";
        String password = "admin";
        String url = "http://localhost:4502/bin/querybuilder.json?p.hits=selective&p.limit=-1&p.properties=jcr%3apath%20%2c%20%20" +
                "jcr%3acontent%2fjcr%3atitle%20%2c%20%20jcr%3acontent%2fcq%3alastModifiedBy%20%2c%20jcr%3acontent%2fcq%3alastModified&" +
                "path=%2fcontent%2fgeometrixx-outdoors%2fen%2fbadges&type=cq%3aPage";
        String outputFilePath = "/Users/hemanth/Downloads/report"+System.currentTimeMillis();

        /*
         *
         * Query under use -
         *
         * path=/content/geometrixx-outdoors/en/badges
         * type=cq:Page
         * p.hits=selective
         * p.properties=  jcr:path ,  jcr:content/jcr:title ,  jcr:content/cq:lastModifiedBy , jcr:content/cq:lastModified
         * p.limit=-1
         *
         * */

        //Create command to be execution
        CreateCURL createCURL = new CreateCURL.Builder().setUsername(username).setPassword(password).setUrl(url).build();
        //Command be processed
        ProcessBuilder process = new ProcessBuilder(createCURL.getCommands());
        Process p;
        try {
            p = process.start();
            String jsonString = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8.name());
            DataVO dataVO = ProcessingJSON.processJSON(jsonString);
            //String string = FileUtils.readFileToString(new File("files/json-testing-format-1.json"), StandardCharsets.UTF_8.name());
            //DataVO dataVO = ProcessingJSON.processJSON(string);
            ReportCreator.writeToCSVFile(outputFilePath, dataVO);
            ReportCreator.writeToXLSXFile(outputFilePath, dataVO);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

}
