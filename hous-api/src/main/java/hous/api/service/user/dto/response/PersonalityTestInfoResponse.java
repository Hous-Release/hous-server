package hous.api.service.user.dto.response;

import hous.core.domain.personality.PersonalityTest;
import hous.core.domain.personality.QuestionType;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class PersonalityTestInfoResponse {

    private int index;
    private List<String> question;
    private QuestionType questionType;
    private List<List<String>> answers;
    private String imageUrl;

    public static PersonalityTestInfoResponse of(PersonalityTest personalityTest) {
        return PersonalityTestInfoResponse.builder()
                .index(personalityTest.getIdx())
                .question(splitToListByLineBreak(personalityTest.getQuestion()))
                .questionType(personalityTest.getQuestionType())
                .answers(splitTwiceToListByLineBreak(personalityTest.getAnswers()))
                .imageUrl(personalityTest.getImageUrl())
                .build();
    }

    private static List<String> splitToListByLineBreak(String content) {
        return Arrays.stream(content.split("\n")).collect(Collectors.toList());
    }

    private static List<List<String>> splitTwiceToListByLineBreak(String content) {
        List<String> splitOnceList = Arrays.stream(content.split("\n")).collect(Collectors.toList());
        return splitOnceList.stream().map(splitOnce ->
                Arrays.stream(splitOnce.split("\t")).collect(Collectors.toList())
        ).collect(Collectors.toList());
    }
}
