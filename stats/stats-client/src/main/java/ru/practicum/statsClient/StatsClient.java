package ru.practicum.statsClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statsDTO.HitDTO;
import ru.practicum.statsDTO.StatsDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public Map<Integer, Integer> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder strUris = new StringBuilder();
        for (String str : uris) {
            strUris.append("/events/").append(str);
        }
        if (start == null) {
            start = LocalDateTime.now().minusYears(333);
        }
        if (end == null) {
            end = LocalDateTime.now().plusYears(333);
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(dateTimeFormatter),
                "end", end.format(dateTimeFormatter),
                "uris", strUris.toString(),
                "unique", unique
        );
        ResponseEntity<Object> objects = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        ObjectMapper objectMapper = new ObjectMapper();
        List<StatsDTO> stats = objectMapper.convertValue(objects.getBody(), new TypeReference<>() {
        });
        if (stats == null) {
            return new HashMap<>();
        } else {
            return stats.stream()
                    .collect(Collectors.toMap(x -> Integer.parseInt(x.getUri().split("/", 0)[2]), StatsDTO::getHits));
        }
    }

    public ResponseEntity<Object> hit(HitDTO hitRequestDto) {
        return post("/hit", hitRequestDto);
    }
}