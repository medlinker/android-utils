package com.medlinker.utils;

/**
 * @author <a href="mailto:tql@medlinker.net">tqlmorepassion</a>
 * @version 1.0
 * @description 功能描述
 * @time 2016/4/26 20:06
 */
public class EmojiUtil {

    /**
     * 判断一个字符串是否包含emoji表情
     *
     * @param str
     * @return
     */
    public static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 截取带有emoji的字符串，避免将emoji截断，如果截止位置是位于emoji表情之间，则往前或往后顺位
     * 例如：截取 前5个字符，substring（0，5），但是第五个字符是emoji的一部分，则自动转换为截取前6个字符substring（0，6）
     * 例如:截取 substring（1，5），但是第二个字符是emoji的一部分，则转换为substring（0，5）
     *
     * @param source
     * @param start
     * @param end
     * @return
     */
    public static String subString(String source, int start, int end) {
        if (start > end) {
            return source;
        }
        int indexStart = start;
        int indexEnd = end;
        if (indexStart != 0 && isEmojiCharacter(source.charAt(indexStart))) {
            indexStart--;
        }
        if (indexEnd < source.length() && isEmojiCharacter(source.charAt(indexEnd - 1))) {
            indexEnd++;
        }
        return source.substring(indexStart, indexEnd);
    }

}
