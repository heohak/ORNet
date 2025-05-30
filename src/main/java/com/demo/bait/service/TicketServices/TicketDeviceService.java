package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.DeviceServices.DeviceHelperService;
import com.demo.bait.service.DeviceServices.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TicketDeviceService {

    private TicketRepo ticketRepo;
    private DeviceMapper deviceMapper;
    private DeviceHelperService deviceHelperService;

    @Transactional
    public void addDevicesToTicket(Ticket ticket, TicketDTO ticketDTO) {
        log.info("Adding devices to ticket with ID: {}", ticket.getId());
        if (ticketDTO.deviceIds() != null) {
            log.debug("Device IDs provided: {}", ticketDTO.deviceIds());
            Set<Device> devices = deviceHelperService.deviceIdsToDevicesSet(ticketDTO.deviceIds());
            ticket.setDevices(devices);
            addCustomerRegisterNos(ticket, devices);
            ticketRepo.save(ticket);
            log.info("Devices successfully added to ticket with ID: {}", ticket.getId());
        } else {
            log.warn("No device IDs provided for ticket with ID: {}", ticket.getId());
        }
    }

    public List<DeviceDTO> getTicketDevices(Integer ticketId) {
        if (ticketId == null) {
            log.warn("Ticket ID is null. Returning empty list.");
            return Collections.emptyList();
        }

        log.info("Fetching devices for ticket with ID: {}", ticketId);
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            log.error("Ticket with ID: {} not found", ticketId);
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        List<DeviceDTO> devices = deviceMapper.toDtoList(ticket.getDevices().stream().toList());
        log.info("Found {} devices for ticket with ID: {}", devices.size(), ticketId);
        return devices;
    }

    public void addCustomerRegisterNos(Ticket ticket, Set<Device> devices) {
        StringBuilder customerRegisterNos = new StringBuilder();
        for (Device device : devices) {
            List<String> deviceNumbers = new ArrayList<>();
            if (device.getWorkstationNo() != null) {
                deviceNumbers.add(device.getWorkstationNo());
            }
            if (device.getCameraNo() != null) {
                deviceNumbers.add(device.getCameraNo());
            }
            if (device.getOtherNo() != null) {
                deviceNumbers.add(device.getOtherNo());
            }
            if (!deviceNumbers.isEmpty()) {
                customerRegisterNos.append(String.join("/", deviceNumbers)).append("; ");
            }
        }
        ticket.setCustomerRegisterNos(customerRegisterNos.toString());
    }
}
