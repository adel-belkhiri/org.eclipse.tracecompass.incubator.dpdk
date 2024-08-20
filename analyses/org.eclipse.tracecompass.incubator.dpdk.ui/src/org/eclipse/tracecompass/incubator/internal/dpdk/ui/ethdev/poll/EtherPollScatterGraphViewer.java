/*******************************************************************************
 * Copyright (c) 2021 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.tracecompass.incubator.internal.dpdk.ui.ethdev.poll;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.incubator.internal.dpdk.core.ethdev.poll.analysis.EtherPollAnalysis;
import org.eclipse.tracecompass.tmf.ui.viewers.xychart.linechart.TmfXYChartSettings;

/**
 * Displays the latency analysis data in a scatter graph
 *
 * @author Adel Belkhiri
 */
public class EtherPollScatterGraphViewer extends AbstractSegmentStoreScatterChartViewer3 {

    /**
     * Constructor
     *
     * @param parent
     *            parent composite
     * @param title
     *            name of the graph
     * @param xLabel
     *            name of the x axis
     * @param yLabel
     *            name of the y axis
     */
    public EtherPollScatterGraphViewer(Composite parent, String title, String xLabel, String yLabel) {
        super(parent, new TmfXYChartSettings(title, xLabel, yLabel, 1), EtherPollAnalysis.ID);
    }

}