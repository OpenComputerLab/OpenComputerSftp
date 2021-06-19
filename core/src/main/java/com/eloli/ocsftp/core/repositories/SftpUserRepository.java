/*
 * Copyright 2021 Mohist-Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eloli.ocsftp.core.repositories;

import com.eloli.ocsftp.core.entities.SftpUser;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class SftpUserRepository {
    private SftpUserRepository() {
        //
    }

    public static SftpUser register(Session session, String name, UUID uuid){
        SftpUser sftpUser =new SftpUser().setName(name).setUuid(uuid);
        session.save(sftpUser);
        return sftpUser;
    }

    public static SftpUser getByName(Session session, String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SftpUser> criteriaQuery = builder.createQuery(SftpUser.class);
        Root<SftpUser> root = criteriaQuery.from(SftpUser.class);
        criteriaQuery.where(builder.equal(root.get("name"), name));
        List<SftpUser> resultList = session.createQuery(criteriaQuery).getResultList();
        if(resultList.size() != 0){
            return resultList.get(0);
        }else{
            return null;
        }
    }

    public static SftpUser getByUuid(Session session, UUID uuid) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<SftpUser> criteriaQuery = builder.createQuery(SftpUser.class);
        Root<SftpUser> root = criteriaQuery.from(SftpUser.class);
        criteriaQuery.where(builder.equal(root.get("uuid"), uuid));
        List<SftpUser> resultList = session.createQuery(criteriaQuery).getResultList();
        if(resultList.size() != 0){
            return resultList.get(0);
        }else{
            return null;
        }
    }
}
