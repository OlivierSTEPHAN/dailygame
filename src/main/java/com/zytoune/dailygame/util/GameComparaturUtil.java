package com.zytoune.dailygame.util;

import io.micrometer.common.util.StringUtils;

import java.text.Normalizer;

public class GameComparaturUtil {

    public static boolean compareGameNames(String name1, String name2) {
        if(StringUtils.isBlank(name1) || StringUtils.isBlank(name2)) {
            return false;
        }

        // Normalize and remove accents
        String normalized1 = normalizeAndRemoveAccents(name1);
        String normalized2 = normalizeAndRemoveAccents(name2);

        // Remove non-alphanumeric characters and trim
        String cleanName1 = normalized1.replaceAll("[^a-zA-Z0-9]", "").trim();
        String cleanName2 = normalized2.replaceAll("[^a-zA-Z0-9]", "").trim();

        if(cleanName1.isEmpty() || cleanName2.isEmpty()) {
            return false;
        }

        if(cleanName2.contains(cleanName1) || cleanName1.contains(cleanName2)) {
            return true;
        }

        return cleanName1.equalsIgnoreCase(cleanName2);
    }

    private static String normalizeAndRemoveAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

}
