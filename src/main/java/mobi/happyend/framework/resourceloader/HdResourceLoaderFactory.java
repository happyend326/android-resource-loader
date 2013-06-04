package mobi.happyend.framework.resourceloader;

/**
 * Created with IntelliJ IDEA.
 * User: xulingzhi
 * Date: 13-5-21
 * Time: 上午10:34
 * To change this template use File | Settings | File Templates.
 */
public interface HdResourceLoaderFactory {
    public HdResourceLoader getLoader(int type);
}
