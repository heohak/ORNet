package com.demo.bait.service.DeviceServices;

import com.demo.bait.dto.DeviceDTO;
import com.demo.bait.entity.Device;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import com.demo.bait.specification.DeviceSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceSummaryService {

    private DeviceRepo deviceRepo;
    private DeviceClassificatorRepo deviceClassificatorRepo;

    public Map<String, Integer> getDevicesSummary(List<Integer> deviceIds) {
        log.info("Generating summary for devices with IDs: {}", deviceIds);
        try {
            Map<String, Integer> summaryMap = new HashMap<>();
            Integer allDevices = deviceIds.size();
            summaryMap.put("All Devices", allDevices);

            log.debug("Building specification for device IDs: {}", deviceIds);
            Specification<Device> byDeviceIds = (root, query, criteriaBuilder) -> {
                CriteriaBuilder.In<Integer> inClause = criteriaBuilder.in(root.get("id"));
                deviceIds.forEach(inClause::value);
                return inClause;
            };

            for (DeviceClassificator classificator : deviceClassificatorRepo.findAll()) {
                log.debug("Calculating count for classificator: {}", classificator.getName());
                Specification<Device> classificatorSpec = DeviceSpecification.hasClassificatorId(classificator.getId());
                Specification<Device> combinedSpec = Specification.where(byDeviceIds).and(classificatorSpec);
                Integer sum = deviceRepo.findAll(combinedSpec).size();
                summaryMap.put(classificator.getName(), sum);
            }

            log.info("Device summary generated successfully.");
            return sortMapWithSpecialFirst(summaryMap, "All Devices");
        } catch (Exception e) {
            log.error("Error while generating devices summary for IDs: {}", deviceIds, e);
            throw e;
        }
    }

    public Map<String, Integer> getClientDevicesSummary(Integer clientId) {
        log.info("Generating device summary for client with ID: {}", clientId);
        try {
            Map<String, Integer> clientSummaryMap = new HashMap<>();

            log.debug("Building specification for client ID: {}", clientId);
            Specification<Device> clientSpec = DeviceSpecification.hasClientId(clientId);
            List<Device> allClientDevices = deviceRepo.findAll(clientSpec);
            clientSummaryMap.put("All Customer Devices", allClientDevices.size());

            for (DeviceClassificator classificator : deviceClassificatorRepo.findAll()) {
                log.debug("Calculating count for classificator: {}", classificator.getName());
                Specification<Device> classificatorSpec = DeviceSpecification.hasClassificatorId(classificator.getId());
                Specification<Device> combinedSpec = clientSpec.and(classificatorSpec);
                Integer sum = deviceRepo.findAll(combinedSpec).size();

                clientSummaryMap.put(classificator.getName(), sum);
            }

            log.info("Client device summary generated successfully for client ID: {}", clientId);
            Map<String, Integer> newClientSummaryMap = removeZeroValues(clientSummaryMap);
            return sortMapWithSpecialFirst(newClientSummaryMap, "All Customer Devices");
        } catch (Exception e) {
            log.error("Error while generating device summary for client ID: {}", clientId, e);
            throw e;
        }
    }

    public static Map<String, Integer> sortMapWithSpecialFirst(Map<String, Integer> map, String specialKey) {
        log.debug("Sorting map with special key '{}' first: {}", specialKey, map);
        Integer specialValue = map.remove(specialKey);

        Map<String, Integer> sortedMap = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        Map<String, Integer> finalMap = new LinkedHashMap<>();
        if (specialValue != null) {
            finalMap.put(specialKey, specialValue);
        }
        finalMap.putAll(sortedMap);

        log.debug("Sorted map: {}", finalMap);
        return finalMap;
    }

    public static Map<String, Integer> removeZeroValues(Map<String, Integer> map) {
        log.debug("Removing zero values from map: {}", map);
        Map<String, Integer> filteredMap = map.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        log.debug("Filtered map after removing zeros: {}", filteredMap);
        return filteredMap;
    }
}
