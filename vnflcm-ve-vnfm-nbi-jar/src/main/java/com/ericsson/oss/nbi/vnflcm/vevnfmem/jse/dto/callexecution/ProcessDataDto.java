package com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.callexecution;

import java.util.List;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.commons.CallExecutionType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.jse.dto.packageManagement.PackageManagementListenerData;

//DTO for passing data from listner to CallExecutionDataRetriever
/*
 * @author xrohdwi
 *
 */
public class ProcessDataDto implements Correlation {
    private String correlationid;
    private CallExecutionType callExecutionType;
    private PackageManagementListenerData packageManagementListenerData;
    private NfvoConfig nfvoConfigData;
    private List<NfvoConfig> emConfigData; // For SOL002 EM configuration also present.

    @Override
    public String getCorrelationid() {
        return correlationid;
    }

    @Override
    public void setCorrelationId(final String correlationId) {
        this.correlationid = correlationId;
    }

    @Override
    public CallExecutionType getCallExecutionType() {
        return callExecutionType;
    }

    @Override
    public void setCallExecutionType(final CallExecutionType callExecutionType) {
        this.callExecutionType = callExecutionType;

    }

    public PackageManagementListenerData getPackageManagementListenerData() {
        return packageManagementListenerData;
    }

    public void setPackageManagementListenerData(
            final PackageManagementListenerData packageManagementListenerData) {
        this.packageManagementListenerData = packageManagementListenerData;
    }

    public NfvoConfig getNfvoConfigData() {
        return nfvoConfigData;
    }

    public void setNfvoConfigData(NfvoConfig nfvoConfigData) {
        this.nfvoConfigData = nfvoConfigData;
    }

    /**
     * @return the emConfigData
     */
    public final List<NfvoConfig> getEmConfigData() {
        return emConfigData;
    }

    /**
     * @param emConfigData the emConfigData to set
     */
    public final void setEmConfigData(final List<NfvoConfig> emConfigData) {
        this.emConfigData = emConfigData;
    }

    @Override
    public String toString() {
        return "ProcessDataDto [correlationid=" + correlationid + ", callExecutionType=" + callExecutionType
                + ", packageManagementListenerData=" + packageManagementListenerData + ", nfvoConfigData="
                + nfvoConfigData + ", emConfigData=" + emConfigData + "]";
    }
}
