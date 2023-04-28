package ru.practicum.statsService.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statsDTO.HitDTO;
import ru.practicum.statsService.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    public Hit mapDTOToEntity(HitDTO hitDTO) {
        Hit hit = Hit.builder()
                .app(hitDTO.getApp())
                .uri(hitDTO.getUri())
                .ip(hitDTO.getIp())
                .timestamp(LocalDateTime.parse(hitDTO.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return hit;
    }

    public HitDTO mapEntityToDTO(Hit hit) {
        HitDTO hitDTO = HitDTO.builder()
                .app(hit.getApp())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .uri(hit.getUri())
                .build();
        return hitDTO;
    }
}
