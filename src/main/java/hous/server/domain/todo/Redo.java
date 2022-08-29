package hous.server.domain.todo;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Redo extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "take_id")
    private Take take;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "redo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Done> dones = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Redo(Take take, DayOfWeek dayOfWeek) {
        this.take = take;
        this.dayOfWeek = dayOfWeek;
    }

    public static Redo newInstance(Take take, DayOfWeek dayOfWeek) {
        return Redo.builder()
                .take(take)
                .dayOfWeek(dayOfWeek)
                .build();
    }
}
