package com.acrobat.shiro.utils;

import java.util.Random;

/**
 * @author xutao
 * @date 2018-11-29 14:24
 */
public class RandomStringUtil {

    private static final String STRING 			= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_STRING 	= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzàèéìòù,;.:-_ç@°#([{}])+*/|!£$%&/()=?^~¡¢€£¤¥¦§¨©ª«¬­®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
    private static Random random = new Random();

    /**
     * Generate a string with random chars, numbers and, eventually, special chars (e.g. punctuation chars).
     * This random generator is based on {@link Random}.
     *
     * <p>The invocation of this method is equivalent to {@code getRandomString(length, false)}.</p>
     *
     * @param length specify the length of the result string
     * @return a {@link String} with random alphanumeric content
     *
     * @see #getRandomString(int, boolean)
     */
    public static String getRandomString(int length) {
        return getRandomString(length, false);
    }

    /**
     * Generate a string with random chars, numbers and, eventually, special chars (e.g. punctuation chars).
     * This random generator is based on {@link Random}.
     *
     * @param length specify the length of the result string
     * @param specialChars if true returns a string containing special chars, false otherwise
     * @return a {@link String} with random alphanumeric content
     */
    public static String getRandomString(int length, boolean specialChars) {
        StringBuilder sb = new StringBuilder(length);
        String string = (specialChars) ? SPECIAL_STRING : STRING;
        int size = string.length();

        for(int i = 0; i < length; i++)
            sb.append(string.charAt(random.nextInt(size)));

        return sb.toString();
    }


    /**
     * Generate a string with random chars, numbers and, eventually, special chars (e.g. punctuation chars).
     * It is possible to avoid some chars in the result string.
     * This random generator is based on {@link Random}.
     *
     * @param length specify the length of the result string
     * @param specialChars if true returns a string containing special chars, false otherwise
     * @param avoidChars string of chars that will not be used in the result string
     * @return a {@link String} with random alphanumeric content
     */
    public static String getRandomString(int length, boolean specialChars, String avoidChars) {
        StringBuilder sb = new StringBuilder(length);
        String string = (specialChars) ? SPECIAL_STRING : STRING;
        int size = string.length();

        for(int i = 0; i < length; i++) {
            char ch = string.charAt(random.nextInt(size));
            if (avoidChars.contains(String.valueOf(ch)))
                i--;
            else
                sb.append(ch);
        }

        return sb.toString();
    }
}
