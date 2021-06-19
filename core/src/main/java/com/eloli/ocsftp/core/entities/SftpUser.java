package com.eloli.ocsftp.core.entities;

import com.eloli.sodioncore.orm.SodionEntity;

import javax.persistence.*;
import java.sql.Blob;
import java.util.UUID;

@Entity
public class SftpUser extends SodionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    protected Integer id;

    @Column(nullable = false)
    protected UUID uuid;

    @Column(nullable = false)
    protected String name;

    @Column
    protected String password;

    @Column
    @Lob
    protected Blob sshKey;

    public Integer getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public SftpUser setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public SftpUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SftpUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public Blob getSshKey() {
        return sshKey;
    }

    public SftpUser setSshKey(Blob sshKey) {
        this.sshKey = sshKey;
        return this;
    }
}
