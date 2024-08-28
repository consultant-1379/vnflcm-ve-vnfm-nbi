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
import java.util.List;

public class CallExecutionData implements Serializable {

    private static final long serialVersionUID = -7622976193706403108L;

    private EcmRequestData ecmData;

    private NbiData nbiData;

    private VnflcmServiceData vnflcmServiceData;

    private List<NfvoConfig> emConfigList;

    public EcmRequestData getEcmData() {
        return ecmData;
    }

    public void setEcmData(final EcmRequestData ecmData) {
        this.ecmData = ecmData;
    }

    public NbiData getNbiNotificationData() {
        return nbiData;
    }

    public void setNbiNotificationData(final NbiData nbiNotificationData) {
        this.nbiData = nbiNotificationData;
    }

    public VnflcmServiceData getVnflcmServiceData() {
        return vnflcmServiceData;
    }

    public void setVnflcmServiceData(final VnflcmServiceData vnflcmServiceData) {
        this.vnflcmServiceData = vnflcmServiceData;
    }

    public NbiData getNbiData() {
        return nbiData;
    }

    public void setNbiData(final NbiData nbiData) {
        this.nbiData = nbiData;
    }

    public List<NfvoConfig> getEmConfigList() {
        return emConfigList;
    }

    public void setEmConfigList(final List<NfvoConfig> emConfigList) {
        this.emConfigList = emConfigList;
    }

    @Override
    public String toString() {
        return "CallExecutionData [ecmData=" + ecmData + ", nbiData=" + nbiData
                + ", vnflcmServiceData=" + vnflcmServiceData + "]";
    }

}
