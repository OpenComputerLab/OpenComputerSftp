package com.eloli.ocsftp.core.hasher;

import java.util.Random;

public abstract class HasherTool {
    protected final int saltLength;

    public HasherTool(int saltLength) {
        this.saltLength = saltLength;
    }

    public String hash(String data) {
        return data;
    }

    public String hash(String data, String salt) {
        return hash(hash(data) + salt);
    }

    public boolean verify(String hash, String data) {
        if (needSalt()) {
            String salt = hash.substring(0, hash.indexOf("$"));
            return hash(data + salt).equalsIgnoreCase(hash.substring(hash.indexOf("$")));
        } else {
            return hash(data).equalsIgnoreCase(hash);
        }
    }

    public boolean needSalt() {
        return false;
    }

    public String generateSalt() {
        @SuppressWarnings("SpellCheckingInspection")
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random1 = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < saltLength; i++) {
            int number = random1.nextInt(str.length());
            char charAt = str.charAt(number);
            sb.append(charAt);
        }
        return String.valueOf(sb);
    }
}
