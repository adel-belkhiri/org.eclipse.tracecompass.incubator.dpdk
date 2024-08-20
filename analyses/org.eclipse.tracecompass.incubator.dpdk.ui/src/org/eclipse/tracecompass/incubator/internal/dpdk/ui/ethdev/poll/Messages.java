/*******************************************************************************
 * Copyright (c) 2016 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.tracecompass.incubator.internal.dpdk.ui.ethdev.poll;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.osgi.util.NLS;

/**
 * Translatable strings for the DPDK Ethernet Poll View
 *
 * @author Adel Belkhiri
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.incubator.internal.dpdk.ui.ethdev.poll.messages"; //$NON-NLS-1$
    /** Title of */
    public static @Nullable String RxPollDensityViewer_Title;
    /** X axis caption */
    public static @Nullable String RxPollDensityViewer_XAxisLabel;
    public static @Nullable String RxPollLatencyScatterGraphViewer_YAxisLabel;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
