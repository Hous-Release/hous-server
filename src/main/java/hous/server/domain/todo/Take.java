package hous.server.domain.todo;

import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.user.Onboarding;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Take extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    private Todo todo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_id")
    private Onboarding onboarding;

    @OneToMany(mappedBy = "take", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Redo> redos = new ArrayList<>();

    public static Take newInstance(Todo todo, Onboarding onboarding) {
        return Take.builder()
                .todo(todo)
                .onboarding(onboarding)
                .build();
    }

    public void addRedo(Redo redo) {
        this.redos.add(redo);
    }
}
