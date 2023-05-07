package ru.practicum.statsServer.service;

import org.springframework.stereotype.Service;
import ru.practicum.statsDTO.HitDTO;
import ru.practicum.statsDTO.StatsDTO;
import ru.practicum.statsServer.mapper.HitMapper;
import ru.practicum.statsServer.model.Hit;
import ru.practicum.statsServer.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    public HitServiceImpl(HitRepository hitRepository, HitMapper hitMapper) {
        this.hitRepository = hitRepository;
        this.hitMapper = hitMapper;
    }

    @Override
    public List<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        boolean isUrisEmpty = uris == null;
        List<Hit> hits;
        if (unique) {
            if (isUrisEmpty) {
                hits = hitRepository.findByTimestampBetweenUnique(start, end);
            } else {
                hits = hitRepository.findByTimestampBetweenAndUrisUnique(start, end, uris);
            }
        } else {
            if (isUrisEmpty) {
                hits = hitRepository.findByTimestampBetween(start, end);
            } else {
                hits = hitRepository.findByTimestampBetweenAndUris(start, end, uris);
            }
        }
        Map<String, StatsDTO> statsMap = new HashMap<>();
        for (Hit hit : hits) {
            String key = hit.getApp() + hit.getUri();
            StatsDTO statsDTO = statsMap.get(key);
            if (statsDTO == null) {
                statsDTO = new StatsDTO(hit.getApp(), hit.getUri(), 1);
                statsMap.put(key, statsDTO);
            } else {
                statsDTO.setHits(statsDTO.getHits() + 1);
            }
        }
        List<StatsDTO> statsList = new ArrayList<>(statsMap.values());
        statsList.sort(Comparator.comparingInt(StatsDTO::getHits).reversed());
        return statsList;
    }

    @Override
    public HitDTO createHit(HitDTO hitDTO) {
        return hitMapper.mapEntityToDTO(hitRepository.save(hitMapper.mapDTOToEntity(hitDTO)));
    }
}
