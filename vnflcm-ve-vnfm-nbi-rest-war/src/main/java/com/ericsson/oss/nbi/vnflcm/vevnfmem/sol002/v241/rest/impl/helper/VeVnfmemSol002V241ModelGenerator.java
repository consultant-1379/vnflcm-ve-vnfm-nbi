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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.impl.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.CpProtocolInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtCpInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtLinkPortInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtManagedVirtualLinkInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiatedVnfInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiationState;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.KeyValuePairs;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ResourceHandle;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ScaleInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VirtualStorageResourceInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfInstance_links;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfLinkPortInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfOperationalStateType;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfVirtualLinkResourceInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfcResourceInfo;
import com.ericsson.oss.services.vnflcm.api_base.dto.ExtLinkPort;
import com.ericsson.oss.services.vnflcm.api_base.dto.ExtVirtualLinkInfo;
import com.ericsson.oss.services.vnflcm.api_base.dto.VnfResponse;
import com.ericsson.oss.services.vnflcm.common.dataTypes.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VeVnfmemSol002V241ModelGenerator {

    @SuppressWarnings("unchecked")
    public static void translateVNFInstanceToModel(final VnfResponse vnfInstance, final VnfInstance vnfInfo) {
        final ModelMapper modelMapper = new ModelMapper();
        if (vnfInstance != null && vnfInfo != null) {
            setVnfBasicProperties(vnfInstance, vnfInfo);

            setVnfConfigurableProperties(vnfInstance, vnfInfo);

            setMetadata(vnfInstance, vnfInfo);

            setExtensions(vnfInstance, vnfInfo);

            if (vnfInstance.getInstantiatedVnfInfo() != null) {
                translateInstantiationState(vnfInstance, vnfInfo);
            }
            if (vnfInstance.get_links() != null) {
                final VnfInstance_links vnfInstanceLink = modelMapper.map(vnfInstance.get_links(), VnfInstance_links.class);
                vnfInfo.set_links(vnfInstanceLink);
            }
        }
    }

    private static void setVnfBasicProperties(final VnfResponse vnfInstance, final VnfInstance vnfInfo) {
        vnfInfo.setId(vnfInstance.getVnfId());
        vnfInfo.setVnfInstanceName(vnfInstance.getVnfName());
        vnfInfo.setVnfInstanceDescription(vnfInstance.getVnfDescription());
        vnfInfo.setVnfdId(vnfInstance.getVnfdId());
        vnfInfo.setVnfdVersion(vnfInstance.getVnfdVersion());
        vnfInfo.setVnfProvider(vnfInstance.getVnfProvider());
        vnfInfo.setVnfProductName(vnfInstance.getVnfProductName());
        vnfInfo.setVnfSoftwareVersion(vnfInstance.getVnfSoftwareVersion());
        vnfInfo.setVnfPkgId(vnfInstance.getOnboardedPackageInfoId());
    }

    @SuppressWarnings("unchecked")
    private static void setVnfConfigurableProperties(final VnfResponse vnfInstance, final VnfInstance vnfInfo) {
        if (vnfInstance.getVnfConfigurableProperties() != null) {
            if (vnfInstance.getVnfConfigurableProperties().getValue() != null) {
                final String configurablePropertiesStringJson = vnfInstance.getVnfConfigurableProperties().getValue();
                Map<String, Object> configurableProperties = null;
                try {
                    configurableProperties = new ObjectMapper().readValue(configurablePropertiesStringJson, HashMap.class);
                } catch (final Exception ex) {
                    //Ignore
                }
                vnfInfo.setVnfConfigurableProperties(configurableProperties);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void setMetadata(final VnfResponse vnfInstance, final VnfInstance vnfInfo) {
        if (vnfInstance.getMetadata() != null) {
            if (vnfInstance.getMetadata().getValue() != null) {
                final String metadataStringJson = vnfInstance.getMetadata().getValue();
                Map<String, Object> metaData = null;
                try {
                    metaData = new ObjectMapper().readValue(metadataStringJson, HashMap.class);
                } catch (final Exception ex) {
                    //Ignore
                }
                vnfInfo.setMetadata(metaData);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void setExtensions(final VnfResponse vnfInstance, final VnfInstance vnfInfo) {
        if (vnfInstance.getExtensions() != null) {
            if (vnfInstance.getExtensions().getValue() != null) {
                final String extensionStringJson = vnfInstance.getExtensions().getValue();
                Map<String, Object> extension = null;
                try {
                    if(extensionStringJson.contains(Constants.SENSITIVE_BLOCK)) {
                        extension = new ObjectMapper().readValue(extensionStringJson, HashMap.class);
                        extension.remove(Constants.SENSITIVE_BLOCK);
                    }
                    else {
                        extension = new ObjectMapper().readValue(extensionStringJson, HashMap.class);
                    }
                } catch (final Exception ex) {
                    //Ignore
                }
                vnfInfo.setExtensions(extension);
            }
        }
    }

    private static void translateInstantiationState(final VnfResponse vnfInstance, final VnfInstance vnfInfo) {
        final ModelMapper modelMapper = new ModelMapper();
        switch (vnfInstance.getInstantiationState()) {
            case INSTANTIATED:
                vnfInfo.setInstantiationState(InstantiationState.INSTANTIATED);
                final InstantiatedVnfInfo instantiatedVnfInformation = new InstantiatedVnfInfo();
                instantiatedVnfInformation.setFlavourId(vnfInstance.getInstantiatedVnfInfo().getFlavourId());

                final ExtCpInfo[] exCpInfos = modelMapper.map(vnfInstance.getInstantiatedVnfInfo().getExtCpInfo(), ExtCpInfo[].class);
                if (exCpInfos != null && exCpInfos.length > 0) {
                    instantiatedVnfInformation.setExtCpInfo(Arrays.asList(exCpInfos));
                }
                final List<ExtCpInfo> nbiExtCpInfos = new ArrayList<>();
                final List<ExtVirtualLinkInfo> extVirtualLinkInfos = vnfInstance.getInstantiatedVnfInfo().getExtVirtualLinkInfo();
                final List<com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtVirtualLinkInfo> extVlInfos = new ArrayList<>();
                for (final ExtVirtualLinkInfo extVirtualLinkInfo : extVirtualLinkInfos) {
                    final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtVirtualLinkInfo nbiExtVirtualLinkInfo = new com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ExtVirtualLinkInfo();
                    nbiExtVirtualLinkInfo.setId(extVirtualLinkInfo.getId());
                    final List<ExtLinkPortInfo> extLinkPortInfos = extractExtLinkPortInfos(modelMapper, nbiExtCpInfos, extVirtualLinkInfo);
                    nbiExtVirtualLinkInfo.setExtLinkPorts(extLinkPortInfos);
                    final ResourceHandle resourceHandle = modelMapper.map(extVirtualLinkInfo.getResourceHandle(), ResourceHandle.class);
                    nbiExtVirtualLinkInfo.setResourceHandle(resourceHandle);
                    extVlInfos.add(nbiExtVirtualLinkInfo);
                }
                instantiatedVnfInformation.setExtVirtualLinkInfo(extVlInfos);
                if (nbiExtCpInfos != null && !nbiExtCpInfos.isEmpty()) {
                    instantiatedVnfInformation.setExtCpInfo(nbiExtCpInfos);
                }
                if (vnfInstance.getInstantiatedVnfInfo().getExtManagedVirtualLinkInfo() != null
                        && !vnfInstance.getInstantiatedVnfInfo().getExtManagedVirtualLinkInfo().isEmpty()) {
                    final List<ExtManagedVirtualLinkInfo> extManagedVirtualLinkInfos = new ArrayList<>();
                    for (final com.ericsson.oss.services.vnflcm.api_base.dto.ExtManagedVirtualLinkInfo extManagedVL : vnfInstance
                            .getInstantiatedVnfInfo().getExtManagedVirtualLinkInfo()) {
                        final ExtManagedVirtualLinkInfo extManagedVirtualLinkInfo = new ExtManagedVirtualLinkInfo();
                        extManagedVirtualLinkInfo.setId(extManagedVL.getId());
                        extManagedVirtualLinkInfo.setVnfVirtualLinkDescId(extManagedVL.getVnfVirtualLinkDescId());
                        if (extManagedVL.getNetworkResource() != null) {
                            final ResourceHandle networkResource = modelMapper.map(extManagedVL.getNetworkResource(), ResourceHandle.class);
                            extManagedVirtualLinkInfo.setNetworkResource(networkResource);
                        }
                        if (extManagedVL.getVnfLinkPorts() != null) {
                            final List<VnfLinkPortInfo> nbiVnfLinkPortInfos = extractVnfLinkPortInfos(modelMapper, extManagedVL);

                            extManagedVirtualLinkInfo.setVnfLinkPorts(nbiVnfLinkPortInfos);
                        }
                        extManagedVirtualLinkInfos.add(extManagedVirtualLinkInfo);
                    }
                    instantiatedVnfInformation.setExtManagedVirtualLinkInfo(extManagedVirtualLinkInfos);
                }
                setScaleStatus(vnfInstance, modelMapper, instantiatedVnfInformation);

                setVirtualStorageResourceInfo(vnfInstance, modelMapper, instantiatedVnfInformation);

                if (vnfInstance.getInstantiatedVnfInfo().getVnfcResourceInfo() != null
                        && !vnfInstance.getInstantiatedVnfInfo().getVnfcResourceInfo().isEmpty()) {
                    final VnfcResourceInfo[] vnfcResourceInfos = modelMapper.map(vnfInstance.getInstantiatedVnfInfo().getVnfcResourceInfo(),
                            VnfcResourceInfo[].class);
                    if (vnfcResourceInfos != null && vnfcResourceInfos.length > 0) {
                        for (final VnfcResourceInfo vnfcResourceInfo : vnfcResourceInfos) {
                            if (vnfcResourceInfo.getVnfcCpInfo() != null) {
                                for (final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VnfcCpInfo vnfcCpInfo : vnfcResourceInfo
                                        .getVnfcCpInfo()) {
                                    if (vnfcCpInfo.getCpProtocolInfo() != null) {
                                        for (final CpProtocolInfo cpProtocolInfo : vnfcCpInfo.getCpProtocolInfo()) {
                                            for (final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.IpAddresses ipAddress : cpProtocolInfo
                                                    .getIpOverEthernet().getIpAddresses()) {
                                                ipAddress.setFixedAddresses(null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        instantiatedVnfInformation.setVnfcResourceInfo(Arrays.asList(vnfcResourceInfos));
                    }
                }
                if (vnfInstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo() != null
                        && !vnfInstance.getInstantiatedVnfInfo().getVnfVirtualLinkResourceInfo().isEmpty()) {
                    final List<VnfVirtualLinkResourceInfo> vnfVLResources = extractVnfVLResourceInfo(vnfInstance, modelMapper);

                    instantiatedVnfInformation.setVnfVirtualLinkResourceInfo(vnfVLResources);
                }

                setVnfState(vnfInstance, instantiatedVnfInformation);
                vnfInfo.setInstantiatedVnfInfo(instantiatedVnfInformation);
                break;
            case NOT_INTANTIATED:
                vnfInfo.setInstantiationState(InstantiationState.NOT_INSTANTIATED);
                break;
        }
    }

    private static void setScaleStatus(final VnfResponse vnfInstance, final ModelMapper modelMapper,
                                       final InstantiatedVnfInfo instantiatedVnfInformation) {
        if (vnfInstance.getInstantiatedVnfInfo().getScaleStatus() != null && !vnfInstance.getInstantiatedVnfInfo().getScaleStatus().isEmpty()) {
            final ScaleInfo[] scaleInfos = modelMapper.map(vnfInstance.getInstantiatedVnfInfo().getScaleStatus(), ScaleInfo[].class);
            if (scaleInfos != null && scaleInfos.length > 0) {
                instantiatedVnfInformation.setScaleStatus(Arrays.asList(scaleInfos));
            }
        }
    }

    private static List<VnfLinkPortInfo> extractVnfLinkPortInfos(final ModelMapper modelMapper,
                                                                 final com.ericsson.oss.services.vnflcm.api_base.dto.ExtManagedVirtualLinkInfo extManagedVL) {
        final List<VnfLinkPortInfo> nbiVnfLinkPortInfos = new ArrayList<VnfLinkPortInfo>();
        for (final com.ericsson.oss.services.vnflcm.api_base.dto.VnfLinkPort vnfLinkPort : extManagedVL.getVnfLinkPorts()) {
            final VnfLinkPortInfo newVnfLinkPort = new VnfLinkPortInfo();
            newVnfLinkPort.setId(vnfLinkPort.getId());
            newVnfLinkPort.setCpInstanceId(vnfLinkPort.getCpInstanceId());
            final ResourceHandle resourceHandle = modelMapper.map(vnfLinkPort.getResourceHandle(), ResourceHandle.class);
            newVnfLinkPort.setResourceHandle(resourceHandle);
            nbiVnfLinkPortInfos.add(newVnfLinkPort);
        }
        return nbiVnfLinkPortInfos;
    }

    private static List<ExtLinkPortInfo> extractExtLinkPortInfos(final ModelMapper modelMapper, final List<ExtCpInfo> nbiExtCpInfos,
                                                                 final ExtVirtualLinkInfo extVirtualLinkInfo) {
        final List<ExtLinkPortInfo> extLinkPortInfos = new ArrayList<>();
        final List<ExtLinkPort> linkPorts = extVirtualLinkInfo.getLinkPorts();
        for (final ExtLinkPort extLinkPort : linkPorts) {
            final ExtLinkPortInfo extLinkPortInfo = new ExtLinkPortInfo();
            extLinkPortInfo.setCpInstanceId(extLinkPort.getCpInstanceId());
            extLinkPortInfo.setId(extLinkPort.getId());
            final ResourceHandle resourceHandle = modelMapper.map(extLinkPort.getResourceHandle(), ResourceHandle.class);
            extLinkPortInfo.setResourceHandle(resourceHandle);
            extLinkPortInfos.add(extLinkPortInfo);
            if (extLinkPort.getExtCpInfo() != null) {
                final ExtCpInfo extCpInfo = new ExtCpInfo();
                extCpInfo.setId(extLinkPort.getId());
                extCpInfo.setCpdId(extLinkPort.getExtCpInfo().getCpdId());
                extCpInfo.setExtLinkPortId(extLinkPort.getId());
                final CpProtocolInfo[] cpProtocolInfos = modelMapper.map(extLinkPort.getExtCpInfo().getCpProtocolInfo(), CpProtocolInfo[].class);
                if (cpProtocolInfos != null && cpProtocolInfos.length > 0) {
                    //Query Response as per SOL002 standards should only contain IpAddresses.addresses, hence setting fixedaddresses to null
                    for (final CpProtocolInfo cpProtocolInfo : cpProtocolInfos) {
                        for (final com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.IpAddresses ipAddress : cpProtocolInfo
                                .getIpOverEthernet().getIpAddresses()) {
                            ipAddress.setFixedAddresses(null);
                        }
                    }
                    // End
                    extCpInfo.setCpProtocolInfo(Arrays.asList(cpProtocolInfos));
                }
                nbiExtCpInfos.add(extCpInfo);
            }
        }
        return extLinkPortInfos;
    }

    private static void setVirtualStorageResourceInfo(final VnfResponse vnfInstance, final ModelMapper modelMapper,
                                                      final InstantiatedVnfInfo instantiatedVnfInformation) {
        if (vnfInstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo() != null
                && !vnfInstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo().isEmpty()) {
            final VirtualStorageResourceInfo[] virtualStorageResourceInfo = modelMapper
                    .map(vnfInstance.getInstantiatedVnfInfo().getVirtualStorageResourceInfo(), VirtualStorageResourceInfo[].class);
            if (virtualStorageResourceInfo != null && virtualStorageResourceInfo.length > 0) {
                instantiatedVnfInformation.setVirtualStorageResourceInfo(Arrays.asList(virtualStorageResourceInfo));
            }
        }
    }

    private static List<VnfVirtualLinkResourceInfo> extractVnfVLResourceInfo(final VnfResponse vnfInstance, final ModelMapper modelMapper) {
        final List<VnfVirtualLinkResourceInfo> vnfVLResources = new ArrayList<VnfVirtualLinkResourceInfo>();
        for (final com.ericsson.oss.services.vnflcm.api_base.dto.VnfVirtualLinkResourceInfo vnfVLResource : vnfInstance.getInstantiatedVnfInfo()
                .getVnfVirtualLinkResourceInfo()) {
            final VnfVirtualLinkResourceInfo vlResource = new VnfVirtualLinkResourceInfo();
            vlResource.setId(vnfVLResource.getId());
            vlResource.setVnfVirtualLinkDescId(vnfVLResource.getVirtualLinkDescId());
            if (vnfVLResource.getMetadata() != null) {
                final KeyValuePairs metaData = modelMapper.map(vnfVLResource.getMetadata(), KeyValuePairs.class);
                vlResource.setMetadata(metaData);
            }
            if (vnfVLResource.getNetworkResource() != null) {
                final ResourceHandle netWorkResource = modelMapper.map(vnfVLResource.getNetworkResource(), ResourceHandle.class);
                vlResource.setNetworkResource(netWorkResource);
            }

            vlResource.setReservationId(vnfVLResource.getReservationId());
            if (vnfVLResource.getVnfLinkPorts() != null) {
                final List<VnfLinkPortInfo> nbiVnfLinkPortInfos = new ArrayList<VnfLinkPortInfo>();
                for (final com.ericsson.oss.services.vnflcm.api_base.dto.VnfLinkPort vnfLinkPort : vnfVLResource.getVnfLinkPorts()) {
                    final VnfLinkPortInfo newVnfLinkPort = new VnfLinkPortInfo();
                    newVnfLinkPort.setId(vnfLinkPort.getId());
                    newVnfLinkPort.setCpInstanceId(vnfLinkPort.getCpInstanceId());
                    final ResourceHandle resourceHandle = modelMapper.map(vnfLinkPort.getResourceHandle(), ResourceHandle.class);
                    newVnfLinkPort.setResourceHandle(resourceHandle);
                    nbiVnfLinkPortInfos.add(newVnfLinkPort);
                }
                vlResource.setVnfLinkPorts(nbiVnfLinkPortInfos);
            }
            vnfVLResources.add(vlResource);
        }
        return vnfVLResources;
    }

    private static void setVnfState(final VnfResponse vnfInstance, final InstantiatedVnfInfo instantiatedVnfInformation) {
        if (vnfInstance.getInstantiatedVnfInfo().getVnfState() == com.ericsson.oss.services.vnflcm.common.dataTypes.VnfOperationalStateType.STOPPED) {
            instantiatedVnfInformation.setVnfState(VnfOperationalStateType.STOPPED);
        } else {
            instantiatedVnfInformation.setVnfState(VnfOperationalStateType.STARTED);
        }
    }

}
