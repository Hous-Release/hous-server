package hous.server.domain.user;

import hous.server.domain.common.AuditingTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
