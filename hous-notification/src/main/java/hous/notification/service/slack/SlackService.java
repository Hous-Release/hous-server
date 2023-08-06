package hous.notification.service.slack;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SlackService {

	@Value(value = "${spring.profiles.active}")
	String profile;
	@Value(value = "${slack.token}")
	String token;
	@Value(value = "${slack.channel.monitor}")
	String channelProductError;
	@Value(value = "${slack.channel.feedback}")
	String channelUserFeedback;

	private static final String LOCAL = "local";
	private static final String PROD_ERROR_MESSAGE_TITLE = "🤯 *500 에러 발생*";
	private static final String USER_FEEDBACK_TITLE = "🤯 *새로운 사용자 피드백*";
	private static final String ATTACHMENTS_ERROR_COLOR = "#eb4034";
	private static final String ATTACHMENTS_NOTIFICATION_COLOR = "#36a64f";

	public void sendSlackMessageProductError(String instance, Exception exception) {
		if (!profile.equals(LOCAL)) {
			try {
				Slack slack = Slack.getInstance();
				List<LayoutBlock> layoutBlocks = SlackServiceUtils.createProdErrorMessage(instance, exception);
				List<Attachment> attachments = SlackServiceUtils.createAttachments(ATTACHMENTS_ERROR_COLOR,
					layoutBlocks);
				slack.methods(token).chatPostMessage(req ->
					req.channel(channelProductError)
						.attachments(attachments)
						.text(PROD_ERROR_MESSAGE_TITLE));
			} catch (SlackApiException | IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public void sendSlackMessageUserFeedback(String comment) {
		if (!profile.equals(LOCAL)) {
			try {
				Slack slack = Slack.getInstance();
				List<LayoutBlock> layoutBlocks = SlackServiceUtils.createUserFeedbackMessage(comment);
				List<Attachment> attachments = SlackServiceUtils.createAttachments(ATTACHMENTS_NOTIFICATION_COLOR,
					layoutBlocks);
				slack.methods(token).chatPostMessage(req ->
					req.channel(channelUserFeedback)
						.attachments(attachments)
						.text(USER_FEEDBACK_TITLE));
			} catch (SlackApiException | IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
