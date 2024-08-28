/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.vnflcm.common.models.RestResponse;
import com.ericsson.oss.services.vnflcm.common.util.RestClient;

public class NotificationUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationUtils.class);

    public static RestResponse sendRestCall(final String endpointURL, final Map<String, String> headers, final String requestBody) throws NfvoCallException{
        RestResponse restResponse = new RestResponse();
        try {
            restResponse = RestClient.doPost(endpointURL, headers, requestBody);
            final Pattern pattern = Pattern.compile("\"password\":\"(.*?)\"");
            if (restResponse.getResponseBody()!= null && !restResponse.getResponseBody().trim().isEmpty()) {
                 final Matcher matcher = pattern.matcher(restResponse.getResponseBody());
                 if (matcher.find()) {
                     LOGGER.info("Response of rest call is {}",Utils.getMaskedString(restResponse.getResponseBody().toString()));
                 }
             }
        } catch (final Exception e) {
            LOGGER.info("Exception Occured in rest call"+ e);
            throw new NfvoCallException(CallExecutionErrorCode.REST_CALL_FAILURE.getValue(), getExceptionMessage(e.getMessage()));
        }
        return restResponse;
    }

    /**
     * @param message
     * @return
     */
    private static String getExceptionMessage(String message) {
        LOGGER.info("Exception message {}",message);
        if(message!=null){
            if(message.contains("{") && message.contains("}")){
                final int firstIndex=message.indexOf("{");
                final int lastIndex=message.lastIndexOf("}");
                message = message.substring(firstIndex, lastIndex+1);
            }else{
                LOGGER.info("Response message is not in json format");
            }
        }
        return message;
    }

    public static NotificationType getEnumByString(final String notificationType) throws IllegalArgumentException {

        NotificationType type = null;
        switch (notificationType) {
            case "CREATE":
                type = NotificationType.CREATE;
                break;

            case "DELETE":
                type = NotificationType.DELETE;
                break;

            case "START":
                type = NotificationType.START;
                break;

            case "PROCESSING":
                type = NotificationType.PROCESSING;
                break;

            case "ROLLED_BACK":
                type = NotificationType.ROLLED_BACK;
                break;
            case "COMPLETED":
                type = NotificationType.COMPLETED;
                break;

            case "FAILED":
                type = NotificationType.FAILED;
                break;

            case "FAILED_TEMP":
                type = NotificationType.FAILED_TEMP;
                break;

            case "ROLLING_BACK":
                type = NotificationType.ROLLING_BACK;
                break;

            default:
                throw new IllegalArgumentException("Invalid notification type: " + notificationType);

        }
        return type;
    }

    public static String getOperationState(final String notificationType) throws IllegalArgumentException {
        String operationState = "";
        switch (notificationType) {
            case "START":
                operationState = "STARTING";
                break;

            case "PROCESSING":
                operationState = "PROCESSING";
                break;
            case "ROLLED_BACK":
                operationState = "ROLLED_BACK";
                break;
            case "COMPLETED":
                operationState = "COMPLETED";
                break;

            case "FAILED":
                operationState = "FAILED";
                break;

            case "FAILED_TEMP":
                operationState = "FAILED_TEMP";
                break;

            case "ROLLING_BACK":
                operationState = "ROLLING_BACK";
                break;

            default:
                throw new IllegalArgumentException("Invalid operation state: " + notificationType);
        }
        return operationState;
    }

    public static String getNotificationStatus(final NotificationType notificationType) throws IllegalArgumentException {
        String notificationStatus = "";
        switch (notificationType) {
            case START:
                notificationStatus = "START";
                break;

            case PROCESSING:
                notificationStatus = "START";
                break;

            case ROLLED_BACK:
                notificationStatus = "RESULT";
                break;
            case COMPLETED:
                notificationStatus = "RESULT";
                break;

            case FAILED:
                notificationStatus = "RESULT";
                break;

            case FAILED_TEMP:
                notificationStatus = "RESULT";
                break;

            case ROLLING_BACK:
                notificationStatus = "START";
                break;

            default:
                throw new IllegalArgumentException("Invalid notification state: " + notificationType);
        }
        return notificationStatus;
    }

    public static String getModifiedOperationType(final String operationType) {
        if (operationType.trim().equalsIgnoreCase("INSTANTIATION")) {
            return "INSTANTIATE";
        } else if (operationType.trim().equalsIgnoreCase("TERMINATION")) {
            return "TERMINATE";
        } else if (operationType.trim().equalsIgnoreCase("SCALE_OUT") || operationType.trim().equalsIgnoreCase("SCALE_IN")) {
            return "SCALE";
        } else {
            return operationType;
        }
    }

}
