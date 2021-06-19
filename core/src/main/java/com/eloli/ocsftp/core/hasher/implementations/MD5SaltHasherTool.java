package com.eloli.ocsftp.core.hasher.implementations;

public class MD5SaltHasherTool extends MD5HasherTool {
    public MD5SaltHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public boolean needSalt() {
        return true;
    }
}
