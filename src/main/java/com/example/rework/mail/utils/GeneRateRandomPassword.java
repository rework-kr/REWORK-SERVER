package com.example.rework.mail.utils;

import java.util.Random;

public class GeneRateRandomPassword {
    public static String getRandomPassword() {
        // 알파벳과 특수문자
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+";

        Random r = new Random();
        StringBuilder password = new StringBuilder();

        // 4자리의 알파벳과 특수문자 추가
        for (int i = 0; i < 4; i++) {
            password.append(characters.charAt(r.nextInt(characters.length())));
        }

        // 6자리의 숫자 추가
        for (int i = 0; i < 6; i++) {
            password.append(r.nextInt(10));
        }

        // 생성된 문자열을 무작위로 섞음
        for (int i = password.length() - 1; i > 0; i--) {
            int index = r.nextInt(i + 1);
            char temp = password.charAt(index);
            password.setCharAt(index, password.charAt(i));
            password.setCharAt(i, temp);
        }

        return password.toString();
    }
}
