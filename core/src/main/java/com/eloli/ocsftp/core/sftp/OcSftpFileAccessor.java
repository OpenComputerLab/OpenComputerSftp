package com.eloli.ocsftp.core.sftp;

import com.eloli.ocsftp.core.OcSftpCore;
import org.apache.sshd.common.util.buffer.Buffer;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.sftp.server.DirectoryHandle;
import org.apache.sshd.sftp.server.SftpFileSystemAccessor;
import org.apache.sshd.sftp.server.SftpSubsystemProxy;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.security.Principal;
import java.util.*;
import java.util.regex.Pattern;

public class OcSftpFileAccessor implements SftpFileSystemAccessor {
    public final static Set<String> fillable = new HashSet<>();
    public final static Map<String, Object> fileAttributes = new HashMap<>();
    public final static Map<String, Object> directoryAttributes = new HashMap<>();
    public final static Map<String, Object> globalAttributes = new HashMap<>();
    static {
        fillable.add("lastAccessTime");
        fillable.add("lastModifiedTime");
        fillable.add("creationTime");
        fillable.add("size");
        fillable.add("isDirectory");

        Set<PosixFilePermission> permissions = new HashSet<>();

        permissions.add(PosixFilePermission.OWNER_EXECUTE);
        permissions.add(PosixFilePermission.OWNER_WRITE);
        permissions.add(PosixFilePermission.OWNER_READ);
        permissions.add(PosixFilePermission.GROUP_EXECUTE);
        permissions.add(PosixFilePermission.GROUP_WRITE);
        permissions.add(PosixFilePermission.GROUP_READ);
        permissions.add(PosixFilePermission.OTHERS_EXECUTE);
        permissions.add(PosixFilePermission.OTHERS_WRITE);
        permissions.add(PosixFilePermission.OTHERS_READ);

        globalAttributes.put("isRegularFile", false);
        globalAttributes.put("isSymbolicLink",false);
        globalAttributes.put("permissions",permissions);
        globalAttributes.put("size", 0L);
        globalAttributes.put("lastModifiedTime", FileTime.fromMillis(0));
        globalAttributes.put("lastAccessTime", FileTime.fromMillis(0));
        globalAttributes.put("creationTime", FileTime.fromMillis(0));

        fileAttributes.putAll(globalAttributes);
        fileAttributes.put("isDirectory", false);

        directoryAttributes.putAll(globalAttributes);
        directoryAttributes.put("isDirectory", true);
    }

    public static NavigableMap<String, Object> overrideAttributes(Map<String, Object> source){
        NavigableMap<String, Object> result = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        result.put("owner","OCLserver");
        result.put("group","OCLserver");
        result.putAll(globalAttributes);
        source.forEach((k,v)->{
            if(fillable.contains(k)){
                result.put(k,v);
            }
        });
        return result;
    }
    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}");
    public static boolean isUuid(String uuid){
        return UUID_PATTERN.matcher(uuid).matches();
    }

    protected final OcSftpCore core;
    public OcSftpFileAccessor(OcSftpCore core){
        this.core = core;
    }

    @Override
    public Map<String, ?> readFileAttributes(
            ServerSession session, SftpSubsystemProxy subsystem,
            Path file, String view, LinkOption... options)
            throws IOException {
        return overrideAttributes(Files.readAttributes(file, view, options));
    }

    @Override
    public LinkOption[] resolveFileAccessLinkOptions(ServerSession session, SftpSubsystemProxy subsystem, Path file, int cmd, String extension, boolean followLinks) throws IOException {
        return new LinkOption[]{};
    }

    @Override
    public void setFileAttribute(ServerSession session, SftpSubsystemProxy subsystem, Path file, String view, String attribute, Object value, LinkOption... options) throws IOException {
        // throw new UnsupportedOperationException();
    }

    @Override
    public void setFileOwner(ServerSession session, SftpSubsystemProxy subsystem, Path file, Principal value, LinkOption... options) throws IOException {
        // throw new UnsupportedOperationException();
    }

    @Override
    public void setGroupOwner(ServerSession session, SftpSubsystemProxy subsystem, Path file, Principal value, LinkOption... options) throws IOException {
        // throw new UnsupportedOperationException();
    }

    @Override
    public void createLink(ServerSession session, SftpSubsystemProxy subsystem, Path link, Path existing, boolean symLink) throws IOException {
        // throw new UnsupportedOperationException();
    }
}
