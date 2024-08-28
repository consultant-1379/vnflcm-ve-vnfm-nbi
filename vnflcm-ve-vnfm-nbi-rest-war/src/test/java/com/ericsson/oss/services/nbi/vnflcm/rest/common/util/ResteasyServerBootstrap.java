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

import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;

/**
 * The ResteasyServerBootstrap class provides support for rest call
 */
public class ResteasyServerBootstrap {

    private NettyJaxrsServer server;

    private String rootResourcePath;

    private static final int PORT = 58586;

    public ResteasyServerBootstrap(final String rootResourcePath, final Application application) {
        this.rootResourcePath = rootResourcePath;
        setupServer(application);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }

    private void setupServer(final Application application) {
        server = new NettyJaxrsServer();
        server.setRootResourcePath(rootResourcePath);
        server.setPort(PORT);
        server.getDeployment().setApplication(application);
    }

}
