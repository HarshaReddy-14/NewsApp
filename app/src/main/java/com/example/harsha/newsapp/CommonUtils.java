package com.example.harsha.newsapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by harsha on 28/06/17.
 */

public class CommonUtils {

    public static boolean isNull(Object obj) {
        if (obj != null) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyString(String string) {
        if (string != null && !("".equalsIgnoreCase(string.trim())) && string.trim().length() > 0
                && !"null".equalsIgnoreCase(string.trim())) {
            return false;
        }
        return true;
    }

    public static boolean isEmptyStringArray(String[] stringArray) {
        if (stringArray == null || stringArray.length < 1) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyList(ArrayList<?> list) {
        if (list == null || list.isEmpty() || list.size() < 1) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyList(List<?> list) {
        if (list == null || list.isEmpty() || list.size() < 1) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyMap(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyMap(HashMap<?, ?> hashMap) {
        if (hashMap == null || hashMap.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyMap(TreeMap<?, ?> treeMap) {
        if (treeMap == null || treeMap.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(String email) {
        if (isEmptyString(email)) {
            return false;
        }
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
