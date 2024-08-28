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

package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.callexecution;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.NfvoCallException;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.CallExecutionData;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.ProcessDataDto;

/**
 * Fetch data required for notification
 *
 * @author xrohdwi
 *
 */
public interface CallExecutionDataRetriever {

    /**
     * Fetch data required for VeVnfm compliant request
     *
     * @param processNotificationData
     *
     * @param vnfInstanceId
     * @param type
     * @return
     */
    public CallExecutionData getCallExecutionData(ProcessDataDto processNotificationData) throws NfvoCallException;

}
