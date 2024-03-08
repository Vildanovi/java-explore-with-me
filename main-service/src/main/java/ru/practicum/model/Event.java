package ru.practicum.model;

import lombok.*;
import ru.practicum.model.enumerations.StateEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category", nullable = false)
    private Category category;
//    @CreatedDate
//    @Builder.Default
    @Column(name = "created", nullable = false)
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator", nullable = false)
    private Users initiator;
    private float lat;
    private float lon;
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published")
    private LocalDateTime publishedOn;
//    @Builder.Default
    @Column(name = "request_moderation")
    private boolean requestModeration = true;
    @Enumerated(EnumType.STRING)
//    @Builder.Default
    @Column(nullable = false)
    private StateEvent state = StateEvent.PENDING;
    @Transient
    private int views;
    @Transient
    private int confirmedRequest;

    @Transient
    public boolean isPublished() {
        return state.equals(StateEvent.PUBLISHED);
    }

    @Transient
    public boolean isCanceled() {
        return state.equals(StateEvent.CANCELED);
    }

    @Transient
    public boolean isPending() {
        return state.equals(StateEvent.PENDING);
    }
}