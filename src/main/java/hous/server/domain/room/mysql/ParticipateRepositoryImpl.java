package hous.server.domain.room.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.room.QParticipate.participate;

@RequiredArgsConstructor
public class ParticipateRepositoryImpl implements ParticipateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByOnboarding(Onboarding onboarding) {
        return queryFactory.selectOne()
                .from(participate)
                .where(participate.onboarding.eq(onboarding))
                .fetchFirst() != null;
    }
}
