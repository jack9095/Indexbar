package com.fei.indexbar.model;

import java.io.Serializable;

public class IndexBean implements Serializable {

    public boolean isSelect;
    public String str;

    public IndexBean(String str) {
        this.str = str;
    }
}
