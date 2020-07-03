package com.acrobat.shiro.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xutao
 * @date 2018-12-05 16:36
 */
public class SessionAttributeKeys {

    public static final String KEY_KICKOUT = "kickout";

    private static Set<String> keys = new HashSet<>();

    static {
        keys.add(KEY_KICKOUT);
    }

    public static Set<String> getAllKeys() {
        return keys;
    }
}
