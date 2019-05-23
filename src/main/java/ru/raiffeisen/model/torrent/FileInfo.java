package ru.raiffeisen.model.torrent;

public class FileInfo {
    private int length;
    private String md5sum;
    private String path;

    public FileInfo() {
    }

    public FileInfo(int length, String md5sum, String path) {
        this.length = length;
        this.md5sum = md5sum;
        this.path = path;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
