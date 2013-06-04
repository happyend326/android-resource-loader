package mobi.happyend.framework.resourceloader;


public abstract class HdResourceCallback<R, Progress> {
    private boolean isCancel;
    protected final void cancel(){
        isCancel = true;
    }
    protected final boolean isCanceled(){
        return isCancel;
    }
    protected void onPreCancel(String key, HdResourceLoaderExtraInfo extraInfo){
    }

    protected void onStart(String key, HdResourceLoaderExtraInfo extraInfo) {
    }

    protected void onLoaded(R resource, String key, HdResourceLoaderExtraInfo extraInfo) {
    }

    protected void onProgressUpdate(Progress... values) {
    }

    protected void onCancelled(String key, HdResourceLoaderExtraInfo extraInfo) {
    }
}