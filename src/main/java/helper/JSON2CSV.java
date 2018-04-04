package helper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * Created by hemanth on 4/2/18
 */
public class JSON2CSV {

    private static final Logger LOGGER = Logger.getLogger(JSON2CSV.class);

    public static StringBuilder processJSON(JSONObject inputJSONObject) {
        JSONArray jsonArray = new JSONArray(inputJSONObject.getJSONArray("hits").toString());
        StringBuilder sb = new StringBuilder();
        if (jsonArray != null && jsonArray.length() > 1) {
            LOGGER.info("Number of hits: " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i == 0) {
                    createCSVHeaderFromJSONObject((JSONObject) jsonArray.get(i), sb);
                    sb =  new StringBuilder(StringUtils.removeEnd(sb.toString(), ","));
                    sb.append("\n");
                }
                JSONObject object = (JSONObject) jsonArray.get(i);
                readJSONObject(object, sb);
                sb =  new StringBuilder(StringUtils.removeEnd(sb.toString(), ","));
                StringUtils.removeEnd(sb.toString(), ",");
                sb.append("\n");
            }
        }
        return sb;
    }

    private static StringBuilder createCSVHeaderFromJSONObject(JSONObject object, StringBuilder stringBuilder) {
        Iterator<?> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (object.get(key) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object.get(key);
                createCSVHeaderFromJSONObject(jsonObject, stringBuilder);
            } else {
                stringBuilder.append(key).append(",");
            }
        }
        return stringBuilder;
    }

    private static StringBuilder readJSONObject(JSONObject object, StringBuilder stringBuilder) {
        Iterator<?> iterator = object.keys();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();

            if (object.get(key) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object.get(key);
                readJSONObject(jsonObject, stringBuilder);
            } else {
                String value = object.get(key).toString();
                stringBuilder.append(value).append(",");
            }
        }
        return stringBuilder;
    }

    public static void writeToCSVFile(String outputFilePath, String csvString){
        try {
            FileUtils.write(new File(outputFilePath), csvString, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            LOGGER.error("IOException: While writing CSV. Exception: {}", e);
        }
    }
}
