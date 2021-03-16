package com.app.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.demo.models.Notification;
import com.app.demo.services.impls.NotificationSeviceImpl;
public class PushNotificationController {

    

    @GetMapping("/{id}")
    public Notification findNotiWhereUserId(@PathVariable("receiverId") Long receiverId) {
       return null;
    }
}