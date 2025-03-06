package com.demo.bait.entity.CustomRevisionEntity;
import com.demo.bait.entity.CustomRevisionEntity.CustomRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity cre = (CustomRevisionEntity) revisionEntity;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username != null && username.endsWith("@bait.local")) {
            username = username.substring(0, username.indexOf("@"));
        }
        cre.setUsername(username);
    }
}

