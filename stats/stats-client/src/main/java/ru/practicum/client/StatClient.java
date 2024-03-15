package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndPointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Service
public class StatClient {

    protected final RestTemplate restTemplate;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void createHit(EndPointHitDto endPointHitDto) {
        HttpEntity<EndPointHitDto> requestHttpEntity = new HttpEntity<>(endPointHitDto);
        restTemplate.exchange("/hit", HttpMethod.POST, requestHttpEntity, Void.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                       Collection<String> uris, boolean unique) {
        ParameterizedTypeReference<List<ViewStatsDto>> typeRef = new ParameterizedTypeReference<>() {
        };
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DATE_TIME_FORMATTER));
        parameters.put("end", end.format(DATE_TIME_FORMATTER));
        parameters.put("uris", String.join(",", uris));
        parameters.put("unique", unique);

        return restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    HttpMethod.GET, null, typeRef, parameters).getBody();
    }
}
