package com.fei.indexbar.util;


import com.fei.indexbar.model.IndexBean;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Comparator;

/**
 * 使用的 pinyin4j-2.5.0.jar
 */
public class PinyinUtils {

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     * @param inputString
     * @return
     */
    public static String getPinYin(String inputString){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String outPut = "";
        try {
            for (char curchar : input){
                if (Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")){
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    outPut += temp[0];
                }else {
                    outPut += Character.toString(curchar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return outPut;
    }

    /**
     * 汉字转换为汉语拼音首字母，英文字符不变
     * @param chinese
     * @return
     */
    public static String getFirstSpell(String chinese){
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char curchar : arr){
            if (curchar > 128){
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else {
                pybf.append(curchar);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 按拼音进行排序 a,b,c,d,e...z
     */
    public static class ComparatorPY implements Comparator<IndexBean> {
        @Override
        public int compare(IndexBean lhs, IndexBean rhs) {
            String str1 = lhs.getLetter();
            String str2 = rhs.getLetter();
            return str1.compareToIgnoreCase(str2);
        }
    }
}
