package com.fei.indexbar.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class IndexBean implements Serializable {

    private boolean isSelect; // 是否选中
    private String letter; // 首字母
    private String name;  // 标题
    private String nameFirst;  // 标题第一个文字
    private List<String> lists;

    private String id; // 唯一标识符

    public IndexBean(String letter) {
        this.letter = letter;
    }

    public IndexBean(boolean isSelect, String letter, String name, String nameFirst) {
        this.isSelect = isSelect;
        this.letter = letter;
        this.name = name;
        this.nameFirst = nameFirst;
    }

    public IndexBean(String letter, String name, String nameFirst) {
        this.letter = letter;
        this.name = name;
        this.nameFirst = nameFirst;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    /**
     * 按拼音进行排序 a,b,c,d,e...z
     */
    public static class ComparatorLetter implements Comparator<IndexBean> {
        @Override
        public int compare(IndexBean lhs, IndexBean rhs) {
            String str1 = lhs.letter;
            String str2 = rhs.letter;
            return str1.compareToIgnoreCase(str2);
        }
    }
}
