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
package com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.impl;

import java.io.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.MetricsExporterRestService;
import com.ericsson.oss.nbi.vnflcm.vevnfmem.rest.VnfRestService;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

@Path(VnfRestService.PATH)
@Produces(MediaType.TEXT_PLAIN)
public class MetricsExporterRestServiceImpl implements MetricsExporterRestService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.nbi.vnflcm.orvnfm.rest.MetricsExporterRestService#returnMetrics()
     */
    @Override
    @GET
    @Produces("text/plain")
    @Path("/metrics")
    public Response returnMetrics() {
        logger.info("Metrics endpoint in vnflcm NBI is called");
        final Writer writer = new StringWriter();
        try {
            TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
        } catch (final IOException e) {
            logger.error("Error in writing metrics : " + e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
        }
        return Response.ok(writer).build();
    }
}
