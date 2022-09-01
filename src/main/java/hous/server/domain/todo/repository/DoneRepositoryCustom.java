package hous.server.domain.todo.repository;

import hous.server.domain.todo.Done;
import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;

import java.time.LocalDate;

public interface DoneRepositoryCustom {

    boolean findTodayTodoCheckStatus(LocalDate today, Onboarding onboarding, Todo todo);

    OurTodoStatus findTodayOurTodoStatus(LocalDate today, Todo todo);

    Done findTodayDoneByOnboardingAndTodo(LocalDate today, Onboarding onboarding, Todo todo);
}
