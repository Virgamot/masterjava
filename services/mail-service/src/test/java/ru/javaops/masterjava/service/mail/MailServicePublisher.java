package ru.javaops.masterjava.service.mail;

import ru.javaops.masterjava.persist.DBITestProvider;

import javax.xml.ws.Endpoint;

/**
 * User: gkislin
 * Date: 28.05.2014
 */
public class MailServicePublisher {
    static {
        DBITestProvider.initDBI();
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/mail/mailService", new MailServiceImpl());
    }
}
