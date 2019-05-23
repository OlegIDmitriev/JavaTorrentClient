package ru.raiffeisen.model.torrent;

import ru.raiffeisen.model.bencode.BenElement;
import ru.raiffeisen.model.bencode.element.BenList;
import ru.raiffeisen.model.bencode.element.BenMap;
import ru.raiffeisen.model.bencode.element.BenNumber;
import ru.raiffeisen.model.bencode.element.BenString;
import ru.raiffeisen.model.torrent.Torrent;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TorrentBuilder {
    public static Torrent buildTorrent(Map<String, BenElement> map) {
        Torrent torrent = new Torrent();

        for(String key : map.keySet()){
            processProperties(torrent, key, map);
        }

        return torrent;
    }

    private static void processProperties(Torrent torrent, String key, Map<String, BenElement> map) {
        switch (key) {
            case "info":
                processInfo(torrent, (BenMap) map.get(key));
                break;
            case "announce":
                torrent.setAnnounce(((BenString) map.get(key)).getValue());
                break;
            case "announce-list":
                processAnnounceList(torrent, (BenList) map.get(key));
                break;
            case "creation date":
                Date created = new Date(((BenNumber) map.get(key)).getValue()*1000);
                torrent.setCreationDate(created);
                break;
            case "comment":
                torrent.setComment(((BenString) map.get(key)).getValue());
                break;
            case "created by":
                torrent.setCreatedBy(((BenString) map.get(key)).getValue());
                break;
            case "publisher":
                torrent.setPublisher(((BenString) map.get(key)).getValue());
                break;
            case "encoding":
                torrent.setEncoding(((BenString) map.get(key)).getValue());
                break;
            case "publisher-url":
                torrent.setPublisherUrl(((BenString) map.get(key)).getValue());
                break;
        }
    }

    private static void processInfo(Torrent torrent, BenMap info) {
        torrent.setInfoHash(sha1(info.getBencode()));
        for(String key : info.getMap().keySet()){
            processInfoProperties(torrent, key, info.getMap());
        }
    }

    private static String sha1(byte[] bytes) {
        String sha1 = null;
        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(bytes);
            sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static void processInfoProperties(Torrent torrent, String key, Map<String, BenElement> map) {
        switch (key) {
            case "piece length":
                torrent.setPieceLength(((BenNumber) map.get(key)).getValue());
                break;
            case "pieces":
                torrent.setPieces(((BenString) map.get(key)).getValue());
                break;
            case "name":
                torrent.setName(((BenString) map.get(key)).getValue());
                break;
            case "length":
                torrent.setLength(Integer.parseInt(((BenString) map.get(key)).getValue()));
                break;
            case "md5sum":
                torrent.setMd5sum(((BenString) map.get(key)).getValue());
                break;
            case "files":
                processFilesList(torrent, (BenList) map.get(key));
                break;
            case "private":
                torrent.setPrivate(((BenNumber) map.get(key)).getValue() == 1);
                break;
        }
    }

    private static void processAnnounceList(Torrent torrent, BenList list) {
        List<String> announceList = new ArrayList<>();

        processList(announceList, list);

        torrent.setAnnounceList(announceList);
    }

    private static void processList(List<String> list, BenList Benlist) {
        for (BenElement benElement : Benlist.getList()) {
            if (benElement instanceof BenString) {
                list.add(((BenString) benElement).getValue());
            } else if (benElement instanceof BenList) {
                processList(list, (BenList) benElement);
            }
        }
    }

    private static void processFilesList(Torrent torrent, BenList list) {
        List<FileInfo> files = new ArrayList<>();

        for(BenElement element : list.getList()){
            Map<String, BenElement> map = ((BenMap) element).getMap();
            List<BenElement> path = ((BenList) map.get("path")).getList();
            int length = ((BenNumber) map.get("length")).getValue();
            BenString bMd5sum = (BenString) map.get("md5sum");
            String md5 = bMd5sum != null ? bMd5sum.getValue() : null;

            FileInfo fileInfo = new FileInfo(length,((BenString) path.get(0)).getValue(),  md5);
            files.add(fileInfo);
        }

        torrent.setFiles(files);
        torrent.setSingleFile(false);
    }
}
