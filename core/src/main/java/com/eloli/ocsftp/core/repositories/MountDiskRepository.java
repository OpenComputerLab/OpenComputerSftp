package com.eloli.ocsftp.core.repositories;

import com.eloli.ocsftp.core.entities.MountDisk;
import com.eloli.ocsftp.core.entities.SftpUser;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class MountDiskRepository {
    private MountDiskRepository() {
        //
    }

    public static List<MountDisk> getByUser(Session session, SftpUser user) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MountDisk> criteriaQuery = builder.createQuery(MountDisk.class);
        Root<MountDisk> root = criteriaQuery.from(MountDisk.class);
        criteriaQuery.where(builder.equal(root.get("owner"), user.getId()));
        return session.createQuery(criteriaQuery).getResultList();
    }

    public static MountDisk getByUserAndName(Session session, SftpUser user, String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MountDisk> criteriaQuery = builder.createQuery(MountDisk.class);
        Root<MountDisk> root = criteriaQuery.from(MountDisk.class);
        criteriaQuery.where(builder.and(
                builder.equal(root.get("owner"), user.getId()),
                builder.equal(root.get("name"), name)
                ));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    public static List<MountDisk> getByUserOfOne(Session session, SftpUser user, String name, UUID uuid) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<MountDisk> criteriaQuery = builder.createQuery(MountDisk.class);
        Root<MountDisk> root = criteriaQuery.from(MountDisk.class);
        criteriaQuery.where(
                builder.and(
                        builder.equal(root.get("owner"), user.getId()),
                        builder.or(
                                builder.equal(root.get("name"), name),
                                builder.equal(root.get("uuid"), uuid)
                        )));
        return session.createQuery(criteriaQuery).getResultList();
    }
}
