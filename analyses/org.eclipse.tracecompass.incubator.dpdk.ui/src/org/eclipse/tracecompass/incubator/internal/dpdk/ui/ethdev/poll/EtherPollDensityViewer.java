/******************************************************************************
 * Copyright (c) 2015 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.tracecompass.incubator.internal.dpdk.ui.ethdev.poll;

import static org.eclipse.tracecompass.common.core.NonNullUtils.nullToEmptyString;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.analysis.timing.core.segmentstore.ISegmentStoreProvider;
import org.eclipse.tracecompass.analysis.timing.ui.views.segmentstore.density2.AbstractSegmentStoreDensityViewer;
import org.eclipse.tracecompass.common.core.format.DecimalUnitFormat;
import org.eclipse.tracecompass.incubator.internal.dpdk.core.ethdev.poll.analysis.EtherPollAnalysis;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;

/**
 * RxPolls density viewer
 *
 * @author Adel Belkhiri
 */
public class EtherPollDensityViewer extends AbstractSegmentStoreDensityViewer {

    /**
     * Constructs a new density viewer.
     *
     * @param parent
     *            the parent of the viewer
     */
    public EtherPollDensityViewer(Composite parent) {
        super(parent, new DecimalUnitFormat());
        getControl().getAxisSet().getXAxis(0).getTitle().setText(nullToEmptyString(Messages.RxPollDensityViewer_XAxisLabel));
    }

    @Override
    protected @Nullable ISegmentStoreProvider getSegmentStoreProvider(ITmfTrace trace) {
        return TmfTraceUtils.getAnalysisModuleOfClass(trace, EtherPollAnalysis.class, EtherPollAnalysis.ID);
    }
}
