package hous.server.domain.personality.mysql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.domain.personality.PersonalityTest;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static hous.server.domain.personality.QPersonalityTest.personalityTest;

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
