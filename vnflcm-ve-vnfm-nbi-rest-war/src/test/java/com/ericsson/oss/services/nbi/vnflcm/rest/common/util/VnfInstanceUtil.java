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
package com.ericsson.oss.services.nbi.vnflcm.rest.common.util;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.services.vnflcm.api_base.dto.*;
import com.ericsson.oss.services.vnflcm.common.dataTypes.CloudTypes;

public class VnfInstanceUtil {
    public VnfResponse createVNFInstance(final String vnfIdentifier) {
        final VnfResponse vnfResponse = new VnfResponse();
        vnfResponse.setVnfId(vnfIdentifier);
        vnfResponse.setVnfDescription("Test Description");
        vnfResponse.set_links(createLinks());
        vnfResponse.setHotPackageName("pack123");
        vnfResponse.setInstantiatedVnfInfo(createInstantiatedVnfInfo());
        vnfResponse.setOnboardedPackageInfoId("onboardedpack123");
        vnfResponse.setVimConnectionInfo(createVimConnectionInfo());
        vnfResponse.setVnfdId("vnfd123");
        vnfResponse.setVnfdVersion("V1");
        vnfResponse.setVnfName("vnf123");
        vnfResponse.setVnfProductName("prod123");
        vnfResponse.setVnfProvider("provider123");
        vnfResponse.setVnfSoftwareVersion("V1");
        return vnfResponse;
    }

    public VnfResponse createVNFInstance2(final String vnfIdentifier) {
        final VnfResponse vnfResponse = new VnfResponse();
        vnfResponse.setVnfId(vnfIdentifier);
        vnfResponse.setVnfDescription("Test Description2");
        vnfResponse.set_links(createLinks2());
        vnfResponse.setHotPackageName("pack456");
        vnfResponse.setInstantiatedVnfInfo(createInstantiatedVnfInfo2());
        vnfResponse.setOnboardedPackageInfoId("onboardedpack456");
        vnfResponse.setVimConnectionInfo(createVimConnectionInfo2());
        vnfResponse.setVnfdId("vnfd456");
        vnfResponse.setVnfdVersion("V2");
        vnfResponse.setVnfName("vnf456");
        vnfResponse.setVnfProductName("prod46");
        vnfResponse.setVnfProvider("provider456");
        vnfResponse.setVnfSoftwareVersion("V2");
        return vnfResponse;
    }

    private List<VimConnectionInfo> createVimConnectionInfo2() {
        final List<VimConnectionInfo> vimConnectionInfos = new ArrayList<VimConnectionInfo>();
        final VimConnectionInfo vimConnectionInfo = new VimConnectionInfo();
        vimConnectionInfo.setId("vimconnect456");
        vimConnectionInfo.setVimId("vim456");
        vimConnectionInfo.setVimType(CloudTypes.OPENSTACK);
        vimConnectionInfo.setAccessInfo(createAccessInfo());
        vimConnectionInfo.setInterfaceInfo(createInterfaceInfo());
        vimConnectionInfos.add(vimConnectionInfo);
        return vimConnectionInfos;
    }

    private InstantiatedVnfInfo createInstantiatedVnfInfo2() {
        final InstantiatedVnfInfo instantiatedVnfInfo = new InstantiatedVnfInfo();
        instantiatedVnfInfo.setFlavourId("flavor456");
        instantiatedVnfInfo.setVappId("vapp456");
        instantiatedVnfInfo.setVappName("vappname456");
        instantiatedVnfInfo.setExtVirtualLinkInfo(createExtVirtualLinkInfo());
        instantiatedVnfInfo.setVnfVirtualLinkResourceInfo(createVirtualLinkResourceInfo());
        instantiatedVnfInfo.setVirtualStorageResourceInfo(createVirtualStorageResourceInfo());
        instantiatedVnfInfo.setVnfcResourceInfo(createVnfcResourceInfo());
        return instantiatedVnfInfo;
    }

