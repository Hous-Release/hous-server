package hous.server.service.slack;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
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

    @Value(value = "${spring.profiles.default}")
    String profile;
    @Value(value = "${slack.token}")
    String token;
    @Value(value = "${slack.channel.monitor}")
    String channel;

    private static final String SLACK_MESSAGE_TITLE = "🤯 *500 에러 발생*";
    private static final String ATTACHMENTS_COLOR = "#eb4034";
    private static final String FILTER_STRING = "hous.server";
    private static final String SLACK_ERROR_MESSAGE = "*Error Message:*\n";
    private static final String SLACK_ERROR_STACK = "*Error Stack:*\n";

    public void sendSlackMessage(Exception exception) {
        if (profile.equals("prod")) {
            try {
                Slack slack = Slack.getInstance();
                List<Attachment> attachments = createSlackAttachment(exception);
                slack.methods(token).chatPostMessage(req ->
                        req.channel(channel)
                                .attachments(attachments)
                                .text(SLACK_MESSAGE_TITLE));
            } catch (SlackApiException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private List<Attachment> createSlackAttachment(Exception exception) {
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachment.setColor(ATTACHMENTS_COLOR);
        attachment.setBlocks(createSlackMessage(exception));
        attachments.add(attachment);
        return attachments;
    }


    private List<LayoutBlock> createSlackMessage(Exception exception) {
        StackTraceElement[] stacks = exception.getStackTrace();

        List<LayoutBlock> layoutBlockList = new ArrayList<>();

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
