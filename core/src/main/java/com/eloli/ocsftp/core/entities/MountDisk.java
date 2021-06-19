package com.eloli.ocsftp.core.entities;

import com.eloli.sodioncore.orm.SodionEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class MountDisk extends SodionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    protected Integer id;

    @Column(nullable = false)
    protected UUID uuid;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected Integer owner;

    public Integer getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public MountDisk setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public MountDisk setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getOwner() {
        return owner;
    }

    public MountDisk setOwner(Integer owner) {
        this.owner = owner;
        return this;
    }

    public MountDisk setOwner(SftpUser user) {
        this.owner = user.getId();
        return this;
    }
}
