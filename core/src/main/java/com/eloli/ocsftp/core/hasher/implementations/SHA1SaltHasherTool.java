package com.eloli.ocsftp.core.hasher.implementations;

public class SHA1SaltHasherTool extends SHA1HasherTool {
    public SHA1SaltHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public boolean needSalt() {
        return true;
    }
}
