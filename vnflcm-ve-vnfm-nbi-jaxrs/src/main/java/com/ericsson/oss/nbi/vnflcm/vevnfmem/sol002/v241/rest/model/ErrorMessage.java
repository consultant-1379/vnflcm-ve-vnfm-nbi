/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2020
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ErrorMessage {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String TIME_ZONE_ID = "Etc/GMT";
    protected String userMessage;
    protected int httpStatusCode;
    protected String developerMessage;
    protected String internalErrorCode;
    protected String time;

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(final String value) {
        this.userMessage = value;
    }

    public ErrorMessage withUserMessage(final String value) {
        this.userMessage = value;
        return this;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(final int value) {
        this.httpStatusCode = value;
    }

    public ErrorMessage withHttpStatusCode(final int value) {
        this.httpStatusCode = value;
        return this;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(final String value) {
        this.developerMessage = value;
    }

    public ErrorMessage withDeveloperMessage(final String value) {
        this.developerMessage = value;
        return this;
    }

    public String getInternalErrorCode() {
        return internalErrorCode;
    }

    public void setInternalErrorCode(final String value) {
        this.internalErrorCode = value;
    }

    public ErrorMessage withInternalErrorCode(final String value) {
        this.internalErrorCode = value;
        return this;
    }

    public String getTime() {
        return time;
    }

    public void setTime(final String value) {
        this.time = value;
    }

    public ErrorMessage withTime(final String value) {
        this.time = value;
        return this;
    }

    public void setTime(final Date time) {
        final SimpleDateFormat simDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        simDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_ID));
        simDateFormat.setLenient(false);
        setTime(simDateFormat.format(time));
    }
}