    private VnfInstance_links createLinks2() {
        final VnfInstance_links vnfLnks = new VnfInstance_links();
        final Link selfLink = new Link();
        selfLink.setHref("/vevnfmem/vnflcm/v1/vnf_instances/{vnfInstanceId}");
        final Link instantiateLink = new Link();
        instantiateLink.setHref("/vevnfmem/vnflcm/v1/vnf_instances/{vnfInstanceId}/instantiate");
        final Link terminateLink = new Link();
        terminateLink.setHref("/vevnfmem/vnflcm/v1/vnf_instances/{vnfInstanceId}");
        vnfLnks.setSelf(selfLink);
        vnfLnks.setInstantiate(instantiateLink);
        vnfLnks.setTerminate(terminateLink);
        return vnfLnks;
    }

    private List<VimConnectionInfo> createVimConnectionInfo() {
        final List<VimConnectionInfo> vimConnectionInfos = new ArrayList<VimConnectionInfo>();
        final VimConnectionInfo vimConnectionInfo = new VimConnectionInfo();
        vimConnectionInfo.setId("vimconnect123");
        vimConnectionInfo.setVimId("vim123");
        vimConnectionInfo.setVimName("vim123");
        vimConnectionInfo.setVimType(CloudTypes.CEE);
        vimConnectionInfo.setAccessInfo(createAccessInfo());
        vimConnectionInfo.setInterfaceInfo(createInterfaceInfo());
        vimConnectionInfos.add(vimConnectionInfo);
        return vimConnectionInfos;
    }

    private InterfaceInfo createInterfaceInfo() {
        final InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setInterfaceEndpoint("auth123");
        return interfaceInfo;
    }

    private AccessInfo createAccessInfo() {
        final AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDomainName("domain123");
        accessInfo.setProjectId("proj123");
        accessInfo.setProjectName("proj123");
        final Credentials cred = new Credentials();
        cred.setUsername("user123");
        cred.setPassword("pass123");
        accessInfo.setCredentials(cred);
        return accessInfo;
    }

    private InstantiatedVnfInfo createInstantiatedVnfInfo() {
        final InstantiatedVnfInfo instantiatedVnfInfo = new InstantiatedVnfInfo();
        instantiatedVnfInfo.setFlavourId("flavor123");
        instantiatedVnfInfo.setVappId("vapp123");
        instantiatedVnfInfo.setVappName("vappname123");
        instantiatedVnfInfo.setExtVirtualLinkInfo(createExtVirtualLinkInfo());
        instantiatedVnfInfo.setVnfVirtualLinkResourceInfo(createVirtualLinkResourceInfo());
        instantiatedVnfInfo.setVirtualStorageResourceInfo(createVirtualStorageResourceInfo());
        instantiatedVnfInfo.setVnfcResourceInfo(createVnfcResourceInfo());
        return instantiatedVnfInfo;
    }

    private List<VnfcResourceInfo> createVnfcResourceInfo() {
        final List<VnfcResourceInfo> VnfcResourceInfos = new ArrayList<VnfcResourceInfo>();
        final VnfcResourceInfo vnfcResourceInfo = new VnfcResourceInfo();
        vnfcResourceInfo.setId("vnfc123");
        vnfcResourceInfo.setVduId("vdu123");
        final List<String> storageResourceIds = new ArrayList<String>();
        storageResourceIds.add("storageRes1");
        storageResourceIds.add("storageRes2");
        vnfcResourceInfo.setStorageResourceIds(storageResourceIds);
        final ResourceHandle resourceHandle = new ResourceHandle();
        resourceHandle.setResourceId("vnfcRes123");
        resourceHandle.setVimConnectionId("vimconnect123");
        resourceHandle.setVimLevelResourceType("OS::Nova::Server");
        vnfcResourceInfo.setComputeResource(resourceHandle);
        VnfcResourceInfos.add(vnfcResourceInfo);
        return VnfcResourceInfos;
    }

