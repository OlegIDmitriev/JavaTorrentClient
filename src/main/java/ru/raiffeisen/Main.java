package ru.raiffeisen;

import ru.raiffeisen.model.input.InputByte;
import ru.raiffeisen.model.bencode.BenElement;
import ru.raiffeisen.model.torrent.Torrent;
import ru.raiffeisen.model.torrent.TorrentBuilder;

import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //StringBuilder torrent = Input.readFile("C:\\distr\\sample.torrent");
        //System.out.println(torrent);

        //List<BenElement> elements = Input.parseBencode(torrent);
        byte[] torrent = InputByte.readFileToBytes("C:\\distr\\sample.torrent");
        System.out.println(Arrays.toString(torrent));
        Map<String, BenElement> map = InputByte.parseBenMap(torrent).getMap();
        System.out.println(map);

        Torrent torrent1 = TorrentBuilder.buildTorrent(map);


    }
}
