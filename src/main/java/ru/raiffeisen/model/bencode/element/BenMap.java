package ru.raiffeisen.model.bencode.element;

import ru.raiffeisen.model.bencode.BenElement;

import java.util.HashMap;
import java.util.Map;

public class BenMap extends BenElement {
    private Map<String, BenElement> map;
    private int length;
    private byte[] bencode;

    public BenMap() {
        map = new HashMap<>();
        length = 0;
    }

    public BenMap(Map<String, BenElement> map, int length) {
        this.map = map;
        this.length = length;
    }

    public void put(BenString key, BenElement element){
        map.put(key.getValue(), element);
        length += key.getLength() + element.getLength();
    }

    public Map<String, BenElement> getMap() {
        return map;
    }

    public void incLength(int inc){
        length += inc;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String toString(){
        return map.toString();
    }

    public byte[] getBencode() {
        return bencode;
    }

    public void setBencode(byte[] bencode) {
        this.bencode = bencode;
    }
}
