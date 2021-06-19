package com.eloli.ocsftp.core.hasher.implementations;

public class SHA512SaltHasherTool extends SHA512HasherTool {
    public SHA512SaltHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public boolean needSalt() {
        return true;
    }
}
