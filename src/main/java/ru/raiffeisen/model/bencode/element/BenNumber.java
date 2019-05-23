package ru.raiffeisen.model.bencode.element;

import ru.raiffeisen.model.bencode.BenElement;

public class BenNumber extends BenElement {
    private int value;
    private int length;

    public BenNumber(int value, int length){
        this.value = value;
        this.length = length;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
