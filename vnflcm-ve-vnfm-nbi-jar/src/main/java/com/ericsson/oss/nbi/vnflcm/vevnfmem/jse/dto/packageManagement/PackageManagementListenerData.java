/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement;

import java.io.Serializable;

/**
 * @author xnareku
 * 
 */
public class PackageManagementListenerData implements Serializable {

    private static final long serialVersionUID = -3133901206829395178L;
    private String vnfdId;
    private String correlationId;
    private String requestType;
    private String vnfPkgId;

    public String getVnfdId() {
        return vnfdId;
    }

    public void setVnfdId(final String vnfdId) {
        this.vnfdId = vnfdId;
    }
    /**
     * @return the correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }
    /**
     * @return the requestType
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(final String requestType) {
        this.requestType = requestType;
    }

    public String getVnfPkgId() {
        return vnfPkgId;
    }

    public void setVnfPkgId(final String vnfPkgId) {
        this.vnfPkgId = vnfPkgId;
    }

    @Override
    public String toString() {
        return "PackageManagementListenerData [vnfdId=" + vnfdId + ", correlationId=" + correlationId + ", requestType="
                + requestType + ", vnfPkgId=" + vnfPkgId + "]";
    }
}
