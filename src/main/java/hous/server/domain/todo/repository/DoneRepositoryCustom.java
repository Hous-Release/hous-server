package hous.server.domain.todo.repository;

import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.todo.Todo;
import hous.server.domain.user.Onboarding;

import java.time.LocalDate;

public interface DoneRepositoryCustom {

    boolean findTodayMyTodoCheckStatus(LocalDate now, Onboarding me, Todo todo);

    OurTodoStatus findTodayOurTodoStatus(LocalDate now, Todo todo);
}
