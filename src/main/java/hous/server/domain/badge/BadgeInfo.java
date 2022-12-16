package hous.server.domain.badge;

import hous.server.common.model.EnumModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BadgeInfo implements EnumModel {

    POUNDING_HOUSE("두근두근 하우스", "어서와요\n우리 Hous-에!"),
    I_AM_SUCH_A_PERSON("나 이런 사람이야", "Homie 카드를\n완성한 호미"),
    OUR_HOUSE_HOMIES("우리 집 호미들", "모든 Homie들의\n테스트 참여"),
    I_DONT_EVEN_KNOW_ME("나도 날 모르겠어", "성향 테스트를\n5번 이상 한 호미"),
    HOMIE_IS_BORN("Homie 탄생", "Hous-에서의\n첫 생일"),
    TODO_ONE_STEP("to-do 한 걸음", "to-do 전체보기에서\n첫 to-do 등록"),
    GOOD_JOB("참 잘했어요", "1주 동안 to-do\n모두 완료"),
    SINCERITY_KING_HOMIE("성실왕 호미", "2주 동안\nto-do 모두 완료"),
    TODO_MASTER("to-do 마스터", "3주 동안\nto-do 모두 완료"),
    LETS_BUILD_A_POLE("기둥을 세우자", "우리 집 규칙에서\n첫 Rules 등록"),
    OUR_HOUSE_PILLAR_HOMIE("우리 집 기둥 호미", "우리 집 Rules\n5개 이상 등록"),
    FEEDBACK_ONE_STEP("피드백 한 걸음", "좋은 의견을\n호미 나라에 전달");

    private final String value;

    private final String description;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
