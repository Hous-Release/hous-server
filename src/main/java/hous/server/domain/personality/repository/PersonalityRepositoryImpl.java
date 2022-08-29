package hous.server.domain.personality.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.personality.Personality;
import hous.server.domain.personality.PersonalityColor;
import lombok.RequiredArgsConstructor;

import static hous.server.domain.personality.QPersonality.personality;

@RequiredArgsConstructor
public class PersonalityRepositoryImpl implements PersonalityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Personality findPersonalityByColor(PersonalityColor color) {
        return queryFactory
                .selectFrom(personality)
                .where(personality.color.eq(color))
                .fetchOne();
    }
}
