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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface MetricsExporterRestService {

    /**
     * Rest Interface to expose VNFLCM metrics in prometheus TextFormat to ADP PM
     * 
     * @return Response containing metrics in text format
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/metrics")
    public Response returnMetrics();

}
