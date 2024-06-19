package Auction_shop.auction.chat.controller;

import Auction_shop.auction.chat.domain.Chat;
import Auction_shop.auction.chat.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{roomId}")   // hello -> chat
    public void greeting(@DestinationVariable String roomId,Chat chat) throws Exception {
        Thread.sleep(1000);
        String destination = "/sub/greetings/" + roomId;
        log.debug("destination = {}", destination);
        log.info("roomId={}", roomId);
        log.info("chat={}", chat.getMessages());
        Greeting greeting = new Greeting("hello," + HtmlUtils.htmlEscape(chat.getMessages()));
        messagingTemplate.convertAndSend(destination, greeting.getContent());
    }
}
