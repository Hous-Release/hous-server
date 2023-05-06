package hous.core.domain.personality.mysql;

import static hous.core.domain.personality.QPersonalityTest.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.core.domain.personality.PersonalityTest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PersonalityTestRepositoryImpl implements PersonalityTestRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<PersonalityTest> findAllPersonalityTest() {
		return queryFactory.selectFrom(personalityTest)
			.orderBy(personalityTest.idx.asc())
			.fetch();
	}
}
