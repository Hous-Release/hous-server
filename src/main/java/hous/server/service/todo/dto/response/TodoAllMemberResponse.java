package hous.server.service.todo.dto.response;

import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.user.Onboarding;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TodoAllMemberResponse {

    private String userName;
    private PersonalityColor color;
    private int totalTodoCnt;
    private List<DayOfWeekTodo> dayOfWeekTodos;

    public static TodoAllMemberResponse of(String userName, PersonalityColor color, int totalTodoCnt, List<DayOfWeekTodo> dayOfWeekTodos) {
        return TodoAllMemberResponse.builder()
                .userName(userName)
                .color(color)
                .totalTodoCnt(totalTodoCnt)
                .dayOfWeekTodos(dayOfWeekTodos)
                .build();
    }
    
    public static List<TodoAllMemberResponse> sortMeFirst(List<TodoAllMemberResponse> allMemberTodosList, Onboarding me) {
        for (int i = 0; i < allMemberTodosList.size(); i++) {
            if (allMemberTodosList.get(i).getUserName().equals(me.getNickname())) {
                TodoAllMemberResponse todoInfo = allMemberTodosList.get(i);
                allMemberTodosList.remove(i);
                allMemberTodosList.add(0, todoInfo);
                break;
            }
        }
        return allMemberTodosList;
    }
}
