package ru.practicum.statsServer.service;

import ru.practicum.statsDTO.HitDTO;
import ru.practicum.statsDTO.StatsDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    public List<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    public HitDTO createHit(HitDTO hitDTO);
}
