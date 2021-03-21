package ru.javaops.masterjava.service.mail.handlers;

import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.HostsConfig;
import ru.javaops.masterjava.web.handler.SoapServerSecurityHandler;

public class SoapMailSecurityHandler extends SoapServerSecurityHandler {
    public SoapMailSecurityHandler() {
        USERS_AUTH_HEADER= AuthUtil.encodeBasicAuthHeader(HostsConfig.USER,HostsConfig.PASSWORD);
    }
}
