package com.demo.bait.service.ReportService;

import com.demo.bait.entity.*;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.TicketRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReportService {

    private ClientRepo clientRepo;
    private TicketRepo ticketRepo;

//    ResponseEntity<Resource>
    public void generateClientTicketsReport(Integer clientId, LocalDate startDate,
                                                                LocalDate endDate) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        Client client = clientOpt.get();

        List<Location> locations = client.getLocations().stream().toList();
        System.out.println("Locations: " + locations);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Ticket> tickets = ticketRepo.findByClientAndDateRange(clientId, startDateTime, endDateTime);
        System.out.println("Tickets: " + tickets);
        System.out.println("################################");

        Map<Location, List<Ticket>> ticketsByLocation = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getLocation));
        // locations without tickets have empty list
        for (Location location : locations) {
            ticketsByLocation.putIfAbsent(location, new ArrayList<>());
        }
        System.out.println(ticketsByLocation);
        System.out.println("################################");

        ticketsByLocation.forEach((location, ticketsForLocation) -> {
            System.out.println("Location: " + location);
            System.out.println("Tickets: " + ticketsForLocation);
        });


        StringBuilder csvBuilder = createCsvForClientTicketsReport(client, ticketsByLocation, startDate, endDate);

        System.out.println(csvBuilder.toString());
        saveCsvToFile(csvBuilder, "C:\\Users\\AMD\\IdeaProjects\\ORNet\\uploads", "testCSV.csv");
    }

    public StringBuilder createCsvForClientTicketsReport(Client client, Map<Location, List<Ticket>> ticketsByLocation,
                                                         LocalDate startDate, LocalDate endDate) {
        StringBuilder csvBuilder = new StringBuilder();
        Duration overallDuration = Duration.ZERO;
        Duration paidDuration = Duration.ZERO;

        csvBuilder.append("Client Ticket Report").append("\n")
                .append("Time Period").append(",").append(startDate).append(" - ").append(endDate).append("\n")
                .append("Full Name, Short Name\n")
                .append(client.getFullName()).append(",")
                .append(client.getShortName()).append("\n\n");

        for (Map.Entry<Location, List<Ticket>> entry : ticketsByLocation.entrySet()) {
            Location location = entry.getKey();
            List<Ticket> ticketsForLocation = entry.getValue();

            csvBuilder.append("Location").append("\n")
                    .append("Location Name, Country, City, Street Address, Postal Code, Phone, Email\n");

            csvBuilder.append(location.getName()).append(",")
                    .append(location.getCountry()).append(",")
                    .append(location.getCity()).append(",")
                    .append(location.getStreetAddress()).append(",")
                    .append(location.getPostalCode()).append(",")
                    .append(location.getPhone()).append(",")
                    .append(location.getEmail()).append("\n\n");

            if (ticketsForLocation.size() == 0) {
                csvBuilder.append("This location does not have any tickets in the time period ")
                        .append(startDate).append(" - ").append(endDate).append("\n\n");
            } else {
                for (Ticket ticket : ticketsForLocation) {
                    if (ticket.getTimeSpent() != null) {
                        overallDuration = overallDuration.plus(ticket.getTimeSpent());
                    }
                    if (ticket.getPaidTime() != null) {
                        paidDuration = paidDuration.plus(ticket.getPaidTime());
                    }

                    csvBuilder.append("Ticket").append("\n")
                            .append("Ticket, Ticket Title, Bait Numeration, Client Numeration, Creation Date Time, Remote, " +
                                    "Status, Responsible Bait Worker, End Date Time, Paid Work, Spent Time, Paid Spent Time")
                            .append("\n");

                    csvBuilder.append(ticket.getName()).append(",")
                            .append(ticket.getTitle()).append(",")
                            .append(ticket.getBaitNumeration()).append(",")
                            .append(ticket.getClientNumeration()).append(",")
                            .append(ticket.getStartDateTime()).append(",")
                            .append(ticket.getRemote()).append(",")
                            .append(ticket.getStatus().getStatus()).append(","); // kas raportisse lahevad ainult closed ticketid???

                    if (ticket.getBaitWorker() != null) {
                        csvBuilder.append(ticket.getBaitWorker().getFirstName())
                                .append(" ")
                                .append(ticket.getBaitWorker().getLastName()).append(",");
                    } else {
                        csvBuilder.append(" ").append(",");
                    }
                    csvBuilder.append(ticket.getEndDateTime()).append(",")
                                .append(ticket.getPaid()).append(",")
                                .append(ticket.getTimeSpent()).append(",")
                                .append(ticket.getPaidTime()).append("\n")

                                .append(ticket.getName()).append(" Description").append(",")
                                .append(ticket.getDescription().replace(",", ";")).append("\n")

                                .append(ticket.getName()).append(" Root Cause").append(",")
                                .append(ticket.getRootCause().replace(",", ";")).append("\n\n");

                    List<Activity> activities = ticket.getActivities().stream().toList();
                    if (activities.size() == 0) {
                        csvBuilder.append("This ticket does not have any activity's").append("\n\n");
                    } else {
                        csvBuilder.append("Completed Activity's").append("\n")
                            .append("Timestamp, Time Spent, Paid, Activity").append("\n");

                        for (Activity activity : activities) {
                            csvBuilder.append(activity.getTimestamp()).append(",")
                                    .append(activity.getTimeSpent()).append(",")
                                    .append(activity.getPaid()).append(",")
                                    .append(activity.getActivity().replace(",", ";")).append("\n");
                        }
                        csvBuilder.append("\n");
                    }

                    List<Device> devices = ticket.getDevices().stream().toList();
                    if (devices.size() != 0) {
                        csvBuilder.append("Affected Devices").append("\n")
                                .append("Device Name, Serial Number, Department").append("\n");
                        for (Device device : devices) {
                            csvBuilder.append(device.getDeviceName()).append(",")
                                    .append(device.getSerialNumber()).append(",")
                                    .append(device.getDepartment()).append("\n");
                        }
                        csvBuilder.append("\n");
                    }
                }
            }
        }
        csvBuilder.append("Overall Time spent: ").append(overallDuration).append("\n")
                .append("Paid Time: ").append(paidDuration)
                .append("\n");
        return csvBuilder;
    }

    public void saveCsvToFile(StringBuilder csvContent, String folderPath, String fileName) {
        try {
            Path directoryPath = Paths.get(folderPath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(folderPath, fileName);

            Files.write(filePath, csvContent.toString().getBytes(StandardCharsets.UTF_8));

            System.out.println("CSV file saved successfully at: " + filePath.toString());
        } catch (IOException e) {
            System.err.println("Error saving CSV file: " + e.getMessage());
        }
    }
}
