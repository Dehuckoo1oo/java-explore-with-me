package ru.practicum.statsServer.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statsDTO.HitDTO;
import ru.practicum.statsServer.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    public Hit mapDTOToEntity(HitDTO hitDTO) {
        LocalDateTime now = LocalDateTime.now();
        Hit hit = Hit.builder()
                .app(hitDTO.getApp())
                .uri(hitDTO.getUri())
                .ip(hitDTO.getIp())
                .timestamp(now)
                .build();
        return hit;
    }

    public HitDTO mapEntityToDTO(Hit hit) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localDateTime = hit.getTimestamp().format(dateTimeFormatter);
        HitDTO hitDTO = HitDTO.builder()
                .app(hit.getApp())
                .ip(hit.getIp())
                .timestamp(localDateTime)
                .uri(hit.getUri())
                .build();
        return hitDTO;
    }
}
