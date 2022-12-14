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
import java.util.HashSet;
import java.util.Set;

import static hous.server.domain.todo.QDone.done;

@RequiredArgsConstructor
public class DoneRepositoryImpl implements DoneRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean findTodayTodoCheckStatus(LocalDate today, Onboarding onboarding, Todo todo) {
        Done lastDone = queryFactory.selectFrom(done)
                .where(
                        done.onboarding.eq(onboarding),
                        done.todo.eq(todo)
                )
                .orderBy(done.createdAt.desc())
                .fetchFirst();
        if (lastDone == null) return false;
        return DateUtils.isSameDate(lastDone.getCreatedAt(), today);
    }

    @Override
    public OurTodoStatus findTodayOurTodoStatus(LocalDate today, Todo todo) {
        Set<Take> takes = new HashSet<>();
        todo.getTakes().forEach(take -> {
            take.getRedos().forEach(redo -> {
                if (redo.getDayOfWeek().toString().equals(DateUtils.nowDayOfWeek(today))) {
                    takes.add(take);
                }
            });
        });
        int doneCnt = (int) takes.stream()
                .map(take -> queryFactory.selectFrom(done)
                        .where(
                                done.onboarding.eq(take.getOnboarding()),
                                done.todo.eq(todo)
                        )
                        .orderBy(done.createdAt.desc())
                        .fetchFirst())
                .filter(lastDone -> lastDone != null && DateUtils.isSameDate(lastDone.getCreatedAt(), today))
                .count();
        if (doneCnt == 0) return OurTodoStatus.EMPTY;
        else if (doneCnt == takes.size()) return OurTodoStatus.FULL_CHECK;
        else return OurTodoStatus.FULL;
    }

    @Override
    public Done findTodayDoneByOnboardingAndTodo(LocalDate today, Onboarding onboarding, Todo todo) {
        Done lastDone = queryFactory.selectFrom(done)
                .where(
                        done.onboarding.eq(onboarding),
                        done.todo.eq(todo)
                )
                .orderBy(done.createdAt.desc())
                .fetchFirst();
        return DateUtils.isSameDate(lastDone.getCreatedAt(), today) ? lastDone : null;
    }

    @Override
    public boolean existsDayDoneByOnboardingAndTodo(LocalDate day, Onboarding onboarding, Todo todo) {
        Done lastDone = queryFactory.selectFrom(done)
                .where(
                        done.onboarding.eq(onboarding),
                        done.todo.eq(todo)
                )
                .orderBy(done.createdAt.desc())
                .fetchFirst();
        if (lastDone == null) return false;
        return DateUtils.isSameDate(lastDone.getCreatedAt(), day);
    }
}
