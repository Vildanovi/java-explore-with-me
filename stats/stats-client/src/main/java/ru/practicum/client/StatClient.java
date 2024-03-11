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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Service
public class StatClient {

    protected final RestTemplate restTemplate;

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
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uris", String.join(",", uris));
        parameters.put("unique", unique);

        return restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                    HttpMethod.GET, null, typeRef, parameters).getBody();
    }
}




//@Service
//public class StatClient extends BaseClient {
//
//    @Autowired
//    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
//        super(
//                builder
//                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
//                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                        .build()
//        );
//    }
//
//    public void createHit(EndPointHitDto endPointHitDto) {
//        post("/hit", endPointHitDto);
//    }
//
//    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
//                                           List<String> uris, boolean unique) {
//        Map<String, Object> parameters = Map.of(
//                "start", start,
//                "end", end,
//                "uris", String.join(",", uris),
//                "unique", unique
//        );
//        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
//
//    }
//}







//@Service
//public class StatClient {
//    private final RestTemplate restTemplate;
//
//    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
//        restTemplate = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
//                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                .build();
//    }
//
//    public void createHit(EndPointHitDto endPointHitDto) {
//        restTemplate.exchange("/hit", HttpMethod.POST, new HttpEntity<>(endPointHitDto), Void.class);
//    }
//
//    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
//        Map<String, Object> params = Map.of(
//                "start", start,
//                "end", end,
//                "uris", String.join(",", uris),
//                "unique", unique
//        );
//
//        return restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET,
//                null, new ParameterizedTypeReference<List<ViewStatsDto>>() {
//                }, params).getBody();
//    }
//}








//@Service
//public class StatClient extends BaseClient {
//
//    @Autowired
//    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
//        super(
//                builder
//                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
//                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
//                        .build()
//        );
//    }
//
//    public ResponseEntity<Object> createHit(EndPointHitDto endPointHitDto) {
//        return post("/hit", endPointHitDto);
//    }
//
//    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
//                                                 List<String> uris, boolean unique) {
//        Map<String, Object> parameters = Map.of(
//                "start", start,
//                "end", end,
//                "uris", String.join(",", uris),
//                "unique", unique
//        );
//        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
//    }
//}