    private List<VirtualStorageResourceInfo> createVirtualStorageResourceInfo() {
        final List<VirtualStorageResourceInfo> virtualStorageResourceInfos = new ArrayList<VirtualStorageResourceInfo>();
        final VirtualStorageResourceInfo virtualStorageResourceInfo = new VirtualStorageResourceInfo();
        virtualStorageResourceInfo.setId("storage1");
        virtualStorageResourceInfo.setVirtualStorageDescId("storageDesc1");
        final ResourceHandle resourceHandle = new ResourceHandle();
        resourceHandle.setResourceId("storageRes1");
        resourceHandle.setVimConnectionId("vimconnect123");
        resourceHandle.setVimLevelResourceType("OS::Cinder::VolumeAttachment");
        virtualStorageResourceInfo.setStorageResource(resourceHandle);
        virtualStorageResourceInfos.add(virtualStorageResourceInfo);
        return virtualStorageResourceInfos;
    }

    private List<VnfVirtualLinkResourceInfo> createVirtualLinkResourceInfo() {
        final List<VnfVirtualLinkResourceInfo> vnfVirtualLinkResourceInfos = new ArrayList<VnfVirtualLinkResourceInfo>();
        final VnfVirtualLinkResourceInfo vnfVirtualLinkResourceInfo = new VnfVirtualLinkResourceInfo();
        vnfVirtualLinkResourceInfo.setId("intVirLink123");
        vnfVirtualLinkResourceInfo.setVirtualLinkDescId("intVirLinkDesc123");
        final ResourceHandle resourceHandle = new ResourceHandle();
        resourceHandle.setResourceId("intVirLinkRes123");
        resourceHandle.setVimConnectionId("vimconnect123");
        resourceHandle.setVimLevelResourceType("OS::Neutron::Net");
        vnfVirtualLinkResourceInfo.setNetworkResource(resourceHandle);
        vnfVirtualLinkResourceInfos.add(vnfVirtualLinkResourceInfo);
        return vnfVirtualLinkResourceInfos;
    }

    private List<ExtVirtualLinkInfo> createExtVirtualLinkInfo() {
        final List<ExtVirtualLinkInfo> extVirtualLinkInfos = new ArrayList<ExtVirtualLinkInfo>();
        final ExtVirtualLinkInfo extVirtualLinkInfo = new ExtVirtualLinkInfo();
        extVirtualLinkInfo.setId("extVirLink123");
        final ResourceHandle resourceHandle = new ResourceHandle();
        resourceHandle.setResourceId("extVirLinkRes123");
        resourceHandle.setVimConnectionId("vimconnect123");
        extVirtualLinkInfo.setResourceHandle(resourceHandle);
        extVirtualLinkInfo.setLinkPorts(createLinkPorts());
        extVirtualLinkInfos.add(extVirtualLinkInfo);
        return extVirtualLinkInfos;
    }

    private List<ExtLinkPort> createLinkPorts() {
        final List<ExtLinkPort> extLinkPorts = new ArrayList<ExtLinkPort>();
        final ExtLinkPort extLinkPort = new ExtLinkPort();
        extLinkPort.setId("extPort123");
        extLinkPort.setName("extPort123");
        final ResourceHandle resourceHandle = new ResourceHandle();
        resourceHandle.setResourceId("extPortRes123");
        resourceHandle.setVimConnectionId("vimconnect123");
        resourceHandle.setVimLevelResourceType("OS::Neutron::Port");
        extLinkPort.setResourceHandle(resourceHandle);
        extLinkPorts.add(extLinkPort);
        return extLinkPorts;
    }

    private VnfInstance_links createLinks() {
        final VnfInstance_links vnfLnks = new VnfInstance_links();
        final Link selfLink = new Link();
        selfLink.setHref("/vevnfmem/vnflcm/v1/vnf_instances/{vnfInstanceId}");
        final Link instantiateLink = new Link();
        instantiateLink.setHref("/vevnfmem/vnflcm/v1/vnf_instances/{vnfInstanceId}/instantiate");
        final Link terminateLink = new Link();
        terminateLink.setHref("/vevnfmem/vnflcm/v1/vnf_instances/{vnfInstanceId}");
        vnfLnks.setSelf(selfLink);
        vnfLnks.setInstantiate(instantiateLink);
        vnfLnks.setTerminate(terminateLink);
        return vnfLnks;
    }
}
