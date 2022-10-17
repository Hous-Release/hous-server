package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TestScore extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int light;

    @Column(nullable = false)
    private int noise;

    @Column(nullable = false)
    private int clean;

    @Column(nullable = false)
    private int smell;

    @Column(nullable = false)
    private int introversion;

    public static TestScore newInstance() {
        return TestScore.builder()
                .light(0)
                .noise(0)
                .clean(0)
                .smell(0)
                .introversion(0)
                .build();
    }

    public void updateScore(int light, int noise, int clean, int smell, int introversion) {
        this.light = light;
        this.noise = noise;
        this.clean = clean;
        this.smell = smell;
        this.introversion = introversion;
    }

    public int getTotalTestScore() {
        return this.getLight() + this.getNoise() + this.getClean() + this.getSmell() + this.getIntroversion();
    }
}
