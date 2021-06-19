package com.eloli.ocsftp.core;

import com.eloli.ocsftp.core.config.MainConfigure;
import com.eloli.ocsftp.core.entities.MountDisk;
import com.eloli.ocsftp.core.entities.SftpUser;
import com.eloli.ocsftp.core.hasher.HasherTools;
import com.eloli.ocsftp.core.repositories.MountDiskRepository;
import com.eloli.ocsftp.core.repositories.SftpUserRepository;
import com.eloli.ocsftp.core.sftp.OcServer;
import com.eloli.sodioncore.config.ConfigureService;
import com.eloli.sodioncore.logger.AbstractLogger;
import com.eloli.sodioncore.orm.AbstractSodionCore;
import com.eloli.sodioncore.orm.OrmService;
import com.eloli.sodioncore.orm.SodionEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OcSftpCore {
    public PlatformAdapter adapter;
    public AbstractSodionCore sodionCore;

    private OrmService ormService;
    private ConfigureService<MainConfigure> configureService;

    public AbstractLogger logger;

    public HasherTools hasherTools;
    public MainConfigure config;
    public SessionFactory sessionFactory;

    public OcServer ocServer;

    public OcSftpCore(PlatformAdapter adapter) throws IOException {
        this.adapter = adapter;

        adapter.getDependencyManager().checkDependencyMaven(
                "org{}mindrot:jbcrypt:0.4:org.mindrot.jbcrypt.BCrypt".replace("{}","."));
        adapter.getDependencyManager().checkDependencyMaven(
                "org{}apache{}sshd:sshd-sftp:2.7.0:org.apache.sshd.sftp.SftpModuleProperties".replace("{}","."));

        this.sodionCore = adapter.getSodionCore();
        this.ormService = sodionCore.getOrmService();
        List<Class<? extends SodionEntity>> sodionEntities = new ArrayList<>();
        sodionEntities.add(SftpUser.class);
        sodionEntities.add(MountDisk.class);
        ormService.addEntities(sodionEntities);

        this.configureService = new ConfigureService<>(
                adapter.getBaseFile(),
                "config.json"
        );
        configureService.register(null,MainConfigure.class);

        this.logger = adapter.getLogger();
    }

    public void init() throws Exception {
        configureService.init();
        config = configureService.instance;
        hasherTools = new HasherTools(this);
        sessionFactory = ormService.sessionFactory;
        this.ocServer = new OcServer(this);
        ocServer.init();
    }

    public void onMount(AbstractPlayer player,String name,UUID uuid){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            SftpUser user = SftpUserRepository.getByUuid(session,player.getUniqueId());
            if(user == null){
                player.sendMessage("Register first.");
                return;
            }
            List<MountDisk> mountDisks = MountDiskRepository.getByUserOfOne(session,user,name,uuid);
            if(!mountDisks.isEmpty()){
                player.sendMessage("Already have "+mountDisks.get(0).getName());
                return;
            }
            session.save(new MountDisk().setOwner(user).setName(name).setUuid(uuid));
            player.sendMessage("Mount successfully. Reconnect your sftp connection please.");

            session.getTransaction().commit();
        }
    }

    public void onRemove(AbstractPlayer player, String name) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SftpUser user = SftpUserRepository.getByUuid(session,player.getUniqueId());
            if(user == null){
                player.sendMessage("Register first.");
                return;
            }

            MountDisk mountDisk = MountDiskRepository.getByUserAndName(session,user,name);
            if(mountDisk == null){
                player.sendMessage("No such mount found");
                return;
            }
            session.remove(mountDisk);
            player.sendMessage("Remove successfully.");

            session.getTransaction().commit();
        }
    }

    public List<String> onTabDisks(AbstractPlayer player){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SftpUser user = SftpUserRepository.getByUuid(session,player.getUniqueId());
            if(user == null){
                return Collections.emptyList();
            }

            List<MountDisk> mountDisks = MountDiskRepository.getByUser(session,user);
            return mountDisks.stream()
                    .map(MountDisk::getName)
                    .collect(Collectors.toList());
        }
    }

    public void join(AbstractPlayer player) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SftpUser sftpUser = SftpUserRepository.getByUuid(session,player.getUniqueId());
            if(sftpUser == null){
                SftpUserRepository.register(session,player.getName(),player.getUniqueId());
            } else if(!sftpUser.getName().equals(player.getName())){
                sftpUser.setName(player.getName());
                session.save(sftpUser);
            }
            session.getTransaction().commit();
        }
    }

    public void onSetPassword(AbstractPlayer player, String password) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            SftpUser user = SftpUserRepository.getByUuid(session,player.getUniqueId());
            if(user == null){
                player.sendMessage("Register first.");
                return;
            }

            user.setPassword(hasherTools.getDefault().hash(password));
            player.sendMessage("Set password successfully.");

            session.getTransaction().commit();
        }
    }
}
