package vo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hemanth on 4/20/18
 */
public class DataVO {

    private List<Map<String, String>> dataSet;

    private Set<String> headerSet;


    public List<Map<String, String>> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Map<String, String>> dataSet) {
        this.dataSet = dataSet;
    }

    public Set<String> getHeaderSet() {
        return headerSet;
    }

    public void setHeaderSet(Set<String> headerSet) {
        this.headerSet = headerSet;
    }
}
