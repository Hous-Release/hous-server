package hous.server.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hous.server.common.util.DateUtils;
import hous.server.domain.todo.Done;
import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.todo.Take;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static hous.server.domain.todo.QDone.done;

@RequiredArgsConstructor
public class DoneRepositoryImpl implements DoneRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean findTodayMyTodoCheckStatus(LocalDate now, Onboarding me, Todo todo) {
        Done lastDone = queryFactory.selectFrom(done)
                .where(done.onboarding.eq(me))
                .orderBy(done.createdAt.desc())
                .fetchFirst();
        if (lastDone == null) return false;
        return DateUtils.isSameDate(lastDone.getCreatedAt(), now);
    }

    @Override
    public OurTodoStatus findTodayOurTodoStatus(LocalDate now, Todo todo) {
        List<Take> takes = todo.getTakes();
        int doneCnt = (int) takes.stream()
                .map(take -> queryFactory.selectFrom(done)
                        .where(done.onboarding.eq(take.getOnboarding()))
                        .orderBy(done.createdAt.desc())
                        .fetchFirst())
                .filter(lastDone -> lastDone != null && DateUtils.isSameDate(lastDone.getCreatedAt(), now))
                .count();
        if (doneCnt == 0) return OurTodoStatus.EMPTY;
        else if (doneCnt == takes.size()) return OurTodoStatus.FULL_CHECK;
        else return OurTodoStatus.FULL;
    }
}
