package com.caching.cachingtest.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import com.caching.cachingtest.AppConstants;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomJsonLayout extends JsonLayout {

    /**
     * @param event 
     * @return
     */
    @Override
    protected Map toJsonMap(ILoggingEvent event) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        add(AppConstants.LOG_CLASS, this.includeLoggerName, event.getCallerData()[0].getClassName(), map);
        add(AppConstants.LOG_FUNCTION, this.includeLoggerName, event.getCallerData()[0].getMethodName(), map);
        add(AppConstants.LOG_LEVEL, this.includeLevel, String.valueOf(event.getLevel()), map);
        add(AppConstants.LOG_MESSAGE, this.includeFormattedMessage, event.getFormattedMessage(), map);
        add(AppConstants.LOG_SERVICE_NAME, this.includeFormattedMessage, AppConstants.LOG_SERVICE_NAME_VALUE, map);
        add(AppConstants.LOG_SERVICE_VERSION, this.includeFormattedMessage, AppConstants.LOG_SERVICE_VERSION_VALUE, map);
        addTimestamp(AppConstants.LOG_TIMESTAMP, this.includeTimestamp, event.getTimeStamp(), map);
        add(AppConstants.LOG_THREAD, this.includeThreadName, event.getThreadName(), map);
        addThrowableInfo(AppConstants.LOG_EXCEPTION, this.includeException, event, map);

        addCustomDataToJsonMap(map, event);
        return map;
    }
}
