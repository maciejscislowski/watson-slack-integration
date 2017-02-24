package pro.scislowski.watsonandslack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import pro.scislowski.watsonandslack.bot.SlackBot;
import pro.scislowski.watsonandslack.message.MessageService;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "pro.scislowski.watsonandslack"})
public class SlackIntegrationApplication {

    @Value("${slackBotToken}")
    private String slackToken;

    @Value("${backendUrl}")
    private String backendUrl;

    public static void main(String[] args) {
        SpringApplication.run(SlackIntegrationApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return new RestTemplate();
    }

    @Bean
    public MessageService messageService() {
        return new MessageService(restTemplate(), backendUrl);
    }

    @Bean
    public SlackBot slackBot() {
        return new SlackBot(messageService(), slackToken);
    }

}
