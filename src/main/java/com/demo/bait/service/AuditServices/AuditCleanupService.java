package com.demo.bait.service.AuditServices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuditCleanupService {

    private final EntityManager entityManager;

    @Transactional
    public void deleteAuditHistoryBefore(LocalDate cutoffDate) {
        LocalDateTime cutoffDateTime = cutoffDate.atStartOfDay(); // Convert LocalDate to LocalDateTime
        Date cutoff = Date.from(cutoffDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // Delete old revisions from the REVINFO table using native SQL
        entityManager.createNativeQuery(
                        "DELETE FROM REVINFO WHERE REVINFO.revtstmp < :cutoff")
                .setParameter("cutoff", cutoff.getTime()) // Note: timestamp is in milliseconds
                .executeUpdate();
    }

    @Transactional
    public void cleanupAudTables(LocalDate cutoffDate) {
        List<String> audTables = List.of("bait_worker_aud", "client_aud", "client_worker_aud",
                "client_worker_role_classificator_aud", "comment_aud", "device_aud", "device_classificator_aud",
                "file_upload_aud", "linked_device_aud", "location_aud", "maintenance_aud", "paid_work_aud",
                "software_aud", "third_partyit_aud", "ticket_aud", "ticket_status_classificator_aud", "wiki_aud",
                "work_type_classificator_aud");

        LocalDateTime cutoffDateTime = cutoffDate.atStartOfDay(); // Convert LocalDate to LocalDateTime
        Date cutoff = Date.from(cutoffDateTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Object> preserve = new ArrayList<>();

        for (String audTable : audTables) {
            Query findEarliestRevisionsQuery = entityManager.createNativeQuery(
                    "WITH EarliestRevisions AS (" +
                            "    SELECT id, MIN(rev) AS earliest_rev_id" +
                            "    FROM " + audTable +
                            "    GROUP BY id" +
                            ")" +
                            "SELECT rev FROM " + audTable + " WHERE rev IN (" +
                            "    SELECT earliest_rev_id FROM EarliestRevisions" +
                            ")"
            );
            List<Object> preservedRevs = findEarliestRevisionsQuery.getResultList();
            System.out.println(preservedRevs);
            preserve.addAll(preservedRevs);

            Query deleteOtherRevisionsQuery = entityManager.createNativeQuery(
                    "DELETE FROM " + audTable +
                            " WHERE rev NOT IN (" +
                            "    SELECT rev FROM " + audTable + " WHERE rev IN (" +
                            "        SELECT earliest_rev_id FROM (" +
                            "            SELECT id, MIN(rev) AS earliest_rev_id" +
                            "            FROM " + audTable +
                            "            GROUP BY id" +
                            "        ) AS EarliestRevisions" +
                            "    )" +
                            ")" +
                            " AND rev IN (" +
                            "    SELECT rev FROM revinfo WHERE revtstmp < :cutoff" +
                            ")"
            );
            deleteOtherRevisionsQuery.setParameter("cutoff", cutoff.getTime());
            deleteOtherRevisionsQuery.executeUpdate();
        }

        String preserveIds = preserve.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        String deleteQuery = "DELETE FROM REVINFO WHERE REVINFO.revtstmp < :cutoff AND REVINFO.rev NOT IN (" + preserveIds + ")";

        entityManager.createNativeQuery(deleteQuery)
                .setParameter("cutoff", cutoff.getTime()) // Note: timestamp is in milliseconds
                .executeUpdate();
    }
}
