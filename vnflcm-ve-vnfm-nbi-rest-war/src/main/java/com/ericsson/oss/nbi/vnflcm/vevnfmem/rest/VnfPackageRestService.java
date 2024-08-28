/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(VnfPackageRestService.PATH)
@Produces(MediaType.APPLICATION_JSON)
public interface VnfPackageRestService {

    String PATH = "/vnflcm";

    /**
    * This operation provides information about onboarded VNF Packages.
    * @return Response containing list of onboarded Vnfpackage
    */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/v1/vnf_packages")
    Response getVnfPackagesInfo();
}
