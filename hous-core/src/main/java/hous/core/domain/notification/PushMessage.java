package hous.core.domain.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PushMessage {

	NEW_RULE("새로운 Rules 추가", "우리 집에 새로운 Rules가 추가되었어요.\n어떤 규칙인지 확인해봐요!"),
	NEW_TODO("새로운 to-do 추가", "우리 집에 새로운 to-do가 추가되었어요.\n어떤 to-do인지 확인해봐요!"),
	NEW_TODO_TAKE("새로운 to-do 추가", "우리 집에 새로운 내 to-do가 추가되었어요.\n어떤 to-do인지 확인해봐요!"),
	TODAY_TODO_START("오늘의 to-do 시작", "상쾌한 Hous-의 아침!\n오늘은 어떤 to-do가 기다리고 있을까요?"),
	TODAY_TODO_TAKE_START("오늘의 to-do 시작", "상쾌한 Hous-의 아침!\n오늘은 어떤 to-do가 나를 기다리고 있을까요?"),
	TODO_REMIND("미완료 to-do 알림", "이런, 오늘의 to-do가 아직 끝나지 않았어요!\n쾌적한 우리 집을 위해, 지금 확인해봐요!"),
	TODO_TAKE_REMIND("미완료 to-do 알림", "이런, 오늘 해야 할 to-do가 아직 끝나지 않았어요!\n빠르게 확인하고, 후다닥 끝내볼까요?"),
	NEW_BADGE("배지 획득", "님, 축하해요!\n새로 받은 배지를 지금 확인해봐요!");

	private final String title;
	private final String body;

	public String getKey() {
		return name();
	}
}
