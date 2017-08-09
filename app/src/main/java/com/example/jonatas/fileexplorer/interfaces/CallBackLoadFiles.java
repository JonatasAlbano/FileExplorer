package com.example.jonatas.fileexplorer.interfaces;

import com.example.jonatas.fileexplorer.model.FileItem;

import java.util.List;

/**
 * Created by jonatas on 10/04/2017.
 */

public interface CallBackLoadFiles<ItemFile> {
    void onLoadSuccess(List<FileItem> fileItems);
    void onLoadFailure(Exception e);
}
