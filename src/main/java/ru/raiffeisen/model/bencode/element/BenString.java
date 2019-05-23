package ru.raiffeisen.model.bencode.element;

import ru.raiffeisen.model.bencode.BenElement;

public class BenString extends BenElement {
    private String value;
    private int length;

    public BenString(String value, int length) {
        this.value = value;
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return value;
    }
}
