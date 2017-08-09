package com.example.jonatas.fileexplorer.comparator;

import com.example.jonatas.fileexplorer.model.FileItem;

import java.util.Comparator;

/**
 * Created by jonatas on 10/04/2017.
 */

public class FileItemComparator implements Comparator<FileItem>{
    @Override
    public int compare(FileItem fileItem1, FileItem fileItem2) {
        return fileItem1.getNome().compareTo(fileItem2.getNome());
    }
}
