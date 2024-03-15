package ru.practicum.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import ru.practicum.model.enumerations.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event", nullable = false)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester", nullable = false)
    private Users requester;
    @CreatedDate
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Transient
    public boolean isDataMatchRequest(Integer eventId, Integer initiatorId) {
        return Objects.equals(event.getInitiator().getId(), initiatorId)
                && Objects.equals(event.getId(), eventId);
    }
}
