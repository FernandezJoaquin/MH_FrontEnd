package com.mindhub.homebanking.utils;

public final class CardUtils {
    private static String random(int min, int max) {
        int rando = (int) ((Math.random() * (max - min)) + min);
        return Integer.toString(rando);
    }

    public static String getCardNumber() {
        String cardNumber;
        cardNumber = random(1000, 9999) + "-" + random(1000, 9999) + "-" + random(1000, 9999) + "-" + random(1000, 9999);
        return cardNumber;
    }

    public static String getCvv() {
        return random(100, 999);
    }
}
