package ru.raiffeisen.model.torrent;

import ru.raiffeisen.model.bencode.element.BenMap;

import java.util.Date;
import java.util.List;

public class Torrent {
    private Date creationDate;
    private String createdBy;
    private String publisher;
    private String comment;
    private String encoding;
    private String publisherUrl;
    private List<String> announceList;
    private String announce;
    private String name;
    private int length;
    private String md5sum;
    private List<FileInfo> files;
    private int pieceLength;
    private String pieces;
    private boolean isSingleFile = true;
    private boolean isPrivate = false;
    private String infoHash;

    public String getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(String infoHash) {
        this.infoHash = infoHash;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void addFileInfo(FileInfo fileInfo){
        files.add(fileInfo);
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isSingleFile() {
        return isSingleFile;
    }

    public void setSingleFile(boolean singleFile) {
        isSingleFile = singleFile;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public List<String> getAnnounceList() {
        return announceList;
    }

    public void setAnnounceList(List<String> announceList) {
        this.announceList = announceList;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPieceLength() {
        return pieceLength;
    }

    public void setPieceLength(int pieceLength) {
        this.pieceLength = pieceLength;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}