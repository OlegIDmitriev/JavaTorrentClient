package ru.raiffeisen.model.input;

import ru.raiffeisen.model.bencode.BenElement;
import ru.raiffeisen.model.bencode.element.BenList;
import ru.raiffeisen.model.bencode.element.BenMap;
import ru.raiffeisen.model.bencode.element.BenNumber;
import ru.raiffeisen.model.bencode.element.BenString;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InputByte {
    public static byte[] readFileToBytes(String path) {
        byte[] array = null;
        try {
            array = Files.readAllBytes(Paths.get(path));
        } catch (IOException e){
            e.printStackTrace();
        }

        return array;
    }

    public static List<BenElement> parseBencode(byte[] bencode){
        List<BenElement> elements = new ArrayList<>();
        int index = 0;

        while (index < bencode.length){
            BenElement element = parseBenElement(bencode, index);
            elements.add(element);
            index += element.getLength();
        }

        return elements;
    }

    private static BenElement parseBenElement(byte[] bencode, int index){
        char c =(char) bencode[index];
        BenElement element = null;

        if(c == 'i'){
            element = parseBenNumber(bencode, index);
        } else if(Character.isDigit(c)){
            element = parseBenString(bencode, index);
        } else if(c == 'l'){
            element = parseBenList(bencode, index);
        } else if(c == 'd'){
            element = parseBenMap(bencode, index);
        }

        return element;
    }

    private static BenNumber parseBenNumber(byte[] bencode, int index){
        char c = (char) bencode[index];

        if(c != 'i'){
            return null;
        }

        String number = getStringFromByteArray(bencode, ++index, getIndexOfByteArray(bencode, 'e', index));

        return new BenNumber(Integer.parseInt(number), number.length() + 2);
    }

    private static String getStringFromByteArray(byte[] bytes, int start, int end){
        if(bytes == null || start <0 || end <= start )
            return null;

        String str = new String(bytes, start, end - start, StandardCharsets.UTF_8);

        return str;
    }

    private static int getIndexOfByteArray(byte[] bytes, char c, int start){
        if(bytes == null || start <0 || start > bytes.length -1){
            return -1;
        }

        for(int i = start; i < bytes.length; i++){
            if(c == (char) bytes[i])
                return i;
        }

        return -1;
    }

    private static BenString parseBenString(byte[] bencode, int index){
        char c = (char) bencode[index];

        if(!Character.isDigit(c)){
            return null;
        }

        String strLength = getStringFromByteArray(bencode, index, getIndexOfByteArray(bencode, ':', index));
        index += strLength.length() + 1;

        int length = Integer.parseInt(strLength);
        String value = getStringFromByteArray(bencode, index, index + length);

        return new BenString(value, strLength.length() + length + 1);
    }

    private static BenList parseBenList(byte[] bencode, int index){
        char c = (char) bencode[index];

        if(c != 'l'){
            return null;
        }

        BenList list = new BenList();
        index++;

        while (index < bencode.length && (char) bencode[index] != 'e'){
            BenElement element = parseBenElement(bencode, index);
            list.add(element);
            index += element.getLength();
        }

        list.incLength(2);
        return list;
    }

    public static BenMap parseBenMap(byte[] bencode){
        return parseBenMap(bencode, 0);
    }

    private static BenMap parseBenMap(byte[] bencode, int index){
        char c = (char) bencode[index];
        int startIndex = index;

        if(c != 'd'){
            return null;
        }

        BenMap map = new BenMap();
        index++;

        while (index < bencode.length && (char) bencode[index] != 'e'){
            BenString key = parseBenString(bencode, index);
            index += key.getLength();

            BenElement element = parseBenElement(bencode, index);
            map.put(key, element);
            index += element.getLength();
        }

        map.incLength(2);
        byte[] mapBenCode = new byte[map.getLength()];

        System.arraycopy(bencode, startIndex, mapBenCode, 0,  map.getLength());
        map.setBencode(mapBenCode);
        return map;
    }
}
