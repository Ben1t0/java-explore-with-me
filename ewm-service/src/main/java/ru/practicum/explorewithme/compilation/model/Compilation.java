package ru.practicum.explorewithme.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "compil_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Collection<Event> events = new ArrayList<>();
    private boolean pinned;
    private String title;
}
