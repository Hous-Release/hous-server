package hous.server.service.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hous.server.common.util.DateUtils;
import hous.server.common.util.MathUtils;
import hous.server.domain.common.AuditingTimeEntity;
import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.todo.Todo;
import hous.server.domain.todo.repository.DoneRepository;
import hous.server.domain.user.Onboarding;
import hous.server.service.todo.TodoServiceUtils;
import lombok.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GetTodoMainResponse {

    private String date;
    private String dayOfWeek;
    private int progress;
    private int myTodosCnt;
    private int ourTodosCnt;
    private List<MyTodo> myTodos;
    private List<OurTodo> ourTodos;

    @ToString
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    private static class MyTodo {
        private Long todoId;
        private String todoName;
        private boolean isChecked;

        @JsonProperty("isChecked")
        public boolean isChecked() {
            return isChecked;
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    private static class OurTodo {
        private String todoName;
        private OurTodoStatus status;
        private List<String> nicknames;
    }

    public static GetTodoMainResponse of(Onboarding me, LocalDate now, List<Todo> todos, DoneRepository doneRepository) {
        List<Todo> todayOurTodosList = TodoServiceUtils.filterTodayOurTodos(now, todos);
        List<Todo> todayMyTodosList = TodoServiceUtils.filterTodayMyTodos(now, me, todos);
        List<MyTodo> todayMyTodos = todayMyTodosList.stream()
                .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                .map(todo -> MyTodo.builder()
                        .todoId(todo.getId())
                        .todoName(todo.getName())
                        .isChecked(doneRepository.findTodayMyTodoCheckStatus(now, me, todo))
                        .build())
                .collect(Collectors.toList());
        List<OurTodo> todayOurTodos = todayOurTodosList.stream()
                .sorted(Comparator.comparing(AuditingTimeEntity::getCreatedAt))
                .map(todo -> OurTodo.builder()
                        .todoName(todo.getName())
                        .status(doneRepository.findTodayOurTodoStatus(now, todo))
                        .nicknames(todo.getTakes().stream()
                                .map(take -> take.getOnboarding().getNickname())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        int doneOurTodosCnt = (int) todayOurTodos.stream()
                .filter(ourTodo -> ourTodo.getStatus() == OurTodoStatus.CHECK).count();
        return GetTodoMainResponse.builder()
                .date(DateUtils.nowMonthAndDay(now))
                .dayOfWeek(DateUtils.nowDayOfWeek(now))
                .progress(MathUtils.percent(doneOurTodosCnt, todayOurTodos.size()))
                .myTodosCnt(todayMyTodos.size())
                .ourTodosCnt(todayOurTodos.size())
                .myTodos(todayMyTodos)
                .ourTodos(todayOurTodos)
                .build();
    }
}
