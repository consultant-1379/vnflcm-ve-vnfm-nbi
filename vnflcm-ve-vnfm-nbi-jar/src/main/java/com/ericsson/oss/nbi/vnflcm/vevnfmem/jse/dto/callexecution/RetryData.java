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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution;

import java.io.Serializable;

/**
 * @author xrohdwi
 * 
 */
public class RetryData implements Serializable {

    private static final long serialVersionUID = -3649607617600152611L;

    @Override
    public String toString() {
        return "RetryData [Action=" + Action + ", retryTimes=" + retryTimes + "]";
    }

    private String Action;
    private int retryTimes;

    public String getAction() {
        return Action;
    }

    public void setAction(final String action) {
        Action = action;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(final int retryTimes) {
        this.retryTimes = retryTimes;
    }

}
