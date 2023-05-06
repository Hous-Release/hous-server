package hous.core.domain.todo.mysql;

import static hous.core.domain.todo.QDone.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.querydsl.jpa.impl.JPAQueryFactory;

import hous.common.util.DateUtils;
import hous.core.domain.todo.Done;
import hous.core.domain.todo.OurTodoStatus;
import hous.core.domain.todo.Take;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.Onboarding;
import lombok.RequiredArgsConstructor;

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
		if (lastDone == null) {
			return false;
		}
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
		int doneCnt = (int)takes.stream()
			.map(take -> queryFactory.selectFrom(done)
				.where(
					done.onboarding.eq(take.getOnboarding()),
					done.todo.eq(todo)
				)
				.orderBy(done.createdAt.desc())
				.fetchFirst())
			.filter(lastDone -> lastDone != null && DateUtils.isSameDate(lastDone.getCreatedAt(), today))
			.count();
		if (doneCnt == 0) {
			return OurTodoStatus.EMPTY;
		} else if (doneCnt == takes.size()) {
			return OurTodoStatus.FULL_CHECK;
		} else {
			return OurTodoStatus.FULL;
		}
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
		if (lastDone == null) {
			return false;
		}
		return DateUtils.isSameDate(lastDone.getCreatedAt(), day);
	}
}
