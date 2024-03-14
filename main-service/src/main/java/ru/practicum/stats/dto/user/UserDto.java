package ru.practicum.stats.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "Идентификатор")
    private Integer id;
//    @NotBlank
//    @Size(min = 2, max = 250, message = "name < 2 или > 250 симвлов")
    private String name;
//    @NotBlank
//    @Email(message = "Не верный формат email")
//    @Size(min = 6,max = 254, message = "email <6 или > 254 симвлов")
    private String email;

}
