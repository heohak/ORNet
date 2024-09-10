package com.demo.bait.service.DeviceServices;

import com.demo.bait.entity.Device;
import com.demo.bait.entity.classificator.DeviceClassificator;
import com.demo.bait.repository.DeviceRepo;
import com.demo.bait.repository.classificator.DeviceClassificatorRepo;
import com.demo.bait.specification.DeviceSpecification;
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

    public Map<String, Integer> getDevicesSummary() {
        Map<String, Integer> summaryMap = new HashMap<>();

        Integer allDevices = Math.toIntExact(deviceRepo.count());
        summaryMap.put("All Devices", allDevices);

        for (DeviceClassificator classificator : deviceClassificatorRepo.findAll()) {
            Specification<Device> classificatorSpec = DeviceSpecification.hasClassificatorId(classificator.getId());
            Integer sum = deviceRepo.findAll(classificatorSpec).size();
            summaryMap.put(classificator.getName(), sum);
        }

        return sortMapWithSpecialFirst(summaryMap, "All Devices");
    }

    public Map<String, Integer> getClientDevicesSummary(Integer clientId) {
        Map<String, Integer> clientSummaryMap = new HashMap<>();

        Specification<Device> clientSpec = DeviceSpecification.hasClientId(clientId);
        List<Device> allClientDevices = deviceRepo.findAll(clientSpec);
        clientSummaryMap.put("All Client Devices", allClientDevices.size());

        for (DeviceClassificator classificator : deviceClassificatorRepo.findAll()) {
            Specification<Device> classificatorSpec = DeviceSpecification.hasClassificatorId(classificator.getId());
            Specification<Device> combinedSpec = clientSpec.and(classificatorSpec);
            Integer sum = deviceRepo.findAll(combinedSpec).size();

            clientSummaryMap.put(classificator.getName(), sum);
        }

        Map<String, Integer> newClientSummaryMap = removeZeroValues(clientSummaryMap);

        return sortMapWithSpecialFirst(newClientSummaryMap, "All Client Devices");
    }

    public static Map<String, Integer> sortMapWithSpecialFirst(Map<String, Integer> map, String specialKey) {
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

        return finalMap;
    }

    public static Map<String, Integer> removeZeroValues(Map<String, Integer> map) {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}
