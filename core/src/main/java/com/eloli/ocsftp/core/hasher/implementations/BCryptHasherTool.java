package com.eloli.ocsftp.core.hasher.implementations;

import com.eloli.ocsftp.core.hasher.HasherTool;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptHasherTool extends HasherTool {
    public BCryptHasherTool(int saltLength) {
        super(saltLength);
    }

    @Override
    public String hash(String data) {
        return BCrypt.hashpw(data, BCrypt.gensalt());
    }

    @Override
    public boolean verify(String hash, String data) {
        return BCrypt.checkpw(data, hash);
    }
}
