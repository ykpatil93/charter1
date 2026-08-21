package com.charter.rewards.controller;

import java.time.LocalDate;
import java.util.List;

import com.charter.rewards.dto.CustomerRewardResponse;
import com.charter.rewards.service.RewardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

  
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

   
    @GetMapping
    public ResponseEntity<List<CustomerRewardResponse>> getRewards(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(rewardService.calculateRewards(from, to));
    }
}
