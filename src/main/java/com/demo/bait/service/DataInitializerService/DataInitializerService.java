package com.demo.bait.service.DataInitializerService;

import com.demo.bait.entity.PredefinedDeviceName;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.repository.PredefinedDeviceNameRepo;
import com.demo.bait.repository.classificator.TicketStatusClassificatorRepo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DataInitializerService {

    private TicketStatusClassificatorRepo ticketStatusClassificatorRepo;
    private PredefinedDeviceNameRepo predefinedDeviceNameRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void init() {
        if (ticketStatusClassificatorRepo.count() == 0) {
//            Query q = entityManager.createNativeQuery("ALTER SEQUENCE ticket_status_classificator_seq RESTART WITH 1");
//            q.executeUpdate();

            TicketStatusClassificator ticketOpenStatus = new TicketStatusClassificator();
            ticketOpenStatus.setStatus("Open");
            ticketOpenStatus.setColor("#28a745");
            ticketStatusClassificatorRepo.save(ticketOpenStatus);

            TicketStatusClassificator ticketClosedStatus = new TicketStatusClassificator();
            ticketClosedStatus.setStatus("Closed");
            ticketClosedStatus.setColor("#dc3545");
            ticketStatusClassificatorRepo.save(ticketClosedStatus);
        }

        if (predefinedDeviceNameRepo.count() == 0) {
            PredefinedDeviceName predefinedDeviceNameORNetPathology = new PredefinedDeviceName();
            predefinedDeviceNameORNetPathology.setName("ORNet Pathology");
            predefinedDeviceNameRepo.save(predefinedDeviceNameORNetPathology);

            PredefinedDeviceName predefinedDeviceNameORNetSurgery = new PredefinedDeviceName();
            predefinedDeviceNameORNetSurgery.setName("ORNet Surgery");
            predefinedDeviceNameRepo.save(predefinedDeviceNameORNetSurgery);

            PredefinedDeviceName predefinedDeviceNameORNetEditor = new PredefinedDeviceName();
            predefinedDeviceNameORNetEditor.setName("ORNet Editor");
            predefinedDeviceNameRepo.save(predefinedDeviceNameORNetEditor);
        }
    }
}
