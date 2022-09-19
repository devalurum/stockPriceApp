package ru.devalurum.tinkoffstockapp.utils;

import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {

    public static String convertStrBySplitWithCommaToUnmarkedList(@NotNull String str) {
        return Arrays.stream(str.split(", "))
                .map(String::trim)
                .map(s -> "• " + s + ".\n")
                .collect(Collectors.joining());
    }

    public static DateTimeFormatter DateTimeFormatterWithPattern(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }


    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
