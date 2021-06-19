package com.eloli.ocsftp.core.hasher.implementations;

public class SHA224SaltHasherTool extends SHA224HasherTool {
    public SHA224SaltHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public boolean needSalt() {
        return true;
    }
}
