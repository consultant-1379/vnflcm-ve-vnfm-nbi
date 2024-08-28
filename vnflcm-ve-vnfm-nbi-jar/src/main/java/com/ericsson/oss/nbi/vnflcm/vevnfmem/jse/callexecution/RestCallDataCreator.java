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
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.RestCallDataDto;

/**
 * @author xrohdwi
 *
 */
public interface RestCallDataCreator {

    /**
     * Creates rest call data
     *
     * @param notificationData
     * @return
     * @throws NfvoCallException
     */
    public RestCallDataDto createRestCallData(CallExecutionData notificationData) throws NfvoCallException;

}
