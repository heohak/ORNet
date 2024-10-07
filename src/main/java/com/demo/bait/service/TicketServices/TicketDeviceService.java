package com.demo.bait.service.TicketServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.dto.TicketDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.Ticket;
import com.demo.bait.mapper.DeviceMapper;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.DeviceServices.DeviceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class TicketDeviceService {

    private TicketRepo ticketRepo;
    private DeviceService deviceService;
    private DeviceMapper deviceMapper;

    @Transactional
    public void addDevicesToTicket(Ticket ticket, TicketDTO ticketDTO) {
        if (ticketDTO.deviceIds() != null) {
            Set<Device> devices = deviceService.deviceIdsToDevicesSet(ticketDTO.deviceIds());
            ticket.setDevices(devices);
            ticketRepo.save(ticket);
        }
    }

    public List<DeviceDTO> getTicketDevices(Integer ticketId) {
        Optional<Ticket> ticketOpt = ticketRepo.findById(ticketId);
        if (ticketOpt.isEmpty()) {
            throw new EntityNotFoundException("Ticket with id " + ticketId + " not found");
        }
        Ticket ticket = ticketOpt.get();
        return deviceMapper.toDtoList(ticket.getDevices().stream().toList());
    }
}
