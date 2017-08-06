package com.sfebiz.supplychain.provider.command.common;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * 中文转拼音工具类
 * Created by zhangdi on 2015/12/2.
 */
public class PinyinUtil {

    public static final String SEPARATOR = "_&_";

    public static String convertToPinyin(String hanzi) {
        if (StringUtils.isNotBlank(hanzi)) {
            hanzi = hanzi.replace(" ", "");
        }
        String tempStr = PinyinHelper.convertToPinyinString(hanzi, SEPARATOR, PinyinFormat.WITHOUT_TONE);
        String[] arr = tempStr.split(SEPARATOR);
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            if (s.length() > 1) {
                sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
            } else if (s.length() == 1) {
                sb.append(Character.toUpperCase(s.charAt(0))).append(" ");
            } else {
                continue;
            }
        }

        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static void main(String args[]) {
        int a = 10;
        System.out.print(convertToPinyin("南京市鼓楼区萨家湾46号   长江学院"));
        System.out.print("HaHa");
        a++;

        System.out.print(convertToPinyin("%^&*#,"));
        a++;

        System.out.print(convertToPinyin("，。！#"));
        a++;
    }
}
