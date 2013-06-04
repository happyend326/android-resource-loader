package mobi.happyend.framework.resourceloader.utils;

/**
 * Created with IntelliJ IDEA.
 * User: xulingzhi
 * Date: 13-5-21
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */
public interface HdLocalCache<T> {
    public void saveLocal(String key, T value, Object extra);
    public T getLocal(String key);
    public void removeLocal(String key);
    public void removeAll();
}
