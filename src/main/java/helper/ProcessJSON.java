package helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hemanth on 4/2/18
 */
@Deprecated
public class ProcessJSON {

    private static final Logger LOGGER = Logger.getLogger(ProcessJSON.class);

    public static StringBuilder processJSON(JSONObject inputJSONObject) {
        JSONArray jsonArray = new JSONArray(inputJSONObject.getJSONArray("hits").toString());
        StringBuilder sb = new StringBuilder();
        if (jsonArray != null && jsonArray.length() > 1) {
            LOGGER.info("Number of hits: " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i == 0) {
                    createHeaderForReport((JSONObject) jsonArray.get(i), sb);
                    sb = new StringBuilder(StringUtils.removeEnd(sb.toString(), ","));
                    sb.append("\n");
                }
                JSONObject object = (JSONObject) jsonArray.get(i);
                createDataForReport(object, sb);
                sb = new StringBuilder(StringUtils.removeEnd(sb.toString(), ","));
                StringUtils.removeEnd(sb.toString(), ",");
                sb.append("\n");
            }
        }
        return sb;
    }

    private static StringBuilder createHeaderForReport(JSONObject object, StringBuilder stringBuilder) {
        Iterator<?> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (object.get(key) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object.get(key);
                createHeaderForReport(jsonObject, stringBuilder);
            } else {
                stringBuilder.append(key).append(",");
            }
        }
        return stringBuilder;
    }

    private static StringBuilder createDataForReport(JSONObject object, StringBuilder stringBuilder) {
        Iterator<?> iterator = object.keys();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();

            if (object.get(key) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object.get(key);
                createDataForReport(jsonObject, stringBuilder);
            } else {
                String value = object.get(key).toString();
                stringBuilder.append(value).append(",");
            }
        }
        return stringBuilder;
    }

    public static String processJsonToString(JSONObject inputJSONObject) {
        List<List<String>> processedData = processJsonToList(inputJSONObject);
        StringBuilder processedString = new StringBuilder();
        if (CollectionUtils.isNotEmpty(processedData)) {
            for (List<String> data : processedData) {
                processedString.append(StringUtils.join(data, ',')).append("\n");
            }
            return processedString.toString();
        }
        return StringUtils.EMPTY;
    }

    public static List<List<String>> processJsonToList(JSONObject inputJSONObject) {
        JSONArray jsonArray = new JSONArray(inputJSONObject.getJSONArray("hits").toString());
        List<List<String>> processedData = new ArrayList<>();
        if (jsonArray != null && jsonArray.length() > 1) {
            LOGGER.info("Number of hits: " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i == 0) {
                    List<String> header = createHeaderForReport((JSONObject) jsonArray.get(i), new ArrayList<>());
                    processedData.add(header);
                }
                JSONObject object = (JSONObject) jsonArray.get(i);
                List<String> data = createDataForReport(object, new ArrayList<>());
                processedData.add(data);
            }
        }
        return processedData;
    }

    private static List<String> createHeaderForReport(JSONObject object, List<String> headerData) {
        Iterator<?> iterator = object.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (object.get(key) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object.get(key);
                createHeaderForReport(jsonObject, headerData);
            } else {
                headerData.add(key);
            }
        }
        return headerData;
    }

    private static List<String> createDataForReport(JSONObject object, List<String> data) {
        Iterator<?> iterator = object.keys();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();

            if (object.get(key) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object.get(key);
                createDataForReport(jsonObject, data);
            } else {
                String value = object.get(key).toString();
                data.add(value);
            }
        }
        return data;
    }

}
