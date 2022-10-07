package hous.server.service.home.dto.response;

import hous.server.common.util.MathUtils;
import hous.server.domain.personality.PersonalityColor;
import hous.server.domain.room.Room;
import hous.server.domain.rule.Rule;
import hous.server.domain.todo.OurTodoStatus;
import hous.server.domain.user.Onboarding;
import hous.server.service.todo.dto.response.OurTodoInfo;
import hous.server.service.todo.dto.response.TodoDetailInfo;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class HomeInfoResponse {

    private String userNickname;
    private String roomName;
    private String roomCode;
    private int progress;
    private int myTodosCnt;
    private List<String> myTodos;
    private List<String> ourRules;
    private List<HomieInfo> homies;

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class HomieInfo {
        private Long homieId;
        private String userNickname;
        private PersonalityColor color;
    }

    public static HomeInfoResponse of(Onboarding me, Room room, List<TodoDetailInfo> myTodos,
                                      List<OurTodoInfo> ourTodos, List<Rule> rules, List<Onboarding> participants) {
        int doneOurTodosCnt = (int) ourTodos.stream().filter(ourTodo -> ourTodo.getStatus() == OurTodoStatus.FULL_CHECK).count();
        return HomeInfoResponse.builder()
                .userNickname(me.getNickname())
                .roomName(room.getName())
                .roomCode(room.getCode())
                .progress(MathUtils.percent(doneOurTodosCnt, ourTodos.size()))
                .myTodosCnt(myTodos.size())
                .myTodos(myTodos.stream()
                        .limit(3)
                        .map(TodoDetailInfo::getTodoName)
                        .collect(Collectors.toList()))
                .ourRules(rules.stream()
                        .sorted(Comparator.comparing(Rule::getIdx))
                        .limit(3)
                        .map(Rule::getName)
                        .collect(Collectors.toList()))
                .homies(participants.stream()
                        .map(onboarding -> HomieInfo.builder()
                                .homieId(onboarding.getId())
                                .userNickname(onboarding.getNickname())
                                .color(onboarding.getPersonality().getColor())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
