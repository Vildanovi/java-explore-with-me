package ru.practicum.mapper;

import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.Event;

public class EventMapper {

    public static EventShortDto mapEventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapCategoriesToCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.mapUsersToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .build();
    }
}
