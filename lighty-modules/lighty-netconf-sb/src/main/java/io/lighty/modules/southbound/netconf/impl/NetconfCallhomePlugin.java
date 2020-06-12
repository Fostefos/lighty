/*
 * Copyright (c) 2018 Pantheon Technologies s.r.o. All Rights Reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v10.html
 */
package io.lighty.modules.southbound.netconf.impl;

import io.lighty.core.controller.api.AbstractLightyModule;
import io.lighty.core.controller.api.LightyServices;
import java.util.concurrent.ExecutorService;
import org.opendaylight.aaa.encrypt.AAAEncryptionService;
import org.opendaylight.netconf.callhome.mount.CallHomeMountDispatcher;
import org.opendaylight.netconf.callhome.mount.IetfZeroTouchCallHomeServerProvider;
import org.opendaylight.netconf.callhome.mount.SchemaRepositoryProviderImpl;
import org.opendaylight.netconf.topology.api.SchemaRepositoryProvider;
import org.slf4j.LoggerFactory;

public class NetconfCallhomePlugin extends AbstractLightyModule {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(NetconfCallhomePlugin.class);

    private final IetfZeroTouchCallHomeServerProvider provider;

    public NetconfCallhomePlugin(final LightyServices lightyServices, final String topologyId,
                                 ExecutorService executorService, final AAAEncryptionService encryptionService) {
        super(executorService);
        final SchemaRepositoryProvider schemaRepositoryProvider =
                new SchemaRepositoryProviderImpl("shared-schema-repository-impl");
        final CallHomeMountDispatcher dispatcher = new CallHomeMountDispatcher(topologyId,
                lightyServices.getEventExecutor(), lightyServices.getScheduledThreaPool(),
                lightyServices.getThreadPool(), schemaRepositoryProvider, lightyServices.getBindingDataBroker(),
                lightyServices.getDOMMountPointService(), encryptionService);
        provider = new IetfZeroTouchCallHomeServerProvider(lightyServices.getBindingDataBroker(), dispatcher);
    }

    @Override
    protected boolean initProcedure() {
        provider.init();
        return true;
    }

    @SuppressWarnings("checkstyle:illegalCatch")
    @Override
    protected boolean stopProcedure() {
        try {
            provider.close();
        } catch (Exception e) {
            LOG.error("{} failed to close!", provider.getClass(), e);
            return false;
        }
        return true;
    }

}
