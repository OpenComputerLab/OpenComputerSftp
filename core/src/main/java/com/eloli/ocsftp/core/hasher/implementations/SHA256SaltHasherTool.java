package com.eloli.ocsftp.core.hasher.implementations;

public class SHA256SaltHasherTool extends SHA256HasherTool {
    public SHA256SaltHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public boolean needSalt() {
        return true;
    }
}
