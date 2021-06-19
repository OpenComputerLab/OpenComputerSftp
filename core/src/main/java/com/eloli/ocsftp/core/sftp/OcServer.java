package com.eloli.ocsftp.core.sftp;

import com.eloli.ocsftp.core.OcSftpCore;
import com.eloli.ocsftp.core.entities.MountDisk;
import com.eloli.ocsftp.core.entities.SftpUser;
import com.eloli.ocsftp.core.repositories.MountDiskRepository;
import com.eloli.ocsftp.core.repositories.SftpUserRepository;
import org.apache.sshd.common.AttributeRepository;
import org.apache.sshd.common.config.keys.AuthorizedKeyEntry;
import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.file.root.RootedFileSystem;
import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.session.SessionContext;
import org.apache.sshd.server.ServerBuilder;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpFileSystemAccessor;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.*;

public class OcServer {
    public static AttributeRepository.AttributeKey<String> ROOT_DIR_KEY = new AttributeRepository.AttributeKey<>();

    private final OcSftpCore core;

    private final SshServer sshServer;
    private final SftpSubsystemFactory sftpSubsystemFactory;
    public OcServer(OcSftpCore core) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.core = core;

        sshServer = ServerBuilder.builder().build();

        sftpSubsystemFactory = new SftpSubsystemFactory();
        sftpSubsystemFactory.setFileSystemAccessor(SftpFileSystemAccessor.DEFAULT);

        sftpSubsystemFactory.setFileSystemAccessor(new OcSftpFileAccessor(core));

        sshServer.setSubsystemFactories(Collections.singletonList(sftpSubsystemFactory));

        sshServer.setHost(core.config.server.host);
        sshServer.setPort(core.config.server.port);

        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get(
                core.adapter.getBaseFile().getConfigPath("host.key")
        )));

        sshServer.setPasswordAuthenticator(
                (username, password, session) -> {
                    try(Session dbsession = core.sessionFactory.openSession()){
                        String[] usernames = username.split("\\.");
                        if(usernames.length != 2){
                            return false;
                        }
                        dbsession.beginTransaction();
                        SftpUser user = SftpUserRepository.getByName(dbsession,usernames[0]);
                        if(user != null
                                && user.getPassword() != null
                                && core.hasherTools.getDefault().verify(user.getPassword(),password)){
                            MountDisk mountDisk = MountDiskRepository.getByUserAndName(dbsession,user,usernames[1]);
                            if(mountDisk == null){
                                return false;
                            }
                            session.setAttribute(ROOT_DIR_KEY,mountDisk.getUuid().toString());
                            return true;
                        }
                    }
                    return false;
                });
        sshServer.setPublickeyAuthenticator(
                (username, key, session) -> {
                    try(Session dbsession = core.sessionFactory.openSession()){
                        String[] usernames = username.split("\\.");
                        if(usernames.length != 2){
                            return false;
                        }
                        dbsession.beginTransaction();
                        SftpUser user = SftpUserRepository.getByName(dbsession,usernames[0]);
                        if(user != null
                                && user.getSshKey() != null){
                            InputStream publicKeyStream = user.getSshKey().getBinaryStream();
                            byte[] publicKeyBytes = new byte[publicKeyStream.available()];
                            publicKeyStream.read(publicKeyBytes);
                            AuthorizedKeyEntry authorizedKeyEntry = AuthorizedKeyEntry.parseAuthorizedKeyEntry(
                                    new String(publicKeyBytes, StandardCharsets.UTF_8)
                            );
                            if(authorizedKeyEntry == null){
                                return false;
                            }
                            PublicKey publicKey = authorizedKeyEntry.resolvePublicKey(null,null);
                            if(publicKey == null){
                                return false;
                            }
                            if(!KeyUtils.compareKeys(publicKey,key)){
                                return false;
                            }
                            MountDisk mountDisk = MountDiskRepository.getByUserAndName(dbsession,user,usernames[1]);
                            if(mountDisk == null){
                                return false;
                            }
                            session.setAttribute(ROOT_DIR_KEY,mountDisk.getUuid().toString());
                            return true;
                        }
                    } catch (SQLException |GeneralSecurityException | IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                });

        sshServer.setFileSystemFactory(new FileSystemFactory() {
            final Path rootPath = Paths.get(core.config.dataPath);
            final HashMap<String, RootedFileSystem> fileSystems = new HashMap<>();
            @Override
            public Path getUserHomeDir(SessionContext session) throws IOException {
                return fileSystems.get(session.getAttribute(ROOT_DIR_KEY)).getPath(".");
            }

            @Override
            public FileSystem createFileSystem(SessionContext session) throws IOException {
                String rootDir = session.getAttribute(ROOT_DIR_KEY);
                if(fileSystems.containsKey(rootDir)){
                    return fileSystems.get(rootDir);
                }
                Path dir = Paths.get(core.config.dataPath).resolve(rootDir);
                if(!dir.toFile().exists()){
                    dir.toFile().mkdirs();
                }
                RootedFileSystem rfs = (RootedFileSystem) new RootedFileSystemProvider()
                        .newFileSystem(dir, Collections.emptyMap());
                fileSystems.put(rootDir,rfs);
                return rfs;
            }
        });
    }

    public void init() throws IOException {
        sshServer.open();
    }
}
