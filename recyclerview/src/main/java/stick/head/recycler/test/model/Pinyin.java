package stick.head.recycler.test.model;

/**
 * Created by yu_longji on 2015/9/1.
 */

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by yu_longji on 2015/8/31.
 */
public class Pinyin {

    public static String makeStringByStringSet(Set<String> stringSet) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String s : stringSet) {
            if (i == stringSet.size() - 1) {
                str.append(s);
            } else {
                str.append(s + ",");
            }
            i++;
        }
        return str.toString().toLowerCase();
    }

    /**
     * 获取拼音集合
     *
     */
    public static Set<String> getPinyin(String src) {
        if (src != null && !src.trim().equalsIgnoreCase("")) {
            char[] srcChar;
            srcChar = src.toCharArray();
            //汉语拼音格式输出类
            HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();

            //输出设置，大小写，音标方式等
            hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
            String[][] temp = new String[src.length()][];
            for (int i = 0; i < srcChar.length; i++) {
                char c = srcChar[i];
                //是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
                if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
                    try {
                        temp[i] = PinyinHelper.toHanyuPinyinStringArray(srcChar[i], hanYuPinOutputFormat);
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                } else if (((int) c >= 65 && (int) c <= 90) || ((int) c >= 97 && (int) c <= 122)) {
                    temp[i] = new String[]{String.valueOf(srcChar[i])};
                } else {
                    temp[i] = new String[]{""};
                }
            }
            String[] pingyinArray = Exchange(temp);

            Set<String> pinyinSet = new HashSet<String>();
            for (int i = 0; i < pingyinArray.length; i++) {
                pinyinSet.add(pingyinArray[i]);
            }
            return pinyinSet;
        }
        return null;
    }

    /**
     * 递归
     *
     */
    public static String[] Exchange(String[][] strJaggedArray) {
        String[][] temp = DoExchange(strJaggedArray);
        return temp[0];
    }

    /**
     * 递归
     *
     */
    private static String[][] DoExchange(String[][] strJaggedArray) {
        int len = strJaggedArray.length;
        if (len >= 2) {
            int len1 = strJaggedArray[0].length;
            int len2 = strJaggedArray[1].length;
            int newlen = len1 * len2;
            String[] temp = new String[newlen];
            int Index = 0;
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
                    Index++;
                }
            }
            String[][] newArray = new String[len - 1][];
            for (int i = 2; i < len; i++) {
                newArray[i - 1] = strJaggedArray[i];
            }
            newArray[0] = temp;
            return DoExchange(newArray);
        } else {
            return strJaggedArray;
        }
    }











//    /** * 汉字转成ASCII码 * * @param chs * @return */
//    private int getChsAscii(String chs) {
//        int asc = 0;
//        try {
//            byte[] bytes = chs.getBytes("gb2312");
//            if (bytes == null || bytes.length > 2 || bytes.length <= 0) {
//                throw new RuntimeException("illegal resource string");
//            }
//            if (bytes.length == 1) {
//                asc = bytes[0];
//            }
//            if (bytes.length == 2) {
//                int hightByte = 256 + bytes[0];
//                int lowByte = 256 + bytes[1];
//                asc = (256 * hightByte + lowByte) - 256 * 256;
//            }
//        } catch (Exception e) {
//            System.out.println("ERROR:ChineseSpelling.class-getChsAscii(String chs)" + e);
//        }
//        return asc;
//    }
//
//    public String getSellingWithPolyphone(String chs){
//        if(polyphoneMap != null && polyphoneMap.isEmpty()){
//            polyphoneMap = initDictionary();
//        }
//
//        String key, value, resultPy = null;
//        buffer = new StringBuilder();
//        for (int i = 0; i < chs.length(); i++) {
//            key = chs.substring(i, i + 1);
//            if (key.getBytes().length >= 2) {
//                value = (String) convert(key);
//                if (value == null) {
//                    value = "unknown";
//                }
//            } else {
//                value = key;
//            }
//            resultPy = value;
//
//            String left = null;
//            if(i>=1 && i+1 <= chs.length()){
//                left = chs.substring(i-1,i+1);
//                if(polyphoneMap.containsKey(value) && polyphoneMap.get(value).contains(left)){
//                    resultPy = value;
//                }
//            }
////    if(chs.contains("重庆")){
//            String right = null; //向右多取一个字,例如 [长]沙
//            if(i<=chs.length()-2){
//                right = chs.substring(i,i+2);
//                if(polyphoneMap.containsKey(right)){
//                    resultPy = polyphoneMap.get(right);
//                }
//            }
////    }
//
//            String middle = null; //左右各多取一个字,例如 龙[爪]槐
//            if(i>=1 && i+2<=chs.length()){
//                middle = chs.substring(i-1,i+2);
//                if(polyphoneMap.containsKey(value) && polyphoneMap.get(value).contains(middle)){
//                    resultPy = value;
//                }
//            }
//
//            String left3 = null; //向左多取2个字,如 芈月[传],列车长
//            if(i>=2 && i+1<=chs.length()){
//                left3 = chs.substring(i-2,i+1);
//                if(polyphoneMap.containsKey(value) && polyphoneMap.get(value).contains(left3)){
//                    resultPy = value;
//                }
//            }
//
//            String right3 = null; //向右多取2个字,如 [长]孙无忌
//            if(i<=chs.length()-3){
//                right3 = chs.substring(i,i+3);
//                if(polyphoneMap.containsKey(value) && polyphoneMap.get(value).contains(right3)){
//                    resultPy = value;
//                }
//            }
//
//            buffer.append(resultPy);
//        }
//        return buffer.toString();
//    }
//
//    public HashMap<String, String> initDictionary(){
//        String fileName = "py4j.dic";
//        InputStreamReader inputReader = null;
//        BufferedReader bufferedReader = null;
//        HashMap<String, String> polyphoneMap = new HashMap<String, String>();
//        try{
//            inputReader = new InputStreamReader(MyApplication.mContext.getResources().getAssets().open(fileName),"UTF-8");
//            bufferedReader = new BufferedReader(inputReader);
//            String line = null;
//            while((line = bufferedReader.readLine()) != null){
//                String[] arr = line.split(PINYIN_SEPARATOR);
//                if(isNotEmpty(arr[1])){
//                    String[] dyzs = arr[1].split(WORD_SEPARATOR);
//                    for(String dyz: dyzs){
//                        if(isNotEmpty(dyz)){
//                            polyphoneMap.put(dyz.trim(),arr[0]);
//                        }
//                    }
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            if(inputReader != null){
//                try {
//                    inputReader.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if(bufferedReader != null){
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        return polyphoneMap;
//    }

}