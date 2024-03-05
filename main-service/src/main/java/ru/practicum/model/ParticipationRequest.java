package ru.practicum.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import ru.practicum.model.enumerations.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester")
    private Users requester;
    @CreatedDate
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
