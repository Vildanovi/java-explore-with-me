package ru.practicum.stats.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.stats.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsResponseDto {
    private int id;
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;
}
