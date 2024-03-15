package ru.practicum.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.user.NewUserRequest;
import ru.practicum.stats.dto.user.UserDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.Users;
import ru.practicum.service.UsersService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin: Пользователи", description = "API для работы с пользователями")
public class UsersAdminController {

    private final UsersService usersService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Добавление нового пользователя")
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        Users user = UserMapper.mapnewUserRequestToUsers(newUserRequest);
        return UserMapper.mapUsersToUserDto(usersService.createUser(user));
    }

    @GetMapping
    @Operation(
            summary = "Получение информации о пользователях",
            description = "Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы).\n" +
                    "В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список"
    )
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        return usersService.getUsers(ids, from, size)
                .stream()
                .map(UserMapper::mapUsersToUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление пользователя")
    public void deleteUser(@PathVariable @Positive Integer userId) {
        usersService.deleteUser(userId);
    }
}
