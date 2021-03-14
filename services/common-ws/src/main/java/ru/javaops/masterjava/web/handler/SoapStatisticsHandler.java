package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import ru.javaops.masterjava.web.Statistics;

import java.util.Date;

public class SoapStatisticsHandler extends SoapBaseHandler {
    @Override
    public boolean handleMessage(MessageHandlerContext context) {
        Statistics.count(context.getMessage().getPayloadLocalPart(), new Date().getTime(), Statistics.RESULT.SUCCESS);
        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {
        Statistics.count(context.getMessage().getPayloadLocalPart(), new Date().getTime(), Statistics.RESULT.FAIL);
        return true;
    }
}
