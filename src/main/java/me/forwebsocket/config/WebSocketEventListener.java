package me.forwebsocket.config;

import me.forwebsocket.controller.CommunicationController;
import me.forwebsocket.model.LocationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    //method called when user open page in browser
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        logger.info("Received a new web socket connection");
    }

    //method called when user open page in browser
    public void handleWebSocketDisconnectListener(SessionConnectedEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username !=null){
            logger.info("User Disconnected : " + username);

            //remove user from latest Location Feed
            CommunicationController.latestLocationFeed.remove(username);

            //transmitting current user's latest location feed
            messagingTemplate.convertAndSend("/app/getData", new LocationBean());        }
    }

}
