package com.example.jonatas.fileexplorer.asynctask;

import android.os.AsyncTask;

import com.example.jonatas.fileexplorer.helper.FileItemsHelper;
import com.example.jonatas.fileexplorer.interfaces.CallBackLoadFiles;
import com.example.jonatas.fileexplorer.model.FileItem;

import java.util.List;

/**
 * Created by jonatas on 10/04/2017.
 */

public class LoadFilesTask extends AsyncTask<Void, Void, List<FileItem>> {

    private CallBackLoadFiles<FileItem> mCallBackLoadFiles;
    private Exception mException;
    private String mDirectory;

    public LoadFilesTask(String directory, CallBackLoadFiles callBack) {
        mDirectory = directory;
        mCallBackLoadFiles = callBack;
    }
    @Override
    protected List<FileItem> doInBackground(Void... params) {
        try {
            FileItemsHelper fileItemsHelper = new FileItemsHelper();
            return fileItemsHelper.getFiles(mDirectory);
        } catch (Exception e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<FileItem> fileItems) {
        if(mCallBackLoadFiles != null) {
            if(mException == null) {
                mCallBackLoadFiles.onLoadSuccess(fileItems);
            } else {
                mCallBackLoadFiles.onLoadFailure(mException);
            }
        }
    }
}
