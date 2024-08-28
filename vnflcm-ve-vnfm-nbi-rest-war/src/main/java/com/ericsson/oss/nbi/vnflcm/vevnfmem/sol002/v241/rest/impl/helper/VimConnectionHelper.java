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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InstantiateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimConnectionInformation;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VimType;
import com.ericsson.oss.services.vnflcm.api_base.VNFLCMServiceException;

import com.ericsson.oss.services.vnflcm.api_base.VimService;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.AccessInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.ChangeCurrentVnfPkgRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.Credentials;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.InterfaceInfo;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.v241.rest.model.VcdOnboardPackageRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiChangeVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiInstantiateVnfRequest;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.sol002.vimConnection.model.NbiVcdPackageOnboardRequest;
import com.ericsson.oss.services.vnflcm.common.exceptions.AuthenticationException;
import com.ericsson.oss.services.vnflcm.common.exceptions.ConnectionFailureException;
import com.ericsson.oss.services.vnflcm.common.exceptions.RestClientException;
import com.ericsson.oss.services.vnflcm.common.exceptions.RestResponseException;
import com.ericsson.oss.services.vnflcm.common.models.RestRequest;
import com.ericsson.oss.services.vnflcm.common.models.RestResponse;
import com.ericsson.oss.services.vnflcm.common.util.RestClient;

/**
 * This Class will adapt the Key Value Pairs in VimConnection info which are send in the Instantiate request. And will create a the one
 * which is understood by NBI, so no change in the down layers.
 *
 * @author xkurmaj
 */
public class VimConnectionHelper {


    private static final Logger LOGGER = LoggerFactory.getLogger(VimConnectionHelper.class);

    @EServiceRef
    private static VimService vimService;

    private static final String CREDENTIALS = "credentials";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    public static final String OPENSTACK_KEYSTONE_VERSION3 = "/v3";
    public static final String OPENSTACK_KEYSTONE_VERSION2 = "/v2.0";
    public static final String OPENSTACK_KEYSTONE_TOKEN_SERVICE_V3 = "auth/tokens";
    public static final String OPENSTACK_KEYSTONE_TOKEN_SERVICE = "tokens";
    static final Map<List<String>, String> INTERFACE_INFO_MAP = new HashMap<>();
    static {

        final List<String> authEndpoints = new ArrayList<>();
        // possible endpoints for identity service. If any nfvo has other endpoint add here.
        authEndpoints.add("identityendpoint");//ECM
        authEndpoints.add("authurl");
        authEndpoints.add("authendpoint");
        authEndpoints.add("identityurl");
        authEndpoints.add("endpoint");//HPE
        INTERFACE_INFO_MAP.put(authEndpoints, "identityEndPoint");
    }

    static final Map<List<String>, String> ACCESS_INFO_MAP = new HashMap<>();
    static {
        final List<String> projectIds = new ArrayList<>();
        projectIds.add("projectid");//ECM
        projectIds.add("projectId");//ECM
        projectIds.add("tenantId");//HPE
        projectIds.add("tenantid");//HPE
        ACCESS_INFO_MAP.put(projectIds, "projectId");

        final List<String> projectNames = new ArrayList<>();
        projectNames.add("projectname");
        projectNames.add("projectName");
        projectNames.add("organization");
        projectNames.add("tenant");//HPE v3
        projectNames.add("project");//HPE v2.0
        ACCESS_INFO_MAP.put(projectNames, "projectName");

        final List<String> domainNames = new ArrayList<>();
        domainNames.add("domainname");//ECM
        domainNames.add("domainName");//ECM
        domainNames.add("projectdomain");//HPE
        domainNames.add("projectDomain");//HPE
        domainNames.add("domain");
        ACCESS_INFO_MAP.put(domainNames, "domainName");

        final List<String> userDomains = new ArrayList<>();
        userDomains.add("userdomain");//ECM, HPE
        userDomains.add("userDomain");//ECM, HPE
        ACCESS_INFO_MAP.put(userDomains, "userDomain");

        final List<String> credentials = new ArrayList<>();
        credentials.add(CREDENTIALS);//ECM
        credentials.add("usercredentials");
        credentials.add("authcredentials");
        ACCESS_INFO_MAP.put(credentials, CREDENTIALS);
    }

