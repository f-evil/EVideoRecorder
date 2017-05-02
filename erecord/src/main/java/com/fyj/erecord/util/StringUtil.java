package com.fyj.erecord.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符工具类
 *
 * @author lmc
 * @version 创建时间：2015-7-4 下午2:15:11
 */
public class StringUtil {
    /**
     * 判断字符串是否为空
     *
     * @param str 输入字符
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return (null == str || "".equals(str.trim()) || "null".equals(str));
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 输入字符
     * @return 是否为空
     */
    public static String removeEmpty(String str,String defauleValue) {
        return isEmpty(str) ? defauleValue : str;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 输入字符
     * @return 是否为空
     */
    public static String removeEmpty(String str) {
        return isEmpty(str) ? "" : str;
    }

    /**
     * 截取最后一个字符@c后面的字符串
     *
     * @param str 输入字符串
     * @param c   判断字符字符
     * @return 截取后的字符串
     */
    public static String lastStringAfter(String str, char c) {
        return str.substring(str.lastIndexOf(c) + 1);
    }

    /**
     * 设置彩色文本
     *
     * @param tv        textview
     * @param formatStr 格式化字符串
     * @param params    需要变色的字符
     * @param colors    需要变换的颜色
     */
    public static void setColorText(TextView tv, String formatStr, Object[] params, int[] colors) {
        if (StringUtil.isEmpty(formatStr) || params.length != colors.length)
            return;
        String text = String.format(formatStr, params);
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        Pattern p = Pattern.compile("%[abcdefghosx]");
        Matcher m = p.matcher(formatStr);
        int i = 0;
        int begin = 0;
        int end = 0;
        while (m.find()) {
            begin += m.start() - end;
            spannable.setSpan(new ForegroundColorSpan(colors[i]), begin, begin + params[i].toString().length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            begin = begin + params[i].toString().length();
            end = m.end();
            i++;
        }
        tv.setText(spannable);
    }

    /**
     * 将时间数字转换为字符
     *
     * @param time 时间（s）
     * @return "xh xxm xxs"
     */
    public static String parseTimeString(int time) {
        String s = time % 60 + "s";
        String m = "";
        String h = "";
        time /= 60;
        if (time > 0) {
            m = time % 60 + "m ";
            time /= 60;
        }
        if (time > 0) {
            h = time + "h ";
        }
        return h + m + s;
    }

    /**
     * 在String中输出keyword的position合集
     *
     * @param regex keywords
     * @param text  text
     * @return ArrayList<Integer>
     */
    public static ArrayList<Integer> searchTextFromString(String regex, String text) {
        ArrayList<Integer> mList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            mList.add(matcher.start());
            mList.add(matcher.end());
        }
        return mList;
    }

    /**
     * MD5 转换
     *
     * @param s 输入
     * @return 输出
     */
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
