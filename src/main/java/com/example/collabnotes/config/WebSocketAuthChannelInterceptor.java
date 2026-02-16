package com.example.collabnotes.config;

import com.example.collabnotes.security.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.util.Map;

@Configuration
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final String ACCESS_TOKEN_ATTR = "ACCESS_TOKEN";
    private final JwtService jwtService;

    public WebSocketAuthChannelInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = extractTokenFromStompHeaderAccessor(accessor);

            if (token != null && jwtService.isValidToken(token)) {
                String userId = jwtService.extractUserIdFromToken(token);
                Principal principal = () -> userId;
                accessor.setUser(principal);
            }
        }
        return message;
    }

    private String extractTokenFromStompHeaderAccessor(StompHeaderAccessor accessor) {
        Map<String, Object> attrs = accessor.getSessionAttributes();
        if (attrs != null) {
            Object tokenObj = attrs.get(ACCESS_TOKEN_ATTR);
            if (tokenObj instanceof String token && !token.isBlank()) {
                return token;
            }
        }

        String headerToken = accessor.getFirstNativeHeader("X-Access-Token");
        if (headerToken != null && !headerToken.isBlank()) {
            return headerToken;
        }

        return null;
    }
}
