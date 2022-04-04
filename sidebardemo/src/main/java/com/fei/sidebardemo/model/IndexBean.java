package com.fei.sidebardemo.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class IndexBean implements Serializable {

    /**
     * letter、name、id 这三个是必传的
     */
    private String letter; // 首字母
    private String name;  // 标题
    private String id; // 唯一标识符
    private boolean isImage; // 是图片
    private int resource; // 图片









    private boolean isSelect; // 是否选中
    private String nameFirst;  // 标题第一个文字
    private List<String> lists; // 省略符号对应的 字母集合

    public IndexBean(String letter, String name, String id) {
        this.id = id;
        this.name = name;
        this.letter = letter;
    }

    public IndexBean(String letter, String id) {
        this.id = id;
        this.letter = letter;
    }

    public IndexBean(String letter, String name, String id, boolean isImage, int resource) {
        this.id = id;
        this.name = name;
        this.letter = letter;
        this.isImage = isImage;
        this.resource = resource;
    }

    public IndexBean(boolean isSelect, String letter, String name, String nameFirst) {
        this.isSelect = isSelect;
        this.letter = letter;
        this.name = name;
        this.nameFirst = nameFirst;
    }

//    public IndexBean(String letter, String name, String nameFirst) {
//        this.letter = letter;
//        this.name = name;
//        this.nameFirst = nameFirst;
//    }

    public IndexBean(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
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

    public IndexBean setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
        return this;
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
