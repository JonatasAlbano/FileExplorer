package com.example.jonatas.fileexplorer.helper;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.example.jonatas.fileexplorer.comparator.FileItemComparator;
import com.example.jonatas.fileexplorer.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jonatas on 10/04/2017.
 */

public class FileItemsHelper {

    public List<FileItem> getFiles(String directory) {

        List<FileItem> items = new ArrayList<FileItem>();
        FileItemComparator comparator = new FileItemComparator();
        File dir = new File(directory);
        File[] files = dir.listFiles();
        List<FileItem> foldersFiles = new ArrayList<FileItem>();
        List<FileItem> defaultFiles = new ArrayList<FileItem>();
        try {
            for (File file : files) {
                FileItem fileItem = new FileItem();
                fileItem.setNome(file.getName());
                if (file.isDirectory()) {
                    fileItem.setFolder(true);
                    foldersFiles.add(fileItem);
                } else {
                    fileItem.setFolder(false);
                    defaultFiles.add(fileItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(foldersFiles, comparator);
        Collections.sort(defaultFiles, comparator);

        items.addAll(foldersFiles);
        items.addAll(defaultFiles);
        return items;
    }

    public Intent tryToOpenFile(File file) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent;
    }

}
