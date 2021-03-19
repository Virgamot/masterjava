package ru.javaops.masterjava.web;

import com.typesafe.config.Config;
import org.slf4j.event.Level;
import ru.javaops.masterjava.config.Configs;


public class HostsConfig {

    final public static Config MAIL_CONFIG;
    final public static Config LOGGING_CONFIG;
    final public static Config CREDENTIALS_CONFIG;
    final public static String USER;
    final public static String PASSWORD;
    final public static Level CLIENT_LOGGING_LEVEL;
    final public static Level SERVER_LOGGING_LEVEL;

    static {
        Config conf = Configs.getConfig("hosts.conf", "hosts");
        MAIL_CONFIG = conf.getConfig("mail");
        LOGGING_CONFIG = conf.getConfig("logging");
        CREDENTIALS_CONFIG = conf.getConfig("credentials");
        USER = CREDENTIALS_CONFIG.getString("user");
        PASSWORD = CREDENTIALS_CONFIG.getString("password");
        CLIENT_LOGGING_LEVEL = Level.valueOf(LOGGING_CONFIG.getString("debug.client"));
        SERVER_LOGGING_LEVEL = Level.valueOf(LOGGING_CONFIG.getString("debug.server"));
    }
}