    public static NbiInstantiateVnfRequest getInstantiatedVnfRequest(final InstantiateVnfRequest instantiatedVnfRequest) throws VNFLCMServiceException {
        LOGGER.info("VimConnectionHelper: getInstantiatedVnfRequest(): Start the adaptation of Vimconenction in Instantiate request.");
        final NbiInstantiateVnfRequest instantiatedVnfReq = new NbiInstantiateVnfRequest();
        instantiatedVnfReq.setFlavourId(instantiatedVnfRequest.getFlavourId());
        instantiatedVnfReq.setInstantiationLevelId(instantiatedVnfRequest.getInstantiationLevelId());
        instantiatedVnfReq.setAdditionalParams(instantiatedVnfRequest.getAdditionalParams());
        instantiatedVnfReq.setExtVirtualLinks(instantiatedVnfRequest.getExtVirtualLinks());
        instantiatedVnfReq.setExtManagedVirtualLinks(instantiatedVnfRequest.getExtManagedVirtualLinks());
        final List<VimConnectionInfo> nbiVimConnectionInfos = new ArrayList<>();
        for (final VimConnectionInformation vimConnectionInformation : instantiatedVnfRequest.getVimConnectionInfo()) {
            final VimConnectionInfo nbiVimConnInfo = new VimConnectionInfo();
            nbiVimConnInfo.setId(vimConnectionInformation.getId());
            nbiVimConnInfo.setVimId(vimConnectionInformation.getVimId());

            // map incoming vimTypes i.e VMWARE_VCLOUD( Netcracker NFVO) , VCD ( EO-CM ) to VCD
            if (vimConnectionInformation.getVimType() != null && (vimConnectionInformation.getVimType().contains("VMWARE_VCLOUD") || vimConnectionInformation.getVimType().contains("VCD"))) {
                LOGGER.debug("VimConnectionHelper: getInstantiatedVnfRequest(): Setting vimConnectionInformation Vim type as VCD from incoming type : {}",
                        vimConnectionInformation.getVimType());
                nbiVimConnInfo.setVimType("VCD");
            } else {
                LOGGER.info("VimConnectionHelper: vimConnectionInformation.getVimType(): {}", vimConnectionInformation.getVimType());
                if (vimConnectionInformation.getVimType()!= null && vimConnectionInformation.getVimType().toUpperCase().contains("OPENSTACK")) {
                    vimConnectionInformation.setVimType("OPENSTACK");
                }
                nbiVimConnInfo.setVimType(vimConnectionInformation.getVimType());
                LOGGER.info("VimConnectionHelper: nbiVimConnInfo.getVimType(): {}", nbiVimConnInfo.getVimType());
            }
            try {
                if (vimConnectionInformation.getInterfaceInfo() != null && !vimConnectionInformation.getInterfaceInfo().isEmpty()) {
                    nbiVimConnInfo.setInterfaceInfo(createInterfaceInfo(vimConnectionInformation));
                }
                if (vimConnectionInformation.getAccessInfo() != null && !vimConnectionInformation.getAccessInfo().isEmpty()) {

                    nbiVimConnInfo.setAccessInfo(createAccessInfo(vimConnectionInformation));
                }
            } catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                LOGGER.error("VimConnectionHelper: getInstantiatedVnfRequest(): Error in accessing the Interface info and access info.");
            }
            //nbiVimConnInfo.setExtra(vimConnectionInfo.getAccessInfo());
            nbiVimConnectionInfos.add(nbiVimConnInfo);
        }
        instantiatedVnfReq.setVimConnectionInfo(nbiVimConnectionInfos);
        LOGGER.info("VimConnectionHelper: getInstantiatedVnfRequest(): DONE");
        return instantiatedVnfReq;
    }

    public static NbiChangeVnfRequest getChangeVnfRequest(final ChangeCurrentVnfPkgRequest changeCurrentVnfRequest) {
        LOGGER.info("VimConnectionHelper: getChangeVnfRequest(): Start the adaptation of Vimconenction in Change Vnf request.");
        final NbiChangeVnfRequest nbiChangeVnfRequest = new NbiChangeVnfRequest();
        nbiChangeVnfRequest.setAdditionalParams(changeCurrentVnfRequest.getAdditionalParams());
        nbiChangeVnfRequest.setExtensions(changeCurrentVnfRequest.getExtensions());
        nbiChangeVnfRequest.setExtManagedVirtualLinks(changeCurrentVnfRequest.getExtManagedVirtualLinks());
        nbiChangeVnfRequest.setExtVirtualLinks(changeCurrentVnfRequest.getExtVirtualLinks());
        nbiChangeVnfRequest.setVnfConfigurableProperties(changeCurrentVnfRequest.getVnfConfigurableProperties());
        nbiChangeVnfRequest.setVnfdId(changeCurrentVnfRequest.getVnfdId());
        final List<VimConnectionInfo> nbiVimConnectionInfos = new ArrayList<>();
        if(changeCurrentVnfRequest.getVimConnectionInfo() != null && !changeCurrentVnfRequest.getVimConnectionInfo().isEmpty()) {
            for (final VimConnectionInformation vimConnectionInformation : changeCurrentVnfRequest.getVimConnectionInfo()) {
                final VimConnectionInfo nbiVimConnInfo = new VimConnectionInfo();
                nbiVimConnInfo.setId(vimConnectionInformation.getId());
                nbiVimConnInfo.setVimId(vimConnectionInformation.getVimId());

                // map incoming vimTypes i.e VMWARE_VCLOUD( Netcracker NFVO) , VCD ( EO-CM ) to VCD
                if (vimConnectionInformation.getVimType() != null && (vimConnectionInformation.getVimType().contains("VMWARE_VCLOUD") || vimConnectionInformation.getVimType().contains("VCD"))) {
                    LOGGER.debug("VimConnectionHelper: getChangeVnfRequest(): Setting vimConnectionInformation Vim type as VCD from incoming type : {}",
                            vimConnectionInformation.getVimType());
                    nbiVimConnInfo.setVimType("VCD");
                } else {
                    LOGGER.info("VimConnectionHelper: vimConnectionInformation.getVimType(): {}", vimConnectionInformation.getVimType());
                    if (vimConnectionInformation.getVimType()!= null && vimConnectionInformation.getVimType().toUpperCase().contains("OPENSTACK")) {
                        vimConnectionInformation.setVimType("OPENSTACK");
                    }
                    nbiVimConnInfo.setVimType(vimConnectionInformation.getVimType());
                    LOGGER.info("VimConnectionHelper: nbiVimConnInfo.getVimType(): {}", nbiVimConnInfo.getVimType());
                }
                try {
                    if (vimConnectionInformation.getInterfaceInfo() != null && !vimConnectionInformation.getInterfaceInfo().isEmpty()) {
                        nbiVimConnInfo.setInterfaceInfo(createInterfaceInfo(vimConnectionInformation));
                    }
                    if (vimConnectionInformation.getAccessInfo() != null && !vimConnectionInformation.getAccessInfo().isEmpty()) {
                        nbiVimConnInfo.setAccessInfo(createAccessInfo(vimConnectionInformation));
                    }
                } catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    LOGGER.error("VimConnectionHelper: getChangeVnfRequest(): Error in accessing the Interface info and access info.");
                }
                //nbiVimConnInfo.setExtra(vimConnectionInfo.getAccessInfo());
                nbiVimConnectionInfos.add(nbiVimConnInfo);
            }
            nbiChangeVnfRequest.setVimConnectionInfo(nbiVimConnectionInfos);
        }
        LOGGER.info("VimConnectionHelper: getChangeVnfRequest(): DONE");
        return nbiChangeVnfRequest;
    }

    public static NbiVcdPackageOnboardRequest getVcdPackageOnboardRequest(final VcdOnboardPackageRequest onboardRequest) {
        final NbiVcdPackageOnboardRequest nbiReq = new NbiVcdPackageOnboardRequest();
        nbiReq.setCatalogName(onboardRequest.getCatalogName());
        nbiReq.setOvfDirectory(onboardRequest.getOvfDirectory());
        nbiReq.setOvfPackage(onboardRequest.getOvfPackage());
        nbiReq.setvAppTemplateDescription(onboardRequest.getvAppTemplateDescription());
        nbiReq.setvAppTemplateName(onboardRequest.getvAppTemplateName());

        final List<VimConnectionInfo> nbiVimConnectionInfos = new ArrayList<>();
        // start
        if (onboardRequest.getVimConnectionInfo() !=null && !onboardRequest.getVimConnectionInfo().isEmpty()) {
            for (final VimConnectionInformation vimConnectionInformation : onboardRequest.getVimConnectionInfo()) {
                final VimConnectionInfo nbiVimConnInfo = new VimConnectionInfo();
                nbiVimConnInfo.setId(vimConnectionInformation.getId());
                nbiVimConnInfo.setVimId(vimConnectionInformation.getVimId());

                // map incoming vimTypes i.e VMWARE_VCLOUD( Netcracker NFVO) , VCD ( EO-CM ) to VCD
                if (vimConnectionInformation.getVimType() != null && (vimConnectionInformation.getVimType().contains("VMWARE_VCLOUD") || vimConnectionInformation.getVimType().contains("VCD"))) {
                    LOGGER.debug("VimConnectionHelper: getVcdPackageOnboardRequest(): Setting vimConnectionInformation Vim type as VCD from incoming type : {}",
                            vimConnectionInformation.getVimType());
                    nbiVimConnInfo.setVimType("VCD");
                }
                try {
                    if (vimConnectionInformation.getInterfaceInfo() != null && !vimConnectionInformation.getInterfaceInfo().isEmpty()) {
                        nbiVimConnInfo.setInterfaceInfo(createInterfaceInfo(vimConnectionInformation));
                    }
                    if (vimConnectionInformation.getAccessInfo() != null && !vimConnectionInformation.getAccessInfo().isEmpty()) {
                        nbiVimConnInfo.setAccessInfo(createAccessInfo(vimConnectionInformation));
                    }
                } catch (final NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    LOGGER.error("VimConnectionHelper: getVcdPackageOnboardRequest(): Error in accessing the Interface info and access info.");
                }
                //nbiVimConnInfo.setExtra(vimConnectionInfo.getAccessInfo());
                nbiVimConnectionInfos.add(nbiVimConnInfo);
            }
        }
        //end
        nbiReq.setVimConnectionInfo(nbiVimConnectionInfos);
        LOGGER.info("VimConnectionHelper: getVcdPackageOnboardRequest(): DONE");
        return nbiReq;
    }

    private static InterfaceInfo createInterfaceInfo(final VimConnectionInformation vimConnectionInformation)
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        //final KeyValuePairs interfaceInfo = vimConnectionInfo.getInterfaceInfo();
        final Map<String, Object> keyValuePairs = vimConnectionInformation.getInterfaceInfo();
        final Iterator<String> interfaceInfoIterator = keyValuePairs.keySet().iterator();
        final InterfaceInfo nbiInterfaceInfo = new InterfaceInfo();
        while (interfaceInfoIterator.hasNext()) {
            final String key = interfaceInfoIterator.next();
            final Iterator<Entry<List<String>, String>> interfaceInfoMapIterator = INTERFACE_INFO_MAP.entrySet().iterator();
            while (interfaceInfoMapIterator.hasNext()) {
                final Entry<List<String>, String> interfaceInfoEntry = interfaceInfoMapIterator.next();
                final List<String> keys = interfaceInfoEntry.getKey();
                if (keys.contains(key.toLowerCase())) {
                    final String value = (String) keyValuePairs.get(key);
                    final String fieldName = interfaceInfoEntry.getValue();
                    final Class<? extends InterfaceInfo> nbiInterfaceInfoClass = nbiInterfaceInfo.getClass();
                    final Field identityEndPoint = nbiInterfaceInfoClass.getDeclaredField(fieldName);
                    identityEndPoint.setAccessible(true);
                    if (vimConnectionInformation.getVimType() != null && !vimConnectionInformation.getVimType().isEmpty()
                            && !vimConnectionInformation.getVimType().equals(VimType.VCD.toString())) {
                        if (!vimConnectionInformation.getVimType().contains(VimType.VMWARE_VCLOUD.toString())) {
                            if (!value.endsWith(OPENSTACK_KEYSTONE_VERSION3)
                                    && !value.endsWith(OPENSTACK_KEYSTONE_VERSION2)) {
                                final String newValue = value + OPENSTACK_KEYSTONE_VERSION3;
                                LOGGER.info(
                                        "VimConnectionHelper: modified identityEndpoint since url is not valid. newValue(): {}",
                                        newValue);
                                identityEndPoint.set(nbiInterfaceInfo, newValue);
                            } else {
                                identityEndPoint.set(nbiInterfaceInfo, value);
                            }
		        } else {
                            identityEndPoint.set(nbiInterfaceInfo, value);
                        }
                    } else {
                        identityEndPoint.set(nbiInterfaceInfo, value);
                    }
                    break;
                }
            }
        }
        return nbiInterfaceInfo;
    }

    @SuppressWarnings("unchecked")
    private static AccessInfo createAccessInfo(final VimConnectionInformation vci)
        throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        //final KeyValuePairs accessInfo = vci.getAccessInfo();
        final Map<String, Object> keyValuePairs = vci.getAccessInfo();
        final Iterator<String> accessInfoIterator = keyValuePairs.keySet().iterator();
        final AccessInfo nbiAccessInfo = new AccessInfo();
        final Credentials nbiVimCredentials = new Credentials();
        while (accessInfoIterator.hasNext()) {
            final String key = accessInfoIterator.next();
            final Object object = keyValuePairs.get(key);
            String value = "";
            Map<String, Object> credentialsMap = null;
            if (object instanceof String) {
                value = (String) object;
            } else if (key.equalsIgnoreCase(CREDENTIALS) && object instanceof Map) { //If more such property added in AccessInfo create map for each property
                credentialsMap = (Map<String, Object>) object;
            }
            final Iterator<Entry<List<String>, String>> accessInfoMapIterator = ACCESS_INFO_MAP.entrySet().iterator();
            while (accessInfoMapIterator.hasNext()) {
                final Entry<List<String>, String> entry = accessInfoMapIterator.next();
                final List<String> keys = entry.getKey();
                if (keys.contains(key.toLowerCase()) && (!key.equalsIgnoreCase(USERNAME) || !key.equalsIgnoreCase(PASSWORD))) {
                    final String fieldName = entry.getValue();
                    if (fieldName.equalsIgnoreCase(CREDENTIALS)) {
                        String userName = ((String) credentialsMap.get(USERNAME)).trim();
                        final String password = ((String) credentialsMap.get(PASSWORD)).trim();
                        //for EO-CM , username and password will be both Base64 encoded
                        if (vci.getVimType()!= null && !vci.getVimType().isEmpty() && vci.getVimType().contains(VimType.VCD.toString())) {
                            userName = new String(Base64.decodeBase64(userName));
                        }
                        nbiVimCredentials.setUsername(userName);
                        nbiVimCredentials.setPassword(password);
                        nbiAccessInfo.setCredentials(nbiVimCredentials);
                    } else {
                        final Class<? extends AccessInfo> nbiAccessClass = nbiAccessInfo.getClass();
                        final Field accessInfoField = nbiAccessClass.getDeclaredField(fieldName);
                        accessInfoField.setAccessible(true);
                        accessInfoField.set(nbiAccessInfo, value);
                    }
                    break;
                } else if (key.equalsIgnoreCase(USERNAME) || key.equalsIgnoreCase(PASSWORD)) {
                    // Credentials object may not come in vimconnectioninfo as in case of Netcracker format
                    // parse the username and password directly from accessInfo
                    if (key.equalsIgnoreCase(USERNAME)) {
                        nbiVimCredentials.setUsername(value);
                    } else if (key.equalsIgnoreCase(PASSWORD)) {
                        nbiVimCredentials.setPassword(value);
                    }
                    nbiAccessInfo.setCredentials(nbiVimCredentials);
                }
            }
            // VimType will come as VCD in incoming NBI raw request from EO-CM
            if (vci.getVimType()!= null && !vci.getVimType().isEmpty() && (vci.getVimType().equals(VimType.VCD.toString()))) {
                /*
                 * For EO-CM , domainName associates to Organization Name . SDK reads organization Name from projectName To maintain
                 * compatibilithy with SDK , rewrite the projectName as domainName. Org VDC and other VCD params are taken from Grant
                 * Response as per study.
                 */
                if(nbiAccessInfo.getDomainName()!=null && !nbiAccessInfo.getDomainName().isEmpty()) {
                    nbiAccessInfo.setProjectName(nbiAccessInfo.getDomainName());
                }
            }
            if (vci.getVimType()!= null && (vci.getVimType().contains(VimType.VMWARE_VCLOUD.toString()))) {
                /*
                 * For Netcracker , organization key maps to projectName and prefix of "ORG-" is removed to extract only the organization
                 * name
                 */
                String organizationName = nbiAccessInfo.getProjectName();
                if (organizationName != null && organizationName.contains("ORG-")) {
                    organizationName = nbiAccessInfo.getProjectName().substring(4);
                }
                nbiAccessInfo.setProjectName(organizationName);
                nbiAccessInfo.setDomainName(organizationName);
            }
        }
        return nbiAccessInfo;

    }

    /**
     * This method will be called for GHOST authentication call, in the cases where there is some certifciate issues and Certificate
     * handshake to be disabled in https. This is only recommneded for POC/PlugTest/Demo etc. Standard product will have the proper SSL
     * certificate handshake.
     *
     * @param authUrl
     * @param userName
     * @param password
     * @param projectId
     * @param projectName
     * @param domainId
     * @param domainName
     * @param userDomain
     * @return
     */
    public static RestResponse createAuthRequestObject(final String authUrl, final String userName, final String password, final String projectId,
        final String projectName, final String domainId, String domainName, String userDomain) {
        String service = null;
        String reqBodyString = null;
        if (authUrl.contains(OPENSTACK_KEYSTONE_VERSION3)) { // V3 Authentication
            if (domainName == null || domainName.isEmpty()) {
                domainName = "Default";
            }
            if (userDomain == null || userDomain.isEmpty()) {
                userDomain = domainName;
            }
            if (projectName != null && !projectName.isEmpty()) {
                reqBodyString = "{\"auth\": {\"identity\": {\"methods\": [\"password\"],\"password\": {\"user\": {\"name\":\"" + userName + "\","
                        + "\"domain\": {\"name\": \"" + userDomain + "\"},\"password\": \"" + password + "\"}}},"
                        + "\"scope\": {\"project\": {\"name\":\"" + projectName + "\", \"domain\": {\"name\": \"" + domainName + "\"}}}}}";
                service = OPENSTACK_KEYSTONE_TOKEN_SERVICE_V3;
            } else if (projectId != null && !projectId.isEmpty()) {
                reqBodyString = "{\"auth\": {\"identity\": {\"methods\": [\"password\"],\"password\": {\"user\": {\"name\":\"" + userName + "\","
                        + "\"domain\": {\"name\": \"" + userDomain + "\"},\"password\": \"" + password + "\"}}}, \"scope\": {\"project\": {\"id\":\""
                        + projectId + "\"}}}}";
                service = OPENSTACK_KEYSTONE_TOKEN_SERVICE_V3;
            }
        } else { // V2 Authentication
            if (projectName != null && !projectName.isEmpty()) {
                reqBodyString = "{\"auth\":{\"passwordCredentials\":{\"username\":\"" + userName + "\"," + "\"password\":\"" + password + "\"},"
                        + "\"tenantId\":\"" + projectId + "\"}}";
                service = OPENSTACK_KEYSTONE_TOKEN_SERVICE;
            } else {
                reqBodyString = "{\"auth\":{\"passwordCredentials\":{\"username\":\"" + userName + "\"," + "\"password\":\"" + password + "\"},"
                        + "\"tenantName\":\"" + projectName + "\"}}";
                service = OPENSTACK_KEYSTONE_TOKEN_SERVICE;
            }
        }
        // Add the content type header
        final Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        final RestRequest req = new RestRequest(authUrl, RestRequest.methodTypes.POST.toString(), reqBodyString, headerMap, service);

        try {
            return RestClient.doPost(req.getBaseUrl(), req.getRequestHeader(), req.getRequestBody());
        } catch (RestClientException | ConnectionFailureException | RestResponseException | AuthenticationException e) {
            LOGGER.info("VimConnectionHelper: Create Auth request has osme issues..");
        }
        return null;
    }
}
