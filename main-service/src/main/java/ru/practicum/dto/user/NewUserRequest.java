package ru.practicum.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewUserRequest {
    @NotBlank
    @Size(min = 2, max = 250, message = "name < 2 или > 250 симвлов")
    private String name;
    @NotBlank
    @Email(message = "Не верный формат email")
    @Size(min = 6,max = 254, message = "email <6 или > 254 симвлов")
    private String email;
}
