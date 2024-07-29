package com.demo.bait.service;

import com.demo.bait.dto.PaidWorkDTO;
import com.demo.bait.dto.ResponseDTO;
import com.demo.bait.entity.PaidWork;
import com.demo.bait.mapper.PaidWorkMapper;
import com.demo.bait.repository.PaidWorkRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PaidWorkService {

    private PaidWorkRepo paidWorkRepo;
    private PaidWorkMapper paidWorkMapper;


    public PaidWork createPaidWork() {
        PaidWork paidWork = new PaidWork();
        paidWork.setStartTime(LocalDateTime.now());
        paidWork.setSettled(false);
        paidWorkRepo.save(paidWork);
        return paidWork;
    }

    public PaidWorkDTO addTimeToPaidWork(Integer paidWorkId, Integer hours, Integer minutes) {
        Optional<PaidWork> paidWorkOpt = paidWorkRepo.findById(paidWorkId);
        if (paidWorkOpt.isEmpty()) {
            throw new EntityNotFoundException("Paid work with id " + paidWorkId + " not found");
        }
        PaidWork paidWork = paidWorkOpt.get();
        Duration currentDuration = paidWork.getTimeSpent();
        if (currentDuration == null) {
            currentDuration = Duration.ZERO;
        }
        if (hours != null) {
            currentDuration = currentDuration.plusHours(hours);
        }
        if (minutes != null) {
            currentDuration = currentDuration.plusMinutes(minutes);
        }
        paidWork.setTimeSpent(currentDuration);
        paidWorkRepo.save(paidWork);
        return paidWorkMapper.toDto(paidWork);
    }
}
