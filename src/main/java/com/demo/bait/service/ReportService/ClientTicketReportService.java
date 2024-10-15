package com.demo.bait.service.ReportService;

import com.demo.bait.entity.*;
import com.demo.bait.repository.ClientRepo;
import com.demo.bait.repository.TicketRepo;
import com.demo.bait.service.FileUploadServices.FileUploadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ClientTicketReportService {

    private ClientRepo clientRepo;
    private TicketRepo ticketRepo;
    private static final String REPORTS_DIR = "reports";

    public ResponseEntity<Resource> generateClientTicketsReport(Integer clientId, LocalDate startDate,
                                                                LocalDate endDate, String fileName) {
        fileName = fileName + ".xlsx";

        Optional<Client> clientOpt = clientRepo.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + clientId + " not found");
        }
        Client client = clientOpt.get();

        List<Location> locations = client.getLocations().stream().toList();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        List<Ticket> tickets = ticketRepo.findByClientAndDateRange(clientId, startDateTime, endDateTime);

        Map<Location, List<Ticket>> ticketsByLocation = groupTicketsByLocation(tickets, locations);

        Workbook workbook = createWorkbook();
        Sheet sheet = createSheet(workbook, "Customer Ticket Report");

        int rowNum = 0;
        rowNum = writeClientDataToSheet(sheet, client, ticketsByLocation, startDate, endDate, rowNum, null);

        adjustColumnWidths(sheet);
        String filePath = saveWorkbookToFile(workbook, fileName);

        return createResponseEntity(fileName, filePath);
    }

    public ResponseEntity<Resource> generateAllClientsTicketsReport(LocalDate startDate, LocalDate endDate,
                                                                    String fileName) {
        fileName = fileName + ".xlsx";

        List<Client> clients = clientRepo.findAll();
        if (clients.isEmpty()) {
            throw new RuntimeException("No clients found.");
        }

        Workbook workbook = createWorkbook();
        Sheet sheet = createSheet(workbook, "All Customers Ticket Report");
        int rowNum = 0;

        DurationTotals grandTotals = new DurationTotals();

        for (Client client : clients) {
            List<Location> locations = client.getLocations().stream().toList();

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            List<Ticket> tickets = ticketRepo.findByClientAndDateRange(client.getId(), startDateTime, endDateTime);

            Map<Location, List<Ticket>> ticketsByLocation = groupTicketsByLocation(tickets, locations);

            rowNum = writeClientDataToSheet(sheet, client, ticketsByLocation, startDate, endDate, rowNum, grandTotals);
        }

        rowNum++;
        Row grandTotalHeaderRow = sheet.createRow(rowNum++);
        grandTotalHeaderRow.createCell(0).setCellValue("Grand Total Summary:");

        Row grandTotalTimeRow = sheet.createRow(rowNum++);
        grandTotalTimeRow.createCell(0).setCellValue("Total Time Spent Across All Customers");
        grandTotalTimeRow.createCell(1).setCellValue(formatDuration(grandTotals.overallDuration));

        Row grandTotalPaidTimeRow = sheet.createRow(rowNum++);
        grandTotalPaidTimeRow.createCell(0).setCellValue("Total Paid Time Across All Customers");
        grandTotalPaidTimeRow.createCell(1).setCellValue(formatDuration(grandTotals.paidDuration));

        adjustColumnWidths(sheet);
        String filePath = saveWorkbookToFile(workbook, fileName);

        return createResponseEntity(fileName, filePath);
    }

    private Map<Location, List<Ticket>> groupTicketsByLocation(List<Ticket> tickets, List<Location> locations) {
        Map<Location, List<Ticket>> ticketsByLocation = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getLocation));

        for (Location location : locations) {
            ticketsByLocation.putIfAbsent(location, new ArrayList<>());
        }
        return ticketsByLocation;
    }

    public Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    public Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    public String saveWorkbookToFile(Workbook workbook, String fileName) {
        File uploadDir = new File(REPORTS_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = REPORTS_DIR + File.separator + fileName;

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    public ResponseEntity<Resource> createResponseEntity(String fileName, String filePath) {
        Path path = Paths.get(filePath);
        Resource resource = FileUploadService.loadResource(path);
        HttpHeaders headers = FileUploadService.createHeaders(fileName, "attachment");

        try {
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(Files.size(path))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving file size", e);
        }
    }

    private static class DurationTotals {
        Duration overallDuration = Duration.ZERO;
        Duration paidDuration = Duration.ZERO;
    }

    private int writeClientDataToSheet(Sheet sheet, Client client, Map<Location, List<Ticket>> ticketsByLocation,
                                       LocalDate startDate, LocalDate endDate, int rowNum, DurationTotals grandTotals) {
        DurationTotals clientTotals = new DurationTotals();

        if (rowNum == 0) {
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Customer Ticket Report");

            Row periodRow = sheet.createRow(rowNum++);
            periodRow.createCell(0).setCellValue("Time Period");
            periodRow.createCell(1).setCellValue(startDate + " - " + endDate);
        }

        Row clientInfoParameterRow = sheet.createRow(rowNum++);
        clientInfoParameterRow.createCell(0).setCellValue("Customer Full Name");
        clientInfoParameterRow.createCell(1).setCellValue(client.getFullName());

        rowNum++;

        for (Map.Entry<Location, List<Ticket>> entry : ticketsByLocation.entrySet()) {
            Location location = entry.getKey();
            List<Ticket> ticketsForLocation = entry.getValue();

            Row locationHeaderRow = sheet.createRow(rowNum++);
            locationHeaderRow.createCell(0).setCellValue("Location: " + location.getName());

            if (ticketsForLocation.isEmpty()) {
                Row noTicketsRow = sheet.createRow(rowNum++);
                noTicketsRow.createCell(0).setCellValue("No Tickets For Location");
            } else {
                rowNum = writeTicketsDataToSheet(sheet, ticketsForLocation, rowNum, clientTotals);
            }
            rowNum++;
        }

        rowNum++;
        Row summaryHeaderRow = sheet.createRow(rowNum++);
        summaryHeaderRow.createCell(0).setCellValue("Customer Summary:");

        Row summaryRow = sheet.createRow(rowNum++);
        summaryRow.createCell(0).setCellValue("Total Time Spent");
        summaryRow.createCell(1).setCellValue(formatDuration(clientTotals.overallDuration));

        Row paidTimeRow = sheet.createRow(rowNum++);
        paidTimeRow.createCell(0).setCellValue("Total Paid Time");
        paidTimeRow.createCell(1).setCellValue(formatDuration(clientTotals.paidDuration));

        if (grandTotals != null) {
            grandTotals.overallDuration = grandTotals.overallDuration.plus(clientTotals.overallDuration);
            grandTotals.paidDuration = grandTotals.paidDuration.plus(clientTotals.paidDuration);
        }

        rowNum++;
        rowNum++;

        return rowNum;
    }

    private int writeTicketsDataToSheet(Sheet sheet, List<Ticket> ticketsForLocation, int rowNum,
                                        DurationTotals totals) {
        Row ticketDetails = sheet.createRow(rowNum++);
        ticketDetails.createCell(0).setCellValue("Ticket ID / Customer ticket nr.");
        ticketDetails.createCell(1).setCellValue("Title");
        ticketDetails.createCell(2).setCellValue("Device");
        ticketDetails.createCell(3).setCellValue("Activity Time");
        ticketDetails.createCell(4).setCellValue("Activity");
        ticketDetails.createCell(5).setCellValue("Time Spent");
        ticketDetails.createCell(6).setCellValue("Paid Activity");
        ticketDetails.createCell(7).setCellValue("Status");
        ticketDetails.createCell(8).setCellValue("Closed Date Time");

        for (Ticket ticket : ticketsForLocation) {
            Row ticketRow = sheet.createRow(rowNum++);
            ticketRow.createCell(0).setCellValue(ticket.getBaitNumeration() + " / " + ticket.getClientNumeration());
            ticketRow.createCell(1).setCellValue(ticket.getTitle());

            List<Device> devices = ticket.getDevices().stream().toList();
            String devicesList = devices.isEmpty() ? "No affected devices"
                    : devices.stream()
                    .map(device -> device.getDeviceName() + " s/n " + device.getSerialNumber())
                    .collect(Collectors.joining("; "));
            ticketRow.createCell(2).setCellValue(devicesList);
            ticketRow.createCell(7).setCellValue(ticket.getStatus().getStatus());
            if (ticket.getEndDateTime() != null) {
                ticketRow.createCell(8).setCellValue(ticket.getEndDateTime().toString());
            }

            List<Activity> activities = ticket.getActivities().stream().toList();
            if (activities.isEmpty()) {
                for (int i = 3; i <= 8; i++) {
                    ticketRow.createCell(i).setCellValue("X");
                }
            } else {
                for (Activity activity : activities) {
                    ticketRow.createCell(3).setCellValue(activity.getTimestamp().toString());
                    ticketRow.createCell(4).setCellValue(activity.getActivity().replace(";", ","));
                    ticketRow.createCell(5).setCellValue(formatDuration(activity.getTimeSpent()));
                    ticketRow.createCell(6).setCellValue(activity.getPaid() ? "Yes" : "No");
                    ticketRow = sheet.createRow(rowNum++);
                }
            }

            if (ticket.getTimeSpent() != null) {
                totals.overallDuration = totals.overallDuration.plus(ticket.getTimeSpent());
                ticketRow.createCell(5).setCellValue("Total " + formatDuration(ticket.getTimeSpent()));
            }
            if (ticket.getPaidTime() != null) {
                totals.paidDuration = totals.paidDuration.plus(ticket.getPaidTime());
                ticketRow.createCell(6).setCellValue("Total " + formatDuration(ticket.getPaidTime()));
            }
            rowNum++;
        }
        return rowNum;
    }

    public void adjustColumnWidths(Sheet sheet) {
        for (int i = 0; i <= 13; i++) {
            sheet.setColumnWidth(i, 8000);
        }
    }

    private String formatDuration(Duration duration) {
        if (duration == null || duration.isZero()) {
            return "0";
        }
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        if (hours > 0 && minutes > 0) {
            return hours + "H " + minutes + "M";
        } else if (hours > 0) {
            return hours + "H";
        } else {
            return minutes + "M";
        }
    }

//    public void createExcelForClientTicketsReport2(Client client, Map<Location, List<Ticket>> ticketsByLocation,
//                                                  LocalDate startDate, LocalDate endDate, String fileName) {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Client Ticket Report");
//        int rowNum = 0;
//        Duration overallDuration = Duration.ZERO;
//        Duration paidDuration = Duration.ZERO;
//
//        Row headerRow = sheet.createRow(rowNum++);
//        headerRow.createCell(0).setCellValue("Client Ticket Report");
//
//        Row periodRow = sheet.createRow(rowNum++);
//        periodRow.createCell(0).setCellValue("Time Period");
//        periodRow.createCell(1).setCellValue(startDate + " - " + endDate);
//
//        Row clientInfoParameterRow = sheet.createRow(rowNum++);
//        clientInfoParameterRow.createCell(0).setCellValue("Client Full Name");
//        clientInfoParameterRow.createCell(1).setCellValue("Client Short Name");
//
//        Row clientInfoRow = sheet.createRow(rowNum++);
//        clientInfoRow.createCell(0).setCellValue(client.getFullName());
//        clientInfoRow.createCell(1).setCellValue(client.getShortName());
//
//        rowNum++;
//
//        for (Map.Entry<Location, List<Ticket>> entry : ticketsByLocation.entrySet()) {
//            Location location = entry.getKey();
//            List<Ticket> ticketsForLocation = entry.getValue();
//
//            Row locationHeaderRow = sheet.createRow(rowNum++);
//            locationHeaderRow.createCell(0).setCellValue("Location Details:");
//
//            Row locationParameterRow = sheet.createRow(rowNum++);
//            locationParameterRow.createCell(0).setCellValue("Location Name");
//            locationParameterRow.createCell(1).setCellValue("Country");
//            locationParameterRow.createCell(2).setCellValue("City");
//            locationParameterRow.createCell(3).setCellValue("Street Address");
//            locationParameterRow.createCell(4).setCellValue("Postal Code");
//            locationParameterRow.createCell(5).setCellValue("Phone");
//            locationParameterRow.createCell(6).setCellValue("Email");
//
//            Row locationDetailsRow = sheet.createRow(rowNum++);
//            locationDetailsRow.createCell(0).setCellValue(location.getName());
//            locationDetailsRow.createCell(1).setCellValue(location.getCountry());
//            locationDetailsRow.createCell(2).setCellValue(location.getCity());
//            locationDetailsRow.createCell(3).setCellValue(location.getStreetAddress());
//            locationDetailsRow.createCell(4).setCellValue(location.getPostalCode());
//            locationDetailsRow.createCell(5).setCellValue(location.getPhone());
//            locationDetailsRow.createCell(6).setCellValue(location.getEmail());
//
//            rowNum++;
//
//            if (ticketsForLocation.isEmpty()) {
//                Row noTicketsRow = sheet.createRow(rowNum++);
//                noTicketsRow.createCell(0).setCellValue("No Tickets For Location");
//            } else {
//                Row ticketDetails = sheet.createRow(rowNum++);
//                ticketDetails.createCell(0).setCellValue("Ticket Details:");
//                Row ticketParameterRow = sheet.createRow(rowNum++);
//                ticketParameterRow.createCell(0).setCellValue("Ticket ID");
//                ticketParameterRow.createCell(1).setCellValue("Title");
//                ticketParameterRow.createCell(2).setCellValue("Start Date Time");
//                ticketParameterRow.createCell(3).setCellValue("Numeration");
//                ticketParameterRow.createCell(4).setCellValue("Description");
//                ticketParameterRow.createCell(5).setCellValue("Devices");
//                ticketParameterRow.createCell(6).setCellValue("Status");
//                ticketParameterRow.createCell(7).setCellValue("Closed Date Time");
//                ticketParameterRow.createCell(8).setCellValue("Root Cause");
//                ticketParameterRow.createCell(9).setCellValue("Total Time Spent");
//                ticketParameterRow.createCell(10).setCellValue("Total Paid Time");
//
//                for (Ticket ticket : ticketsForLocation) {
//                    if (ticket.getTimeSpent() != null) {
//                        overallDuration = overallDuration.plus(ticket.getTimeSpent());
//                    }
//                    if (ticket.getPaidTime() != null) {
//                        paidDuration = paidDuration.plus(ticket.getPaidTime());
//                    }
//                    Row ticketRow = sheet.createRow(rowNum++);
//                    ticketRow.createCell(0).setCellValue(ticket.getName());
//                    ticketRow.createCell(1).setCellValue(ticket.getTitle());
//                    ticketRow.createCell(2).setCellValue(ticket.getStartDateTime().toString());
//                    ticketRow.createCell(3).setCellValue(ticket.getBaitNumeration());
//                    ticketRow.createCell(4).setCellValue(ticket.getDescription().replace(";", ","));
//
//                    List<Device> devices = ticket.getDevices().stream().toList();
//                    String devicesList = devices.isEmpty() ? "No affected devices"
//                            : devices.stream().map(Device::getDeviceName).collect(Collectors.joining(", "));
//                    ticketRow.createCell(5).setCellValue(devicesList);
//
//                    ticketRow.createCell(6).setCellValue(ticket.getStatus().getStatus());
//                    ticketRow.createCell(7).setCellValue(ticket.getEndDateTime() != null ? ticket.getEndDateTime().toString() : "");
//                    ticketRow.createCell(8).setCellValue(ticket.getRootCause().replace(";", ","));
//                    ticketRow.createCell(9).setCellValue(formatDuration(ticket.getTimeSpent()));
//                    ticketRow.createCell(10).setCellValue(formatDuration(ticket.getPaidTime()));
//
//                    List<Activity> activities = ticket.getActivities().stream().toList();
//                    if (activities.isEmpty()) {
//                        Row noActivitiesRow = sheet.createRow(rowNum++);
//                        noActivitiesRow.createCell(1).setCellValue("No Activities for this Ticket");
//                    } else {
//                        Row activityHeaderRow = sheet.createRow(rowNum++);
//                        activityHeaderRow.createCell(1).setCellValue("Activities:");
//                        Row activityParameterRow = sheet.createRow(rowNum++);
//                        activityParameterRow.createCell(2).setCellValue("Timestamp");
//                        activityParameterRow.createCell(3).setCellValue("Time Spent");
//                        activityParameterRow.createCell(4).setCellValue("Paid Activity");
//                        activityParameterRow.createCell(5).setCellValue("Activity Details");
//
//                        for (Activity activity : activities) {
//                            Row activityRow = sheet.createRow(rowNum++);
//                            activityRow.createCell(2).setCellValue(activity.getTimestamp().toString());
//                            activityRow.createCell(3).setCellValue(formatDuration(activity.getTimeSpent()));
//                            activityRow.createCell(4).setCellValue(activity.getPaid() ? "Yes" : "No");
//                            activityRow.createCell(5).setCellValue(activity.getActivity().replace(";", ","));
//                        }
//                    }
//                    rowNum++;
//                }
//            }
//            rowNum++;
//            rowNum++;
//        }
//
//        rowNum++;
//        Row summaryHeaderRow = sheet.createRow(rowNum++);
//        summaryHeaderRow.createCell(0).setCellValue("Overall Summary:");
//
//        Row summaryRow = sheet.createRow(rowNum++);
//        summaryRow.createCell(0).setCellValue("Total Time Spent");
//        summaryRow.createCell(1).setCellValue(formatDuration(overallDuration));
//
//        Row paidTimeRow = sheet.createRow(rowNum++);
//        paidTimeRow.createCell(0).setCellValue("Total Paid Time");
//        paidTimeRow.createCell(1).setCellValue(formatDuration(paidDuration));
//
//        // columns sizes
//        for (int i = 0; i <= 13; i++) {
////            sheet.autoSizeColumn(i);
//            sheet.setColumnWidth(i, 8000);
//        }
//
//        File uploadDir = new File(REPORTS_DIR);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdirs();
//        }
//
//        String filePath = REPORTS_DIR + File.separator + fileName;
//
//        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//            workbook.write(fileOut);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                workbook.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
