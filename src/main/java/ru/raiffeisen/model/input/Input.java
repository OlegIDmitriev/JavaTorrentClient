package ru.raiffeisen.model.input;

import ru.raiffeisen.model.bencode.BenElement;
import ru.raiffeisen.model.bencode.element.BenList;
import ru.raiffeisen.model.bencode.element.BenMap;
import ru.raiffeisen.model.bencode.element.BenNumber;
import ru.raiffeisen.model.bencode.element.BenString;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Input {
    public static StringBuilder readFile(String path) {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try (InputStream is = new FileInputStream(path);
             Reader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        try{
            String temp = URLDecoder.decode(out.toString(), "UTF-8");
            return new StringBuilder(temp);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
*/
        return out;
    }

    public static byte[] readFileToBytes(String path) {
        byte[] array = null;
        try {
            array = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    public static List<BenElement> parseBencode(StringBuilder bencode) {
        List<BenElement> elements = new ArrayList<>();
        int index = 0;

        while (index < bencode.length()) {
            BenElement element = parseBenElement(bencode, index);
            elements.add(element);
            index += element.getLength();
        }

        return elements;
    }

    private static BenElement parseBenElement(StringBuilder bencode, int index) {
        char c = bencode.charAt(index);
        BenElement element = null;

        if (c == 'i') {
            element = parseBenNumber(bencode, index);
        } else if (Character.isDigit(c)) {
            element = parseBenString(bencode, index);
        } else if (c == 'l') {
            element = parseBenList(bencode, index);
        } else if (c == 'd') {
            element = parseBenMap(bencode, index);
        }

        return element;
    }

    private static BenNumber parseBenNumber(StringBuilder bencode, int index) {
        char c = bencode.charAt(index);

        if (c != 'i') {
            return null;
        }

        String number = bencode.substring(++index, bencode.indexOf("e", index));

        return new BenNumber(Integer.parseInt(number), number.length() + 2);
    }

    private static BenString parseBenString(StringBuilder bencode, int index) {
        char c = bencode.charAt(index);

        if (!Character.isDigit(c)) {
            return null;
        }

        String strLength = bencode.substring(index, bencode.indexOf(":", index));
        index += strLength.length() + 1;

        int length = Integer.parseInt(strLength);
        String value = bencode.substring(index, index + length);

        return new BenString(value, strLength.length() + length + 1);
    }

    private static BenList parseBenList(StringBuilder bencode, int index) {
        char c = bencode.charAt(index);

        if (c != 'l') {
            return null;
        }

        BenList list = new BenList();
        index++;

        while (index < bencode.length() && bencode.charAt(index) != 'e') {
            BenElement element = parseBenElement(bencode, index);
            list.add(element);
            index += element.getLength();
        }

        list.incLength(2);
        return list;
    }

    private static BenMap parseBenMap(StringBuilder bencode, int index) {
        char c = bencode.charAt(index);

        if (c != 'd') {
            return null;
        }

        BenMap map = new BenMap();
        index++;

        while (index < bencode.length() && bencode.charAt(index) != 'e') {
            BenString key = parseBenString(bencode, index);
            index += key.getLength();

            BenElement element = parseBenElement(bencode, index);
            map.put(key, element);
            index += element.getLength();
        }

        map.incLength(2);
        return map;
    }
}
