/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.vnflcm.common.models.RestResponse;
import com.ericsson.oss.services.vnflcm.common.util.RestClient;

public class QueryNfvoVdcListUtils {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(QueryNfvoVdcListUtils.class);

    public static RestResponse sendRestCall(final String endpointURL,
            final Map<String, String> headers, final String requestBody)
            throws NfvoCallException {
        RestResponse restResponse = new RestResponse();
        try {
            LOGGER.info("Start NfvoVdcUtils sendRestCall endpointURL {} "
                    , endpointURL);
            restResponse = RestClient.doGet(endpointURL, headers, requestBody);
            LOGGER.debug("Response of rest call is {}",
                    restResponse.getResponseBody());
        } catch (final Exception e) {
            LOGGER.info("Exception Occured in rest call" + e);
            throw new NfvoCallException(
                    CallExecutionErrorCode.REST_CALL_FAILURE.getValue(),
                    getExceptionMessage(e.getMessage()));
        }
        return restResponse;
    }

    /**
     * @param message
     * @return
     */
    private static String getExceptionMessage(String message) {
        LOGGER.info("Exception message {}", message);
        if (message != null) {
            if (message.contains("{") && message.contains("}")) {
                final int firstIndex = message.indexOf("{");
                final int lastIndex = message.lastIndexOf("}");
                message = message.substring(firstIndex, lastIndex + 1);
            } else {
                LOGGER.info("Response message is not in json format");
            }
        }
        return message;
    }

}
