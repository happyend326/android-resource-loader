package mobi.happyend.framework.resourceloader;

/**
 * Created with IntelliJ IDEA.
 * User: xulingzhi
 * Date: 13-5-21
 * Time: 上午10:36
 * To change this template use File | Settings | File Templates.
 */
public interface HdResourceLoader<R, Progress> {
    public R loadResource(String identifier, HdResourceCallback<R, Progress> callback, HdResourceLoaderExtraInfo extras);
    public R loadResource(String identifier, HdResourceCallback<R, Progress> callback);
    public R loadResource(String identifier);

    public void cancelAll(String tag);
    public void cancel(String identifier, String tag);
    public void cancel(String identifier, String tag, HdResourceCallback<R, Progress> callback);
}
