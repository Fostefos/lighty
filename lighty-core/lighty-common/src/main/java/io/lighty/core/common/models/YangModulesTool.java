/*
 * Copyright (c) 2018 Pantheon Technologies s.r.o. All Rights Reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v10.html
 */
package io.lighty.core.common.models;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import org.opendaylight.yangtools.yang.binding.YangModuleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class YangModulesTool {

    private static final Logger LOG = LoggerFactory.getLogger(YangModulesTool.class);
    private static final int PREFIX = 2;

    private YangModulesTool() {
    }

    public static void main(String[] args) {
        Set<YangModuleInfo> allModelsFromClasspath = YangModuleUtils.getAllModelsFromClasspath();
        printModelInfo(allModelsFromClasspath);
    }

    @SuppressFBWarnings(value = "SLF4J_SIGN_ONLY_FORMAT", justification = "Special log formatting.")
    public static void printModelInfo(Set<YangModuleInfo> allModelsFromClasspath) {
        int prefixLength = 0;
        Set<YangModuleInfo> topLevelModels = YangModuleUtils.filterTopLevelModels(allModelsFromClasspath);
        LOG.info("# top-level models tree: {}", topLevelModels.size());
        for (YangModuleInfo yangModuleInfo : topLevelModels) {
            LOG.info("{} {} {}", yangModuleInfo.getNamespace(), yangModuleInfo.getName(), yangModuleInfo.getRevision());
            printDependencies(yangModuleInfo.getImportedModules(), prefixLength + PREFIX);
        }
        LOG.info("# top-level models list: {}", topLevelModels.size());
        for (YangModuleInfo yangModuleInfo : topLevelModels) {
            LOG.info("{} {} {}", yangModuleInfo.getNamespace(), yangModuleInfo.getName(), yangModuleInfo.getRevision());
        }
        Set<YangModuleInfo> uniqueModels = YangModuleUtils.filterUniqueModels(allModelsFromClasspath);
        LOG.info("# unique models list   : {}", uniqueModels.size());
        for (YangModuleInfo yangModuleInfo : uniqueModels) {
            LOG.info("{} {} {}", yangModuleInfo.getNamespace(), yangModuleInfo.getName(), yangModuleInfo.getRevision());
        }
    }

    public static void printConfiguration(Set<YangModuleInfo> allModelsFromClasspath) {
        Set<YangModuleInfo> topLevelModels = YangModuleUtils.filterTopLevelModels(allModelsFromClasspath);
        LOG.info("# top-level models list: {}", topLevelModels.size());
        for (YangModuleInfo yangModuleInfo : topLevelModels) {
            LOG.info("{ \"nameSpace\": \"{}\", \"name\": \"{}\", \"revision\": \"{}\" },",
                    yangModuleInfo.getNamespace(), yangModuleInfo.getName(), yangModuleInfo.getRevision());
        }
        LOG.info("# top-level models list: {}", topLevelModels.size());
        for (YangModuleInfo yangModuleInfo : topLevelModels) {
            LOG.info("{}.getInstance(),", yangModuleInfo.getClass().getCanonicalName());
        }
    }

    @SuppressFBWarnings(value = "SLF4J_SIGN_ONLY_FORMAT", justification = "Special log formatting.")
    private static void printDependencies(Set<YangModuleInfo> yangModuleInfos, int prefixLength) {
        for (YangModuleInfo yangModuleInfo : yangModuleInfos) {
            LOG.info("{}{} {} {}", createPrefixSpaces(prefixLength), yangModuleInfo.getNamespace(),
                    yangModuleInfo.getName(), yangModuleInfo.getRevision());
            printDependencies(yangModuleInfo.getImportedModules(), prefixLength + PREFIX);
        }
    }

    private static String createPrefixSpaces(int prefixLength) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < prefixLength; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
