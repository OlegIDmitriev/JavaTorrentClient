package ru.raiffeisen.model.net.http;

import ru.raiffeisen.model.bencode.BenElement;
import ru.raiffeisen.model.input.InputByte;
import ru.raiffeisen.model.torrent.Torrent;
import ru.raiffeisen.model.torrent.TorrentBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class HttpURLConnectionExample {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main2(String[] args) throws Exception {

        HttpURLConnectionExample http = new HttpURLConnectionExample();

        System.out.println("Testing 1 - Send Http GET request");
        String url = "http://www.google.com/search?q=mkyong";
        http.sendGet(url);

        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }

    public static void main(String[] args) throws Exception {
        byte[] torrentBytes = InputByte.readFileToBytes("C:\\distr\\s2.torrent");
        Map<String, BenElement> map = InputByte.parseBenMap(torrentBytes).getMap();

        Torrent torrent = TorrentBuilder.buildTorrent(map);

        HttpURLConnectionExample http = new HttpURLConnectionExample();


        System.out.println("Testing 1 - Send Http GET request");
        //String encodeURL1 = URLEncoder.encode(torrent.getInfoHash(), "UTF-8");
        String encodeURL1 = encodeInfoHash(torrent.getInfoHash());
        URLEnc urlConverter = new URLEnc(torrent.getInfoHash());
        String encodeURL2 = urlConverter.getEncoded();
        String url = torrent.getAnnounce() + "?info_hash=" + encodeURL2;
        http.sendGet(url);

        //System.out.println("\nTesting 2 - Send Http POST request");
        //http.sendPost();

    }

    private static String encodeInfoHash(String code)
    {
        //string code = "78820B24672757A60BEF15252E93F3D0C4DEB5C3";
        byte[] codeB = code.getBytes();
        String info_hash_encoded = "";
        for (int i = 0; i < code.length(); i += 2)
        {
            String tmpCode = code.substring(i, i+2);
            int j = Integer.parseInt(tmpCode,16);
            char s1 = (char)j;
            boolean isSpecChar = false;
            if (s1 == '.' || s1 == '-' || s1 == '~')
                isSpecChar = true;
            if ((Character.isAlphabetic(s1) || Character.isDigit(s1) || isSpecChar) && !Character.isISOControl(s1) && j <= 127)
            {
                info_hash_encoded += s1;
            }
            else
            {
                info_hash_encoded += "%" + tmpCode.toLowerCase();
            }
        }
        return info_hash_encoded;
    }

    private static String encodeSha1(String sha1) throws Exception{
        byte[] infoHash = DatatypeConverter.parseHexBinary(sha1);
        String ecodedValue1 = URLEncoder.encode(new String(infoHash, ISO_8859_1.name()), StandardCharsets.UTF_8.name());

        return ecodedValue1;
    }

    // HTTP GET request
    private void sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

}