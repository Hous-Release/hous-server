package hous.core.domain.todo.mysql;

import hous.core.domain.todo.Done;
import hous.core.domain.todo.Todo;
import hous.core.domain.user.Onboarding;
import hous.core.domain.todo.OurTodoStatus;

import java.time.LocalDate;

public interface DoneRepositoryCustom {

    boolean findTodayTodoCheckStatus(LocalDate today, Onboarding onboarding, Todo todo);

    OurTodoStatus findTodayOurTodoStatus(LocalDate today, Todo todo);

    Done findTodayDoneByOnboardingAndTodo(LocalDate today, Onboarding onboarding, Todo todo);

    boolean existsDayDoneByOnboardingAndTodo(LocalDate today, Onboarding onboarding, Todo todo);
}
