package mobi.happyend.framework.resourceloader.utils;

/**
 * Created with IntelliJ IDEA.
 * User: xulingzhi
 * Date: 13-5-21
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public interface HdMemoryCache<K, V> {
    public V put(K key, V value);
    public V get(K key);
    public V remove(K key);
    public void clearAll();
}
