package ru.javaops.masterjava.web;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Statistics {
    public enum RESULT {
        SUCCESS, FAIL
    }

    private static List<String> failResults = new ArrayList<>();

    public static void count(String payload, long startTime, RESULT result) {
        long now = System.currentTimeMillis();
        int ms = (int) (now - startTime);
        log.info(payload + " " + result.name() + " execution time(ms): " + ms);

        if (result == RESULT.FAIL) {
            countFailures(payload);
        }
    }

    private static void countFailures(String payload) {
        failResults.add(payload);
        int failsCount = failResults.size();
        if (failsCount >= 10 && failsCount % 10 == 0) {
            log.warn("There is a lot of failures. Last 10: \n" + getLast10Failures());
        }
    }

    private static String getLast10Failures() {
        StringBuilder sb = new StringBuilder();
        int failuresCount = failResults.size();
        if (failuresCount < 10) {
            return sb.toString();
        }
        for (int i = failuresCount; i > (failuresCount - 10); i--) {
            sb.append(failResults.get(i - 1)).append("\n");
        }
        return sb.toString();
    }
}
