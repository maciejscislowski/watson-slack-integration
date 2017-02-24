package pro.scislowski.watsonandslack.bot;

import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.springframework.web.socket.WebSocketSession;
import pro.scislowski.watsonandslack.message.MessageService;

/**
 * @author Maciej.Scislowski@gmail.com
 */
public class SlackBot extends Bot {

    private String slackToken;
    private MessageService messageService;

    public SlackBot(MessageService messageService, String slackToken) {
        super();
        this.messageService = messageService;
        this.slackToken = slackToken;
    }

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }

    @Controller(events = {EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE})
    public void onReceiveDM(WebSocketSession session, Event event) {
        MessageRequest messageRequest = new MessageRequest.Builder().inputText(event.getText()).build();
        MessageResponse messageResponse = messageService.send(messageRequest);
        reply(session, event, new Message(messageResponse.getText().get(0)));
    }

}
