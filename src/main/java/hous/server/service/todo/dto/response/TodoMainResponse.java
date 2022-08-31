package hous.server.service.todo.dto.response;

import hous.server.common.util.DateUtils;
import hous.server.common.util.MathUtils;
import hous.server.domain.todo.OurTodoStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoMainResponse {

    private String date;
    private String dayOfWeek;
    private int progress;
    private int myTodosCnt;
    private int ourTodosCnt;
    private List<MyTodoInfo> myTodos;
    private List<OurTodoInfo> ourTodos;

    public static TodoMainResponse of(LocalDate today, List<MyTodoInfo> myTodos, List<OurTodoInfo> ourTodos) {
        int doneOurTodosCnt = (int) ourTodos.stream().filter(ourTodo -> ourTodo.getStatus() == OurTodoStatus.FULL_CHECK).count();
        return TodoMainResponse.builder()
                .date(DateUtils.nowMonthAndDay(today))
                .dayOfWeek(DateUtils.nowDayOfWeek(today))
                .progress(MathUtils.percent(doneOurTodosCnt, ourTodos.size()))
                .myTodosCnt(myTodos.size())
                .ourTodosCnt(ourTodos.size())
                .myTodos(myTodos)
                .ourTodos(ourTodos)
                .build();
    }
}
