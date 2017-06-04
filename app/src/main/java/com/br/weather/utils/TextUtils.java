package com.br.weather.utils;

public class TextUtils {

    public static String capitalize(final String line) {

        String[] parts = line.split(" ");

        StringBuilder sb = new StringBuilder();

        for (String s : parts) {
            sb.append(Character.toUpperCase(s.charAt(0)));
            sb.append(s.substring(1));
            sb.append(" ");
        }

        return sb.toString();
    }
}
