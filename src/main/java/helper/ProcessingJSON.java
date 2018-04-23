package helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.apache.log4j.Logger;
import vo.DataVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Deserialize JSON to a flat map.
 * Approach reference https://stackoverflow.com/a/24150263/4802007
 * Created by hemanth on 4/20/18
 */
public class ProcessingJSON {

    private static final Logger LOGGER = Logger.getLogger(ProcessingJSON.class);

    public static DataVO processJSON(String jsonString) {
        LOGGER.info("---> Processing JSON conversion : Started");
        DataVO dataVO = new DataVO();
        Map<String, String> map;
        Set<String> headerSet  = new TreeSet<>();
        List<Map<String, String>> dataSet = new ArrayList<>();
        try {
            ArrayNode arrayNode = (ArrayNode) new ObjectMapper().readTree(jsonString).get("hits");
            for (int i = 0; i < arrayNode.size(); i++) {
                map = new TreeMap<>();
                addKeys("hits", arrayNode.get(i), map);
                //LOGGER.info(map);
                dataSet.add(map);
                headerSet.addAll(map.keySet());
            }
            dataVO.setDataSet(dataSet);
            dataVO.setHeaderSet(headerSet);
            //addKeys("hits", new ObjectMapper().readTree(jsonString).get("hits"), map);
        } catch (Exception e) {
            LOGGER.error("Error while Deserialize JSON to a flat map. ", e);
        }
        LOGGER.info("---> Processing JSON conversion : Ended");
        return dataVO;
    }

    private static void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) throws Exception {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }
}
