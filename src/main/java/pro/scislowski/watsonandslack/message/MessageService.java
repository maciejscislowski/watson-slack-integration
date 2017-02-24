package pro.scislowski.watsonandslack.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author Maciej.Scislowski@gmail.com
 */
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    private final ObjectMapper mapper;
    private RestTemplate restTemplate;
    private String backendUrl;

    public MessageService(RestTemplate restTemplate, String backendUrl) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.addMixIn(Intent.class, IntentMixIn.class);
    }

    public MessageResponse send(MessageRequest messageRequest) {
        HttpEntity<String> entity = new HttpEntity<>(toJson(messageRequest), getHeaders());
        String messageResponseJson = restTemplate.postForObject(backendUrl, entity, String.class);
        LOG.info("messageResponse: " + messageResponseJson);

        return fromJson(messageResponseJson);
    }

    private MultiValueMap<String, String> getHeaders() {
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return requestHeaders;
    }

    private MessageResponse fromJson(String answer) {
        MessageResponse messageResponse = null;
        try {
            messageResponse = mapper.readValue(answer, MessageResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageResponse;
    }

    private String toJson(MessageRequest messageRequest) {
        String incomingMessage = null;
        try {
            incomingMessage = mapper.writeValueAsString(messageRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return incomingMessage;
    }
}
