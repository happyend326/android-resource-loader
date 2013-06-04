package mobi.happyend.framework.resourceloader;

import mobi.happyend.framework.asynctask.HdAsyncTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: xulingzhi
 * Date: 13-5-21
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class HdAbstractResourceLoader<R, Progress> implements HdResourceLoader<R,Progress>{
    @Override
    public void cancelAll(String tag) {
        if (tag != null) {
            HdAsyncTask.removeAllTask(tag);
        }
    }

    @Override
    public void cancel(String identifier, String tag){
        this.cancel(identifier, tag, null);
    }

    @Override
    public void cancel(String identifier, String tag, HdResourceCallback callback){
        HdAsyncTask task = HdAsyncTask.searchTask(identifier+tag);
        if(task == null){
            return;
        }
        HdResourceLoaderTask rTask = (HdResourceLoaderTask)task;

        Iterator ite = rTask.extras.entrySet().iterator();
        int activeCallbacks = 0;
        while(ite.hasNext()){
            Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo> entry = (Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo>) ite.next();
            HdResourceCallback cb = entry.getKey();//map中的key
            HdResourceLoaderExtraInfo extraInfo = entry.getValue();//上面key对应的value
            if(callback!=null && !callback.isCanceled()){
                activeCallbacks++;
            }
        }

        if(activeCallbacks >=2){
            if(rTask.extras.containsKey(callback)) {
                rTask.delegateCancel(callback);
            }
        } else {
            rTask.cancel();
        }
    }


    @Override
    public R loadResource(String identifier, HdResourceCallback callback, HdResourceLoaderExtraInfo extraInfos) {
        R resource = loadResourceFromMemory(identifier);
        if(resource != null){
            return resource;
        }

        if(callback == null){
            callback = new HdResourceCallback() {
            };
        }

        HdAsyncTask task = null;

        if(extraInfos != null && extraInfos.getTag() != null){
            task = HdAsyncTask.searchTask(identifier+extraInfos.getTag());
        }  else {
            task = HdAsyncTask.searchTask(identifier);
        }
        // 判断是否已在任务队列
        if (task != null && task.getStatus() != HdAsyncTask.HdAsyncTaskStatus.FINISHED) {
            HdResourceLoaderTask rTask = (HdResourceLoaderTask)task;
            rTask.addSameTask(callback, extraInfos);
        }  else {
            HdResourceLoaderTask rTask = new HdResourceLoaderTask(identifier, callback, extraInfos);
            rTask.execute();
        }
        return null;
    }

    @Override
    public R loadResource(String identifier, HdResourceCallback callback) {
        return loadResource(identifier, callback, null);
    }

    @Override
    public R loadResource(String identifier) {
        return loadResource(identifier, null, null);
    }

    protected abstract R loadResourceFromMemory(String identifier);
    protected abstract R loadResourceFromLocal(String identifier);
    protected abstract R loadResourceFromRemote(String identifier);

    public class HdResourceLoaderTask extends HdAsyncTask<String, Progress, R> {
        private String identifier;
        private Map<HdResourceCallback, HdResourceLoaderExtraInfo> extras = new HashMap<HdResourceCallback,HdResourceLoaderExtraInfo>();

        public HdResourceLoaderTask(String identifier, HdResourceCallback callback, HdResourceLoaderExtraInfo extraInfo){
            extras.put(callback, extraInfo);
            if(extraInfo == null){
                setKey(identifier);
            }  else {
                if(extraInfo.getTag() != null){
                    setKey(identifier+extraInfo.getTag());
                    setTag(extraInfo.getTag());
                }  else {
                    setKey(identifier);
                }

                if(extraInfo.getPriority() != null){
                    setPriority(extraInfo.getPriority()) ;
                }

                if(extraInfo.getType() != null){
                    setType(extraInfo.getType());
                }
            }
        }

        public void addSameTask(HdResourceCallback callback, HdResourceLoaderExtraInfo extraInfo){
            this.extras.put(callback, extraInfo);
            if(extraInfo != null){
                if(extraInfo.getPriority() != null){
                    setPriority(extraInfo.getPriority()) ;
                }

                if(extraInfo.getType() != null){
                    setType(extraInfo.getType());
                }
            }
        }

        public void delegatePublishProgress(Progress... progress){
            publishProgress(progress);
        }

        protected void delegateCancel(HdResourceCallback callback){
            if(callback != null) {
                callback.onPreCancel(identifier, extras.get(callback));
                //cancel callback
                callback.cancel();
                callback.onCancelled(identifier, extras.get(callback));
            }
        }

        @Override
        protected R doInBackground(String... params) {
            R resource = loadResourceFromMemory(identifier);
            if(resource != null){
                return resource;
            }

            resource = loadResourceFromLocal(identifier);
            if(resource != null){
                return resource;
            }

            resource = loadResourceFromRemote(identifier);

            return resource;
        }

        protected void onPreCancel(){
            Iterator ite = extras.entrySet().iterator();

            while(ite.hasNext()){
                Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo> entry = (Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo>) ite.next();
                HdResourceCallback callback = entry.getKey();
                HdResourceLoaderExtraInfo extraInfo = entry.getValue();
                if(!callback.isCanceled()){
                    callback.onPreCancel(identifier, extraInfo);
                }
            }
        }

        protected void onPreExecute() {
            Iterator ite = extras.entrySet().iterator();

            while(ite.hasNext()){
                Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo> entry = (Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo>) ite.next();
                HdResourceCallback callback = entry.getKey();
                HdResourceLoaderExtraInfo extraInfo = entry.getValue();
                if(!callback.isCanceled()){
                    callback.onStart(identifier, extraInfo);
                }

            }
        }

        protected void onPostExecute(Object result) {
            Iterator ite = extras.entrySet().iterator();

            while(ite.hasNext()){
                Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo> entry = (Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo>) ite.next();
                HdResourceCallback callback = entry.getKey();
                HdResourceLoaderExtraInfo extraInfo = entry.getValue();
                if(!callback.isCanceled()){
                    callback.onLoaded(result, identifier, extraInfo);
                }
            }

        }

        protected void onProgressUpdate(Object... values) {
            Iterator ite = extras.entrySet().iterator();
            while(ite.hasNext()){
                Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo> entry = (Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo>) ite.next();
                HdResourceCallback callback = entry.getKey();
                HdResourceLoaderExtraInfo extraInfo = entry.getValue();
                if(!callback.isCanceled()){
                    callback.onProgressUpdate(values);
                }
            }
        }

        protected void onCancelled() {
            Iterator ite = extras.entrySet().iterator();

            while(ite.hasNext()){
                Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo> entry = (Map.Entry<HdResourceCallback, HdResourceLoaderExtraInfo>) ite.next();
                HdResourceCallback callback = entry.getKey();
                HdResourceLoaderExtraInfo extraInfo = entry.getValue();
                if(!callback.isCanceled()){
                    callback.onCancelled(identifier, extraInfo);
                }

            }
        }
    }
}
