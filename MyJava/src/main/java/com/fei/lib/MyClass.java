package com.fei.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MyClass {

    public static void main(String[] args) {

        String[] array = {"☆","A","b","c","d","e","f","g","h","i","j","k","l","m","n",
                "o","p","q","r","s","t","u","v","w","x","y","Z"};
        for (int i = 0; i < array.length; i++) {
            if (i % 4 == 0 || i == 0 || i == array.length - 1) {
//                System.out.println(array[i]);
            }
        }

//        for (int i = 0; i < 26; i++) {
//            if (i % 4 == 0 || i == 0 || i ==25) {
//                System.out.println(array[i]);
//            }
//        }

        for (String s : filterAlphabet(array)) {
            System.out.print(s + ", ");
        }

    }

    /**
     * 正则过滤出字符串字母
     * @param alph
     * @return
     */
    public static String filterAlphabet(String alph) {
        alph = alph.replaceAll("[^(A-Za-z)]", "");
        return alph;
    }

    /**
     * 正则过滤出字数组母
     * @param array
     * @return
     */
    public static List<String> filterAlphabet(String[] array) {
        List<String> lists = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-Z]");
        for (int i = 0; i < array.length; i++) {
            //如果包含英文字母我这边不做处理,如果有需求,可以自己添加
            if(p.matcher(array[i]).find()) {
                lists.add(array[i]);
            }
        }
        return lists;
    }

}