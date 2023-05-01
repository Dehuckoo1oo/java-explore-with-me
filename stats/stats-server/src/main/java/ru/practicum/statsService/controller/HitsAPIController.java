package ru.practicum.statsService.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsDTO.HitDTO;
import ru.practicum.statsDTO.StatsDTO;
import ru.practicum.statsService.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class HitsAPIController {

    private final HitService hitService;

    public HitsAPIController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDTO hit(@RequestBody HitDTO hitDTO) {
        return hitService.createHit(hitDTO);
    }

    @GetMapping("/stats")
    public List<StatsDTO> getStats(@RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime start,
                                   @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime end,
                                   @RequestParam(name = "uris", required = false) List<String> uris,
                                   @RequestParam(name = "unique", required = false, defaultValue = "false")
                                              Boolean unique) {
        return hitService.getStats(start, end, uris, unique);
    }
}
