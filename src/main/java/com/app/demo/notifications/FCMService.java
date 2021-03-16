package com.app.demo.notifications;

import org.springframework.stereotype.Service;

import com.app.demo.dtos.NotifyRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FCMService {
	public String pushNotification(String receiverToken, NotifyRequest request, String senderId, String receiverId) {
		Message message = Message.builder().setToken(receiverToken)
				.setNotification(Notification.builder().setTitle(request.getTitle()).setBody(request.getBody()).build())
				.putData("sender", senderId).putData("receiver", receiverId)
				.putData("click_action", "NOTIFICATION_CLICK").build();
		String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

	}

