package ru.javaops.masterjava.service.mail.handlers;

import ru.javaops.masterjava.web.HostsConfig;
import ru.javaops.masterjava.web.handler.SoapLoggingHandlers;

public class SoapMailLoggingHandler extends SoapLoggingHandlers.ServerHandler {
    public SoapMailLoggingHandler() {
        super(HostsConfig.SERVER_LOGGING_LEVEL);
    }
}
