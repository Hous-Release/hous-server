package hous.core.domain.todo.mysql;

import java.time.LocalDate;

import hous.core.domain.todo.Done;
import hous.core.domain.todo.OurTodoStatus;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.Onboarding;

public interface DoneRepositoryCustom {

	boolean findTodayTodoCheckStatus(LocalDate today, Onboarding onboarding, Todo todo);

	OurTodoStatus findTodayOurTodoStatus(LocalDate today, Todo todo);

	Done findTodayDoneByOnboardingAndTodo(LocalDate today, Onboarding onboarding, Todo todo);

	boolean existsDayDoneByOnboardingAndTodo(LocalDate today, Onboarding onboarding, Todo todo);
}
