//package com.demo.bait.service.ReportService;
//
//import com.demo.bait.entity.Client;
//import com.demo.bait.entity.Device;
//import com.demo.bait.entity.Location;
//import com.demo.bait.entity.Maintenance;
//import com.demo.bait.repository.ClientRepo;
//import com.demo.bait.repository.DeviceRepo;
//import com.demo.bait.repository.LocationRepo;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@AllArgsConstructor
//public class ClientMaintenanceReportService {
//
//    private LocationRepo locationRepo;
//    private ClientRepo clientRepo;
//    private DeviceRepo deviceRepo;
//    private ClientTicketReportService clientTicketReportService;
//
//
//    public ResponseEntity<Resource> generateClientMaintenanceReport(Integer clientId, LocalDate startDate,
//                                                                    LocalDate endDate, String fileName) {
//        log.info("Generating maintenance report for client with ID: {}, Start Date: {}, End Date: {}", clientId, startDate, endDate);
//
//        fileName = fileName + ".xlsx";
//
//        Optional<Client> clientOpt = clientRepo.findById(clientId);
//        if (clientOpt.isEmpty()) {
//            log.error("Client with ID {} not found", clientId);
//            throw new EntityNotFoundException("Client with id " + clientId + " not found");
//        }
//        Client client = clientOpt.get();
//
//        log.info("Fetching maintenances, locations, and devices for client ID: {}", clientId);
//        List<Maintenance> maintenances = clientRepo.findMaintenancesByClientAndDateRange(clientId, startDate, endDate);
//        List<Location> locations = client.getLocations().stream().toList();
//        Map<Location, List<Maintenance>> maintenancesByLocation = findLocationMaintenances(locations, startDate, endDate);
//        List<Device> devices = deviceRepo.findByClientId(clientId);
//        Map<Device, List<Maintenance>> maintenancesByDevice = findDeviceMaintenances(devices, startDate, endDate);
//
//        log.info("Creating Excel workbook for client maintenance report");
//        Workbook workbook = clientTicketReportService.createWorkbook();
//        Sheet sheet = clientTicketReportService.createSheet(workbook, "Customer Maintenance Report");
//
//        int rowNum = 0;
//        rowNum = writeClientDataToSheet(sheet, client, maintenances, maintenancesByLocation, maintenancesByDevice, startDate, endDate, rowNum);
//
//        clientTicketReportService.adjustColumnWidths(sheet);
//        String filePath = clientTicketReportService.saveWorkbookToFile(workbook, fileName);
//
//        log.info("Report generated and saved to file: {}", filePath);
//        return clientTicketReportService.createResponseEntity(fileName, filePath);
//    }
//
//    public ResponseEntity<Resource> generateAllClientsMaintenancesReport(LocalDate startDate, LocalDate endDate, String fileName) {
//        log.info("Generating maintenance report for all clients. Start Date: {}, End Date: {}", startDate, endDate);
//
//        fileName = fileName + ".xlsx";
//
//        List<Client> clients = clientRepo.findAll();
//        if (clients.isEmpty()) {
//            log.error("No clients found for maintenance report generation");
//            throw new RuntimeException("No clients found.");
//        }
//
//        log.info("Creating Excel workbook for all clients' maintenance report");
//        Workbook workbook = clientTicketReportService.createWorkbook();
//        Sheet sheet = clientTicketReportService.createSheet(workbook, "All Customers Maintenance Report");
//
//        int rowNum = 0;
//
//        for (Client client : clients) {
//            log.info("Processing client ID: {}", client.getId());
//            List<Maintenance> maintenances = clientRepo.findMaintenancesByClientAndDateRange(client.getId(), startDate, endDate);
//            List<Location> locations = client.getLocations().stream().toList();
//            Map<Location, List<Maintenance>> maintenancesByLocation = findLocationMaintenances(locations, startDate, endDate);
//            List<Device> devices = deviceRepo.findByClientId(client.getId());
//            Map<Device, List<Maintenance>> maintenancesByDevice = findDeviceMaintenances(devices, startDate, endDate);
//            rowNum = writeClientDataToSheet(sheet, client, maintenances, maintenancesByLocation, maintenancesByDevice, startDate, endDate, rowNum);
//            rowNum += 2;
//        }
//
//        clientTicketReportService.adjustColumnWidths(sheet);
//        String filePath = clientTicketReportService.saveWorkbookToFile(workbook, fileName);
//
//        log.info("All clients' report generated and saved to file: {}", filePath);
//        return clientTicketReportService.createResponseEntity(fileName, filePath);
//    }
//
//    private int writeClientDataToSheet(Sheet sheet, Client client, List<Maintenance> clientMaintenances,
//                                       Map<Location, List<Maintenance>> maintenancesByLocation,
//                                       Map<Device, List<Maintenance>> maintenancesByDevice,
//                                       LocalDate startDate, LocalDate endDate, int rowNum) {
//        log.debug("Writing client data to sheet for client ID: {}", client.getId());
//
//        if (rowNum == 0) {
//            log.debug("Writing header for maintenance report");
//            Row headerRow = sheet.createRow(rowNum++);
//            headerRow.createCell(0).setCellValue("Customer Maintenance Report");
//
//            Row periodRow = sheet.createRow(rowNum++);
//            periodRow.createCell(0).setCellValue("Time Period");
//            periodRow.createCell(1).setCellValue(startDate + " - " + endDate);
//        }
//
//        Row clientInfoParameterRow = sheet.createRow(rowNum++);
//        clientInfoParameterRow.createCell(0).setCellValue("Customer Full Name");
//        clientInfoParameterRow.createCell(1).setCellValue(client.getFullName());
//
//        rowNum++;
//
//        for (Map.Entry<Location, List<Maintenance>> entry : maintenancesByLocation.entrySet()) {
//            log.debug("Writing maintenance data for location: {}", entry.getKey().getName());
//            Location location = entry.getKey();
//            List<Maintenance> maintenancesForLocation = entry.getValue();
//
//            Row locationHeaderRow = sheet.createRow(rowNum++);
//            locationHeaderRow.createCell(0).setCellValue("Location: " + location.getName());
//
//            if (maintenancesForLocation.isEmpty()) {
//                Row noMaintenanceRow = sheet.createRow(rowNum++);
//                noMaintenanceRow.createCell(0).setCellValue("No Maintenances For Location");
//                rowNum++;
//            } else {
//                rowNum = writeMaintenanceDataToSheet(sheet, maintenancesForLocation, rowNum);
//            }
//            rowNum++;
//        }
//
//        for (Map.Entry<Device, List<Maintenance>> entry : maintenancesByDevice.entrySet()) {
//            log.debug("Writing maintenance data for device: {}", entry.getKey().getDeviceName());
//            Device device = entry.getKey();
//            List<Maintenance> maintenancesForDevice = entry.getValue();
//
//            Row deviceHeaderRow = sheet.createRow(rowNum++);
//            deviceHeaderRow.createCell(0).setCellValue(device.getDeviceName() + " s/n " + device.getSerialNumber());
//
//            if (maintenancesForDevice.isEmpty()) {
//                Row noMaintenanceRow = sheet.createRow(rowNum++);
//                noMaintenanceRow.createCell(0).setCellValue("No Maintenances For Device");
//                rowNum++;
//            } else {
//                rowNum = writeMaintenanceDataToSheet(sheet, maintenancesForDevice, rowNum);
//            }
//            rowNum++;
//        }
//
//        Row clientOtherMaintenances = sheet.createRow(rowNum++);
//        clientOtherMaintenances.createCell(0).setCellValue(client.getFullName() + " Maintenances:");
//        rowNum = writeMaintenanceDataToSheet(sheet, clientMaintenances, rowNum);
//
//        return rowNum;
//    }
//
//    private int writeMaintenanceDataToSheet(Sheet sheet, List<Maintenance> maintenances, int rowNum) {
//        log.debug("Writing maintenance data to sheet. Number of maintenances: {}", maintenances.size());
//        Row maintenanceDetails = sheet.createRow(rowNum++);
//        maintenanceDetails.createCell(0).setCellValue("Maintenance Date");
//        maintenanceDetails.createCell(1).setCellValue("Maintenance Name");
//        maintenanceDetails.createCell(2).setCellValue("Description");
//
//        for (Maintenance maintenance : maintenances) {
//            Row maintenanceRow = sheet.createRow(rowNum++);
//            maintenanceRow.createCell(0).setCellValue(maintenance.getMaintenanceDate().toString());
//            maintenanceRow.createCell(1).setCellValue(maintenance.getMaintenanceName());
//            maintenanceRow.createCell(2).setCellValue(maintenance.getComment());
//        }
//        rowNum++;
//        return rowNum;
//    }
//
//    private Map<Location, List<Maintenance>> findLocationMaintenances(List<Location> locations, LocalDate startDate, LocalDate endDate) {
//        log.debug("Finding maintenances by location. Start Date: {}, End Date: {}", startDate, endDate);
//        Map<Location, List<Maintenance>> locationMaintenancesMap = new HashMap<>();
//        for (Location location : locations) {
//            List<Maintenance> locationMaintenances = locationRepo.findMaintenancesByLocationAndDateRange(location.getId(), startDate, endDate);
//            locationMaintenancesMap.put(location, locationMaintenances);
//        }
//        return locationMaintenancesMap;
//    }
//
//    private Map<Device, List<Maintenance>> findDeviceMaintenances(List<Device> devices, LocalDate startDate, LocalDate endDate) {
//        log.debug("Finding maintenances by device. Start Date: {}, End Date: {}", startDate, endDate);
//        Map<Device, List<Maintenance>> deviceMaintenancesMap = new HashMap<>();
//        for (Device device : devices) {
//            List<Maintenance> deviceMaintenances = deviceRepo.findMaintenancesByDeviceAndDateRange(device.getId(), startDate, endDate);
//            deviceMaintenancesMap.put(device, deviceMaintenances);
//        }
//        return deviceMaintenancesMap;
//    }
//}
