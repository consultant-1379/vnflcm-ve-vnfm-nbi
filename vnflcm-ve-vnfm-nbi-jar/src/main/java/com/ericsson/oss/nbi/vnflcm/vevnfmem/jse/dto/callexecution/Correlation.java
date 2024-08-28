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

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionType;

public interface Correlation {

    /**
     * Fetch Correlation id
     * 
     * @return
     */
    public String getCorrelationid();

    /**
     * Set Correlation id
     */
    public void setCorrelationId(String correlationId);

    /**
     * @return
     */
    public CallExecutionType getCallExecutionType();

    /**
     * Set call execution type. Values are NOTIFICATION or GRANT
     */
    public void setCallExecutionType(CallExecutionType callExecutionType);

}
