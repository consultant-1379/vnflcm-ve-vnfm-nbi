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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Summary;
import io.prometheus.client.hotspot.DefaultExports;

/*
 * This class registers VNFLCM metrics in Prometheus default registry on war deployment and HTTP servlet context initialization
 */
public class VnflcmHttpServletContextListener
        implements ServletContextListener {

    private static final Summary CREATE_VNF_REQUEST_LATENCY = Summary.build()
            .name("createVnfRequestLatency")
            .help("Create Vnf request Latency in seconds").register();
    private static final Summary INSTANTIATE_VNF_REQUEST_LATENCY = Summary
            .build().name("instantiateVnfRequestLatency")
            .help("Instantiate Vnf request Latency in seconds").register();
    private static final Summary SCALE_VNF_REQUEST_LATENCY = Summary.build()
            .name("scaleVnfRequestLatency")
            .help("Scale Vnf request Latency in seconds").register();
    private static final Summary TERMINATE_VNF_REQUEST_LATENCY = Summary.build()
            .name("terminateVnfRequestLatency")
            .help("Terminate Vnf request Latency in seconds").register();
    private static final Summary HEAL_VNF_REQUEST_LATENCY = Summary.build()
            .name("healVnfRequestLatency")
            .help("Heal Vnf request Latency in seconds").register();
    private static final Summary OPERATE_VNF_REQUEST_LATENCY = Summary.build()
            .name("operateVnfRequestLatency")
            .help("Operate Vnf request Latency in seconds").register();
    private static final Summary MODIFY_VNF_REQUEST_LATENCY = Summary.build()
            .name("modifyVnfRequestLatency")
            .help("Modify Vnf request Latency in seconds").register();
    private static final Summary CHANGE_VNF_REQUEST_LATENCY = Summary.build()
            .name("changeVnfRequestLatency")
            .help("Change Vnf request Latency in seconds").register();
    private static final Summary DELETE_VNF_IDENTIFIER_REQUEST_LATENCY = Summary
            .build().name("deleteVnfIdentifierRequestLatency")
            .help("Delete Vnf Identifier request Latency in seconds")
            .register();

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent arg0) {
        // Clear the registry when servlet context is deleted
        // This is needed to avoid duplicate metric found in Default Registry error from Prometheus
        CollectorRegistry.defaultRegistry.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent arg0) {
        // When servlet context is initialized , expose the jvm metrics initially
        DefaultExports.initialize();
    }

    /**
     * @return the createVnfRequestLatency
     */
    public static Summary getCreateVnfRequestLatency() {
        return CREATE_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the scaleVnfRequestLatency
     */
    public static Summary getScaleVnfRequestLatency() {
        return SCALE_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the changeVnfRequestLatency
     */
    public static Summary getChangeVnfRequestLatency() {
        return CHANGE_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the terminateVnfRequestLatency
     */
    public static Summary getTerminateVnfRequestLatency() {
        return TERMINATE_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the instantiateVnfRequestLatency
     */
    public static Summary getInstantiateVnfRequestLatency() {
        return INSTANTIATE_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the healVnfRequestLatency
     */
    public static Summary getHealVnfRequestLatency() {
        return HEAL_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the operatevnfrequestlatency
     */
    public static Summary getOperateVnfRequestLatency() {
        return OPERATE_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the modifyvnfrequestlatency
     */
    public static Summary getModifyVnfRequestLatency() {
        return MODIFY_VNF_REQUEST_LATENCY;
    }

    /**
     * @return the deletevnfidentifierrequestlatency
     */
    public static Summary getDeleteVnfIdentifierRequestLatency() {
        return DELETE_VNF_IDENTIFIER_REQUEST_LATENCY;
    }

}
