package ru.practicum.explorewithme.event.model;

import lombok.*;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.location.model.Location;
import ru.practicum.explorewithme.partisipationrequest.model.ParticipationRequest;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private String annotation;
    private String description;
    private String title;
    private boolean paid;
    private LocalDateTime created;
    private LocalDateTime published;
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Integer participantLimit;
    private boolean requestModeration;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipationRequest> participationRequests;
}
