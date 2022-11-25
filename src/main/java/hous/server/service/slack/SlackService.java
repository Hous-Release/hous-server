package hous.server.service.slack;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import hous.server.common.util.YamlPropertySourceFactory;
import hous.server.service.slack.dto.response.UserDeleteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@PropertySource(value = "classpath:application-slack.yml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
public class SlackService {

    @Value(value = "${spring.profiles.default}")
    String profile;
    @Value(value = "${slack.token}")
    String token;
    @Value(value = "${slack.channel.monitor}")
    String channelProductError;
    @Value(value = "${slack.channel.notification}")
    String channelDeleteUserNotification;

    private static final String PROD_ERROR_MESSAGE_TITLE = "🤯 *500 에러 발생*";
    private static final String PROD_USER_DELETE_MESSAGE_TITLE = "🤯 *현재 사용자 탈퇴 현황*";
    private static final String ATTACHMENTS_ERROR_COLOR = "#eb4034";
    private static final String ATTACHMENTS_NOTIFICATION_COLOR = "#36a64f";

    public void sendSlackMessageDeleteUser(UserDeleteResponse userDeleteResponse) {
        if (profile.equals("prod") && userDeleteResponse.getTotalDeleteUserList().isEmpty()) {
            try {
                Slack slack = Slack.getInstance();
                List<LayoutBlock> layoutBlocks = SlackServiceUtils.createUserDeleteMessage(userDeleteResponse);
                List<Attachment> attachments = SlackServiceUtils.createAttachments(ATTACHMENTS_NOTIFICATION_COLOR, layoutBlocks);
                slack.methods(token).chatPostMessage(req ->
                        req.channel(channelDeleteUserNotification)
                                .attachments(attachments)
                                .text(PROD_USER_DELETE_MESSAGE_TITLE));
            } catch (SlackApiException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void sendSlackMessageProductError(Exception exception) {
        if (profile.equals("prod")) {
            try {
                Slack slack = Slack.getInstance();
                List<LayoutBlock> layoutBlocks = SlackServiceUtils.createProdErrorMessage(exception);
                List<Attachment> attachments = SlackServiceUtils.createAttachments(ATTACHMENTS_ERROR_COLOR, layoutBlocks);
                slack.methods(token).chatPostMessage(req ->
                        req.channel(channelProductError)
                                .attachments(attachments)
                                .text(PROD_ERROR_MESSAGE_TITLE));
            } catch (SlackApiException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
