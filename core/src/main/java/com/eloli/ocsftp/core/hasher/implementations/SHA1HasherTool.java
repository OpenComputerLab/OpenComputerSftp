package com.eloli.ocsftp.core.hasher.implementations;

import com.eloli.ocsftp.core.hasher.HasherTool;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1HasherTool extends HasherTool {
    public SHA1HasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public String hash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
