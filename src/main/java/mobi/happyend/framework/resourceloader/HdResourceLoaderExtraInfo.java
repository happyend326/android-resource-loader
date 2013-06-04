package mobi.happyend.framework.resourceloader;

import mobi.happyend.framework.asynctask.HdAsyncTaskType;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源下载额外信息
 * 
 * @author gaofei
 */
public class HdResourceLoaderExtraInfo {
    private int position;
    private Integer priority;
    private String tag;
    private HdAsyncTaskType type;

	private Map<String, Object> extraInfos = new HashMap<String, Object>();

    public void setExtraInfo(String key, Object value){
         extraInfos.put(key, value);
    }

    public Object getExtraInfo(String key){
        return extraInfos.get(key);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public HdAsyncTaskType getType() {
        return type;
    }

    public void setType(HdAsyncTaskType type) {
        this.type = type;
    }
}