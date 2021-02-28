package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;

public class MailWSClientMain {
    public static void main(String[] args) {
        MailWSClient.sendMail(
                ImmutableList.of(new Addressee("To <ipodskochin@mail.ru>")),
                ImmutableList.of(new Addressee("Copy <ipodskochin@mail.ru>")), "Subject", "Body");
    }
}