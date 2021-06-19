package com.eloli.ocsftp.core.hasher.implementations;

import com.eloli.ocsftp.core.hasher.HasherTool;

public class PlainHasherTool extends HasherTool {
    public PlainHasherTool(int saltLength) {
        super(saltLength);
    }

    public boolean verify(String hash, String data) {
        return hash(data).equals(hash);
    }
}
