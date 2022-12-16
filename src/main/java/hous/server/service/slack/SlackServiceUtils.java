package hous.server.service.slack;

import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.TextObject;
import hous.server.service.slack.dto.response.UserDelete;
import hous.server.service.slack.dto.response.UserDeleteResponse;

import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

public class SlackServiceUtils {

    private static final String PROD_ERROR_MESSAGE = "*Error Message:*\n";
    private static final String PROD_ERROR_STACK = "*Error Stack:*\n";
    private static final String FILTER_STRING = "hous.server";
    private static final String PROD_USER_DELETE_TOTAL_COUNT_MESSAGE = "*피드백을 남긴 총 탈퇴 인원:* ";
    private static final String PROD_USER_DELETE_MESSAGE = "*피드백 유형별 탈퇴 인원:*\n";
    private static final String PROD_NOW_USER_DELETE_COMMENT = "*지금 탈퇴한 유저의 의견:*\n";

    public static List<Attachment> createAttachments(String color, List<LayoutBlock> data) {
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachment.setColor(color);
        attachment.setBlocks(data);
        attachments.add(attachment);
        return attachments;
    }

    public static List<LayoutBlock> createUserDeleteMessage(UserDeleteResponse userDeleteResponse) {
        List<LayoutBlock> layoutBlockList = new ArrayList<>();
        layoutBlockList.add(section(section ->
                section.text(markdownText(PROD_USER_DELETE_TOTAL_COUNT_MESSAGE + userDeleteResponse.getTotalDeleteUserCount()))));

        StringBuilder stringBuilder = new StringBuilder();
        for (UserDelete userDelete : userDeleteResponse.getTotalDeleteUserList()) {
            stringBuilder.append(userDelete.toString());
            stringBuilder.append('\n');
        }
        layoutBlockList.add(section(section ->
                section.text(markdownText(PROD_USER_DELETE_MESSAGE + stringBuilder.toString()))));

        if (userDeleteResponse.getComment() != null) {
            layoutBlockList.add(section(section ->
                    section.text(markdownText(PROD_NOW_USER_DELETE_COMMENT + userDeleteResponse.getComment()))));
        }

        return layoutBlockList;
    }

    public static List<LayoutBlock> createProdErrorMessage(Exception exception) {
        StackTraceElement[] stacks = exception.getStackTrace();

        List<LayoutBlock> layoutBlockList = new ArrayList<>();

        List<TextObject> sectionInFields = new ArrayList<>();
        sectionInFields.add(markdownText(PROD_ERROR_MESSAGE + exception.getMessage()));
        sectionInFields.add(markdownText(PROD_ERROR_STACK + exception));
        layoutBlockList.add(section(section -> section.fields(sectionInFields)));

        layoutBlockList.add(divider());
        layoutBlockList.add(section(section -> section.text(markdownText(filterErrorStack(stacks)))));
        return layoutBlockList;
    }

    private static String filterErrorStack(StackTraceElement[] stacks) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        for (StackTraceElement stack : stacks) {
            if (stack.toString().contains(FILTER_STRING)) {
                stringBuilder.append(stack).append("\n");
            }
        }
        stringBuilder.append("```");
        return stringBuilder.toString();
    }
}
