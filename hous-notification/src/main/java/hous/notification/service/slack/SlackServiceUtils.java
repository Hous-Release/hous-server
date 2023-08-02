package hous.notification.service.slack;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;

import java.util.ArrayList;
import java.util.List;

import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.TextObject;

public class SlackServiceUtils {

	private static final String PROD_ERROR_INSTANCE = "*Error Instance:*\n";
	private static final String PROD_ERROR_MESSAGE = "*Error Message:*\n";
	private static final String PROD_ERROR_STACK = "*Error Stack:*\n";
	private static final String FILTER_STRING = "hous";

	public static List<Attachment> createAttachments(String color, List<LayoutBlock> data) {
		List<Attachment> attachments = new ArrayList<>();
		Attachment attachment = new Attachment();
		attachment.setColor(color);
		attachment.setBlocks(data);
		attachments.add(attachment);
		return attachments;
	}

	public static List<LayoutBlock> createProdErrorMessage(String instance, Exception exception) {
		StackTraceElement[] stacks = exception.getStackTrace();

		List<LayoutBlock> layoutBlockList = new ArrayList<>();

		List<TextObject> sectionInFields = new ArrayList<>();
		sectionInFields.add(markdownText(PROD_ERROR_INSTANCE + instance));
		sectionInFields.add(markdownText(PROD_ERROR_MESSAGE + exception.getMessage()));
		sectionInFields.add(markdownText(PROD_ERROR_STACK + exception));
		layoutBlockList.add(section(section -> section.fields(sectionInFields)));

		layoutBlockList.add(divider());
		layoutBlockList.add(section(section -> section.text(markdownText(filterErrorStack(stacks)))));
		return layoutBlockList;
	}

	public static List<LayoutBlock> createUserDeleteFeedbackMessage(String comment) {
		List<LayoutBlock> layoutBlockList = new ArrayList<>();
		layoutBlockList.add(section(section -> section.text(markdownText(comment))));
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
