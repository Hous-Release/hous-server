package hous.server.service.slack;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.TextObject;
import hous.server.common.util.YamlPropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

@Slf4j
@Service
@PropertySource(value = "classpath:application-slack.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class SlackService {
    @Value(value = "${slack.token}")
    String token;
    @Value(value = "${slack.channel.monitor}")
    String channel;

    private static final String SLACK_MESSAGE_TITLE = "🤯 *500 에러 발생*";
    private static final String SLACK_NOTI_MESSAGE_TITLE = "호엥 에러 발생. 서버 다 모 여";
    private static final String FILTER_STRING = "hous.server";
    private static final String SLACK_ERROR_MESSAGE = "*Error Message:*\n";
    private static final String SLACK_ERROR_STACK = "*Error Stack:*\n";

    public void sendSlackMessage(Exception exception) {
        try {
            Slack slack = Slack.getInstance();
            List<LayoutBlock> layoutBlockList = createSlackMessage(exception);
            slack.methods(token).chatPostMessage(req ->
                    req.channel(channel)
                            .blocks(layoutBlockList)
                            .text(SLACK_NOTI_MESSAGE_TITLE));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    private List<LayoutBlock> createSlackMessage(Exception exception) {
        StackTraceElement[] stacks = exception.getStackTrace();

        List<LayoutBlock> layoutBlockList = new ArrayList<>();
        layoutBlockList.add(section(header -> header.text(markdownText(SLACK_MESSAGE_TITLE))));

        List<TextObject> sectionInFields = new ArrayList<>();
        sectionInFields.add(markdownText(SLACK_ERROR_MESSAGE + exception.getMessage()));
        sectionInFields.add(markdownText(SLACK_ERROR_STACK + exception));
        layoutBlockList.add(section(section -> section.fields(sectionInFields)));

        layoutBlockList.add(divider());
        layoutBlockList.add(section(section -> section.text(markdownText(filterErrorStack(stacks)))));
        return layoutBlockList;
    }

    private String filterErrorStack(StackTraceElement[] stacks) {
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
