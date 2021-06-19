package com.eloli.ocsftp.core.config;

import com.eloli.sodioncore.config.Configure;
import com.eloli.sodioncore.config.Lore;
import com.google.gson.annotations.Expose;

public class MainConfigure extends Configure {
    @Expose(deserialize = false)
    public int version = 0;

    @Lore("The path to opencomputer data")
    @Lore("You should make sure OpenComputer's config \"opencomputers.filesystem.bufferChanges\" is false.")
    @Expose
    public String dataPath = "@ required @";

    @Expose
    public ServerConfigure server = new ServerConfigure();

    public static class ServerConfigure extends Configure{
        @Lore("Build-in sftp server listen host")
        @Expose
        public String host = "0.0.0.0";

        @Lore("Build-in sftp server listen port")
        @Expose
        public int port = 2020;

        @Lore("Sftp host showed when player use /ocsftp")
        @Expose
        public String publicHost = "Minecraft server address";

        @Lore("Port host showed when player use /ocsftp")
        @Expose
        public int publicPort = 2020;
    }

    @Expose
    public PasswordConfigure password = new PasswordConfigure();

    public static class PasswordConfigure extends Configure{
        @Lore("Encrypt method, possible value")
        @Lore("BCrypt MD5 MD5Salt Plain SHA1 SHA1Salt")
        @Lore("SHA224 SHA224Salt SHA256 SHA256Salt")
        @Lore("SHA384 SHA384Salt SHA512 SHA512Salt")
        @Expose
        public String hasher = "BCrypt";

        @Lore("Encrypt salts")
        @Expose
        public int saltLength = 6;
    }
}
