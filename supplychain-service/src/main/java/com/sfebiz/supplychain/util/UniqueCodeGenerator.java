package com.sfebiz.supplychain.util;

import com.sfebiz.common.utils.generator.AutoTuningLRUCache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>业务单据唯一编码生成器</p>
 * User: <a href="mailto:yanmingming1989@163.com">严明明</a>
 * Date: 14/12/21
 * Time: 下午12:12
 */
public class UniqueCodeGenerator {

    //缓存最近生成的100个结尾4位序列码
    private static AutoTuningLRUCache<String, String> purchaseCodeCache = new AutoTuningLRUCache<String, String>("purchaseCodeCache", 100);

    //防止重复的序列号
    private static volatile long sequence = 90000;


    private static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    /**
     * 生成业务单据唯一编码
     *
     * @return
     */
    public static String generate(String prefix) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        StringBuffer code = new StringBuffer(prefix);
        Date date = new Date();
        String middleCode = simpleDateFormat.format(date);
        code.append(middleCode);
        code.append(getUniquCode());
        return code.toString();
    }

    public static String toRadix(long data, int radix) {
        char buf[] = new char[33];
        int charPos = 32;
        while (data >= radix) {
            String modStr = String.valueOf(data % radix);
            buf[charPos--] = digits[Integer.valueOf(modStr)];
            data = data / radix;
        }
        String lastMod = String.valueOf(data);
        buf[charPos] = digits[Integer.valueOf(lastMod)];

        return new String(buf, charPos, (33 - charPos));
    }

    /**
     * 获取4位流水码
     *
     * @return
     */
    public static String getUniquCode() {
        int bit = 4;
        String key = toRadix(generateId(), digits.length);
        String perfix = "";
        for (int i = 0; i < bit - key.length(); i++) {
            perfix = perfix + "0";
        }
        if (key.length() > bit) {
            key = key.substring(key.length() - bit, key.length());
        }
        String endCode = perfix + key;
        int repeatCount = 1;
        while (purchaseCodeCache.containsKey(endCode)) {
            endCode = getUniquCode();
            if (repeatCount == 100) {
                break;
            }
            repeatCount++;
        }
        purchaseCodeCache.put(endCode, "");
        return endCode;
    }

    private static long generateId() {
        long id = getCurrentTotalSecond();
        if (sequence >= 99999) {
            sequence = 90000;
        } else {
            sequence = sequence + 1;
        }
        id = id + sequence;
        return id;
    }

    /**
     * 获取当前时间在当天中的总描述，最大值为 86400
     *
     * @return
     */
    private static long getCurrentTotalSecond() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long beginTime = today.getTimeInMillis() / 1000;
        today = Calendar.getInstance();
        long currentTime = today.getTimeInMillis() / 1000;
        return currentTime - beginTime;
    }

}
