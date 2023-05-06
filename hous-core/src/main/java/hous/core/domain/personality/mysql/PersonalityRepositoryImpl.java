package hous.core.domain.personality.mysql;

import static hous.core.domain.personality.QPersonality.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.personality.Personality;
import hous.core.domain.personality.PersonalityColor;
import lombok.RequiredArgsConstructor;

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
