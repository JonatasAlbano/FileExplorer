package com.example.jonatas.fileexplorer.model;

/**
 * Created by jonatas on 09/04/2017.
 */

public class FileItem {
    private String nome;
    private boolean isFolder;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }
}
