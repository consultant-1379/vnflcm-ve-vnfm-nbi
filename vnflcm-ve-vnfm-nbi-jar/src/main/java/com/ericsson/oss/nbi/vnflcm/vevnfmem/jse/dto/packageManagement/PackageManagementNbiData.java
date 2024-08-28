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

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution.NbiData;

public class PackageManagementNbiData extends NbiData implements Serializable{

    private static final long serialVersionUID = -2918049288655136992L;
    private String url;
    private String vnfdId;
    
    /**
     * @return the vnfdId
     */
    public String getVnfdId() {
        return vnfdId;
    }

    /**
     * @param vnfdId the vnfdId to set
     */
    public void setVnfdId(final String vnfdId) {
        this.vnfdId = vnfdId;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PackageManagementNbiData [url=" + url + "vnfdId=" + vnfdId + "]";
    }
    
    
}
