package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;

public class MailWSClientMain {
    public static void main(String[] args) {
        MailWSClient.sendBulk(
                ImmutableSet.of(new Addressee("To <ipodskochin@mail.ru>")), "Subject", "BodyBulk");

        MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <ipodskochin@mail.ru>")),
                ImmutableSet.of(new Addressee("Copy <ipodskochin@mail.ru>")), "Subject", "BodyGroup");
    }
}