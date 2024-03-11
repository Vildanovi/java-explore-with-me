package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.model.enumerations.StateEvent;
import ru.practicum.stats.dto.locations.LocationDto;
import ru.practicum.stats.dto.event.EventFullDto;
import ru.practicum.stats.dto.event.EventShortDto;
import ru.practicum.stats.dto.event.NewEventDto;
import ru.practicum.model.Event;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public Event mapNewEventDtoToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .createdOn(LocalDateTime.now())
                .eventDate(newEventDto.getEventDate())
                .state(StateEvent.PENDING)
                .lon(newEventDto.getLocation().getLon())
                .lat(newEventDto.getLocation().getLat())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public EventShortDto mapEventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapCategoriesToCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(UserMapper.mapUsersToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequest())
                .build();
    }

    public EventFullDto mapEventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapCategoriesToCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.mapUsersToUserShortDto(event.getInitiator()))
                .location(LocationDto.builder()
                        .lat(event.getLat())
                        .lon(event.getLon())
                        .build())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequest())
                .build();
    }
}
