package mobi.happyend.framework.resourceloader.utils;

import android.os.Environment;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: xulingzhi
 * Date: 13-5-21
 * Time: 下午1:30
 * To change this template use File | Settings | File Templates.
 */
public class HdSDCardManager {
    public String SDCardRoot;
    public File updateFile;

    private boolean available;
    private boolean writeable;

    private static HdSDCardManager manager;

    private HdSDCardManager(){
        SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        updateState();
    }

    public static HdSDCardManager getInstance(){
        if(manager == null) {
            manager = new HdSDCardManager();
        }
        return manager;
    }

    private void updateState() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            available = true;
            writeable = true;
        } else if (status.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            available = true;
            writeable = false;
        } else {
            available = false;
            writeable = false;
        }
    }

    public boolean isWriteable(){
        return writeable;
    }

    public boolean isAvailable(){
        return available;
    }

    /**
     * Create File in sd card
     * @param fileName
     * @param dir
     * @return File created file
     */
    public File createFileInSDCard(String fileName, String dir) {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        try
        {
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        updateFile = file;
        return file;
    }

    /**
     * Create Dir
     *
     * @param dir
     * @return File created dir
     */
    public File creatSDDir(String dir) {
        File dirFile = new File(SDCardRoot + dir + File.separator);
        dirFile.mkdirs();
        return dirFile;
    }

    /**
     * Check if file exists;
     *
     * @param fileName
     * @param path
     * @return Boolean
     */
    public boolean isFileExist(String fileName, String path) {
        File file = new File(SDCardRoot + path + File.separator + fileName);
        return file.exists();
    }

    public boolean isDirExist(String path){
        File file = new File(SDCardRoot + path);
        return file.exists();
    }

    /**
     * Delete dir and all files in dir
     *
     * @param filepath
     */
    public void rmDirFiles(String filepath) {
        File dir = new File(filepath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            } else if (dir.isFile()) {
                dir.delete();
            }
        }
    }

    public void rmFile(String filepath){
        File file = new File(filepath);
        if(file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * Write file by InputStream
     *
     * @param path
     * @param fileName
     * @param input
     * @return File
     */
    public File writeToSDFromInput(String path, String fileName, InputStream input) {

        File file = null;
        OutputStream output = null;
        try
        {
            file = createFileInSDCard(fileName, path);
            output = new FileOutputStream(file, false);
            byte buffer[] = new byte[4 * 1024];
            int temp;
            while ((temp = input.read(buffer)) != -1)
            {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                output.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Write String to file
     *
     * @param path
     * @param fileName
     * @param  data
     * @return File
     */
    public File writeToSDFromInput(String path, String fileName, String data) {

        File file = null;
        OutputStreamWriter outputWriter = null;
        OutputStream outputStream = null;
        try
        {
            creatSDDir(path);
            file = createFileInSDCard(fileName, path);
            outputStream = new FileOutputStream(file, false);
            outputWriter = new OutputStreamWriter(outputStream);
            outputWriter.write(data);
            outputWriter.flush();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                outputWriter.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return file;
    }
}
