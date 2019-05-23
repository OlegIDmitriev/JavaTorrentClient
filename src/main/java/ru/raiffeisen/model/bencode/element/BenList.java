package ru.raiffeisen.model.bencode.element;

import ru.raiffeisen.model.bencode.BenElement;

import java.util.ArrayList;
import java.util.List;

public class BenList extends BenElement {
    private List<BenElement> list;
    private int length;

    public BenList() {
        list = new ArrayList<>();
        length = 0;
    }

    public BenList(List<BenElement> list, int length) {
        this.list = list;
        this.length = length;
    }

    public void add(BenElement element){
        list.add(element);
        length += element.getLength();
    }

    public List<BenElement> getList() {
        return list;
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
        return list.toString();
    }
}
