package com.eloli.ocsftp.core.hasher.implementations;

public class SHA384SaltHasherTool extends SHA384HasherTool {
    public SHA384SaltHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public boolean needSalt() {
        return true;
    }
}
