package com.demo.bait.service.DataInitializerService;

import com.demo.bait.entity.PredefinedDeviceName;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.entity.classificator.TicketStatusClassificator;
import com.demo.bait.repository.PredefinedDeviceNameRepo;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
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
    private DeviceClassificatorRepo deviceClassificatorRepo;


    @PostConstruct
    @Transactional
    public void init() {
        log.info("Initializing data in the system...");

        try {
            if (ticketStatusClassificatorRepo.count() == 0) {
                log.info("No Ticket Status Classificators found. Initializing default statuses...");
                TicketStatusClassificator ticketOpenStatus = new TicketStatusClassificator();
                ticketOpenStatus.setStatus("Open");
                ticketOpenStatus.setColor("#28a745");
                ticketStatusClassificatorRepo.save(ticketOpenStatus);
                log.info("Saved Ticket Status: Open");

                TicketStatusClassificator ticketClosedStatus = new TicketStatusClassificator();
                ticketClosedStatus.setStatus("Closed");
                ticketClosedStatus.setColor("#dc3545");
                ticketStatusClassificatorRepo.save(ticketClosedStatus);
                log.info("Saved Ticket Status: Closed");
            } else {
                log.debug("Ticket Status Classificators already initialized.");
            }

            if (predefinedDeviceNameRepo.count() == 0) {
                log.info("No Predefined Device Names found. Initializing default device names...");
                PredefinedDeviceName predefinedDeviceNameORNetPathology = new PredefinedDeviceName();
                predefinedDeviceNameORNetPathology.setName("ORNet Pathology");
                predefinedDeviceNameRepo.save(predefinedDeviceNameORNetPathology);
                log.info("Saved Predefined Device Name: ORNet Pathology");

                PredefinedDeviceName predefinedDeviceNameORNetSurgery = new PredefinedDeviceName();
                predefinedDeviceNameORNetSurgery.setName("ORNet Surgery");
                predefinedDeviceNameRepo.save(predefinedDeviceNameORNetSurgery);
                log.info("Saved Predefined Device Name: ORNet Surgery");

                PredefinedDeviceName predefinedDeviceNameORNetEditor = new PredefinedDeviceName();
                predefinedDeviceNameORNetEditor.setName("ORNet Editor");
                predefinedDeviceNameRepo.save(predefinedDeviceNameORNetEditor);
                log.info("Saved Predefined Device Name: ORNet Editor");
            } else {
                log.debug("Predefined Device Names already initialized.");
            }

            if (deviceClassificatorRepo.count() == 0) {
                log.info("No Device Classificators found. Initializing default device classificators...");
                DeviceClassificator deviceClassificatorORNetPathology = new DeviceClassificator();
                deviceClassificatorORNetPathology.setName("ORNet Pathology");
                deviceClassificatorRepo.save(deviceClassificatorORNetPathology);
                log.info("Saved Device Classificator: ORNet Pathology");

                DeviceClassificator deviceClassificatorORNetSurgery = new DeviceClassificator();
                deviceClassificatorORNetSurgery.setName("ORNet Surgery");
                deviceClassificatorRepo.save(deviceClassificatorORNetSurgery);
                log.info("Saved Device Classificator: ORNet Surgery");

                DeviceClassificator deviceClassificatorORNetEditor = new DeviceClassificator();
                deviceClassificatorORNetEditor.setName("ORNet Editor");
                deviceClassificatorRepo.save(deviceClassificatorORNetEditor);
                log.info("Saved Device Classificator: ORNet Editor");
            } else {
                log.debug("Device Classificators already initialized.");
            }

            log.info("Data initialization completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred during data initialization.", e);
            throw e;
        }
    }
}
