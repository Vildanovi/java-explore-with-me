package ru.practicum.controller.priv;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestsService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Private: Запросы на участие", description = "Закрытый API для работы с " +
        "запросами текущего пользователя на участие в событиях")
public class RequestsPrivateController {

    private final RequestsService requestsService;

    @GetMapping
    @Operation(
            summary = "Получение информации о заявках текущего пользователя на участие в чужих событиях",
            description = "В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список"
    )
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable @Positive Integer userId) {
        return requestsService.getRequestsByUser(userId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(
            summary = "Добавление запроса от текущего пользователя на участие в событии",
            description = "Обратите внимание:\n" +
                    "- нельзя добавить повторный запрос  (Ожидается код ошибки 409)\n" +
                    "- инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)\n" +
                    "- нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)\n" +
                    "- если у события достигнут лимит запросов на участие - необходимо вернуть ошибку  (Ожидается код ошибки 409)\n" +
                    "- если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного"
    )
    public ParticipationRequestDto createRequest(@PathVariable @Positive Integer userId,
                                                 @RequestParam @Positive Integer eventId) {
        return requestsService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    @Operation(
            summary = "Отмена своего запроса на участие в событии"
    )
    public ParticipationRequestDto cancelRequest(@PathVariable @Positive Integer userId,
                                                 @PathVariable @Positive Integer requestId) {
        return requestsService.cancelRequest(userId, requestId);
    }
}
