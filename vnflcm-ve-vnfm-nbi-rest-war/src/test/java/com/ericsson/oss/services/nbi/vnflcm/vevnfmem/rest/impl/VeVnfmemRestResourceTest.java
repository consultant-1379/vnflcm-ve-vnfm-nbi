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
package com.ericsson.oss.services.nbi.vnflcm.vevnfmem.rest.impl;

import java.util.HashSet;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.exception.mappers.ExceptionResponseMapper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.exception.mappers.VeVnfmemConstraintViolationExceptionMapper;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.NBIRestApplication;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.VnfRestServiceImpl;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl.VnfLcmOperationRestServiceImpl;
import com.ericsson.oss.services.vnflcm.api_base.FileResourceService;
import com.ericsson.oss.services.vnflcm.api_base.LcmOperationService;
import com.ericsson.oss.services.vnflcm.api_base.VnfService;

public class VeVnfmemRestResourceTest {

    protected static final String ROOT_RESOURCE_PATH = "/vevnfmem";

    protected static VnfService vnfServiceMock;

    protected static FileResourceService fileResourceServiceMock;

    protected static LcmOperationService lcmOperationServiceMock;

    public static class NBIRestApplicationWrapper extends NBIRestApplication {

        /**
         *
         */
        public NBIRestApplicationWrapper(final VnfService vnfService, final FileResourceService fileResourceService) {
            vnfServiceMock = vnfService;
            fileResourceServiceMock = fileResourceService;
        }

        /**
         *
         */
        public NBIRestApplicationWrapper() {

        }

        @Override
        public HashSet<Class<?>> getClasses() {
                    final HashSet<Class<?>> classes = new HashSet<Class<?>>();
            classes.add(VnfRestServiceWrapper.class);
            classes.add(VeVnfmemConstraintViolationExceptionMapper.class);
            classes.add(ExceptionResponseMapper.class);
            return classes;
        }
    }

    public static class VnfRestServiceWrapper extends VnfRestServiceImpl {

        public VnfRestServiceWrapper() {
            setVnfService(vnfServiceMock);
            setFileResourceService(fileResourceServiceMock);
            setUnitTesting(true);
        }
    }

    public static class CreateVnfLcmOperationRestServiceImplWrapper extends VnfLcmOperationRestServiceImpl {

        public CreateVnfLcmOperationRestServiceImplWrapper() {
            setLcmOperationService(lcmOperationServiceMock);
            setUnitTesting(true);
        }
    }
}