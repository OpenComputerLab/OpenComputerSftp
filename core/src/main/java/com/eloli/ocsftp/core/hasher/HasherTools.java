package com.eloli.ocsftp.core.hasher;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.core.hasher.implementations.*;

import java.util.HashMap;
import java.util.Map;

public class HasherTools {
    private Map<String, HasherTool> hasherTools;
    private OcSftpCore core;
    public HasherTools(OcSftpCore core) {
        this.core = core;
        int saltLength = core.config.password.saltLength;
        hasherTools = new HashMap<>();
        hasherTools.put("BCrypt", new BCryptHasherTool(saltLength));
        hasherTools.put("MD5", new MD5HasherTool(saltLength));
        hasherTools.put("MD5Salt", new MD5SaltHasherTool(saltLength));
        hasherTools.put("Plain", new PlainHasherTool(saltLength));
        hasherTools.put("SHA1", new SHA1HasherTool(saltLength));
        hasherTools.put("SHA1Salt", new SHA1SaltHasherTool(saltLength));
        hasherTools.put("SHA224", new SHA224HasherTool(saltLength));
        hasherTools.put("SHA224Salt", new SHA224SaltHasherTool(saltLength));
        hasherTools.put("SHA256", new SHA256HasherTool(saltLength));
        hasherTools.put("SHA256Salt", new SHA256SaltHasherTool(saltLength));
        hasherTools.put("SHA384", new SHA384HasherTool(saltLength));
        hasherTools.put("SHA384Salt", new SHA384SaltHasherTool(saltLength));
        hasherTools.put("SHA512", new SHA512HasherTool(saltLength));
        hasherTools.put("SHA512Salt", new SHA512SaltHasherTool(saltLength));
    }

    public HasherTool getByName(String name) {
        return hasherTools.get(name);
    }

    public HasherTool getDefault() {
        return getByName(core.config.password.hasher);
    }
}
