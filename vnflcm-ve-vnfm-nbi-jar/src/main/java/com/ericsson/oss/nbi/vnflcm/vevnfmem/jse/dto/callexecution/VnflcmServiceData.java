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
 * @author xrohdwi Data holder for data passed from service layer
 */
public class VnflcmServiceData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2746542818726497048L;

    private String vnfInstanceId;

    private String correlationId;

    private String vnflOppOccId;

    private String vnfdId;

    private String vnfPkgId;

    public String getVnfInstanceId() {
        return vnfInstanceId;
    }

    public void setVnfInstanceId(final String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }

    public String getVnflOppOccId() {
        return vnflOppOccId;
    }

    public void setVnflOppOccId(final String vnflOppOccId) {
        this.vnflOppOccId = vnflOppOccId;
    }

    public String getVnfdId() {
        return vnfdId;
    }

    public void setVnfId(final String vnfdId) {
        this.vnfdId = vnfdId;
    }

    public String getVnfPkgId() {
        return vnfPkgId;
    }

    public void setVnfPkgId(String vnfPkgId) {
        this.vnfPkgId = vnfPkgId;
    }

    @Override
    public String toString() {
        return "VnflcmServiceData [vnfInstanceId=" + vnfInstanceId + ", correlationId=" + correlationId
                + ", vnflOppOccId=" + vnflOppOccId + ", vnfdId=" + vnfdId + ", vnfPkgId=" + vnfPkgId + "]";
    }
}
