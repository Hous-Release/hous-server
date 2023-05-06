package hous.api.service.todo.dto.response;

import java.time.LocalDate;
import java.util.List;

import hous.common.util.DateUtils;
import hous.common.util.MathUtils;
import hous.core.domain.todo.OurTodoStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
	private List<TodoDetailInfo> myTodos;
	private List<OurTodoInfo> ourTodos;

	public static TodoMainResponse of(LocalDate today, List<TodoDetailInfo> myTodos, List<OurTodoInfo> ourTodos) {
		int doneOurTodosCnt = (int)ourTodos.stream()
			.filter(ourTodo -> ourTodo.getStatus() == OurTodoStatus.FULL_CHECK)
			.count();
		return TodoMainResponse.builder()
			.date(DateUtils.parseMonthAndDay(today))
			.dayOfWeek(DateUtils.nowDayOfWeek(today))
			.progress(MathUtils.percent(doneOurTodosCnt, ourTodos.size()))
			.myTodosCnt(myTodos.size())
			.ourTodosCnt(ourTodos.size())
			.myTodos(myTodos)
			.ourTodos(ourTodos)
			.build();
	}
}
