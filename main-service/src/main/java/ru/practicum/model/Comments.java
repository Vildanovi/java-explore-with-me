package ru.practicum.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.practicum.model.enumerations.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
//    @ToString.Exclude
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
//    @ToString.Exclude
    private Users author;
    @Column(name = "created_date")
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Comments comment = (Comments) o;
        return getId() != null && Objects.equals(getId(), comment.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
