/******************************************************************************
 * Copyright (c) 2015, 2016 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.tracecompass.incubator.internal.dpdk.ui.ethdev.poll;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tracecompass.analysis.timing.ui.views.segmentstore.density2.AbstractSegmentStoreDensityView;
import org.eclipse.tracecompass.analysis.timing.ui.views.segmentstore.density2.AbstractSegmentStoreDensityViewer;
import org.eclipse.tracecompass.analysis.timing.ui.views.segmentstore.table.AbstractSegmentStoreTableViewer;
import org.eclipse.tracecompass.analysis.timing.ui.views.segmentstore.table.SegmentStoreTableViewer;
import org.eclipse.tracecompass.incubator.internal.dpdk.core.ethdev.poll.analysis.EtherPollAnalysis;

/**
 * Polls Density view
 *
 * @author Adel Belkhiri
 */
public class EtherPollDensityView extends AbstractSegmentStoreDensityView {

    /** The view's ID */
    public static final String ID = "org.eclipse.tracecompass.incubator.dpdk.ethdev.poll.density.view"; //$NON-NLS-1$

    /**
     * Constructs a new density view.
     */
    public EtherPollDensityView() {
        super(ID);
    }

    @Override
    protected AbstractSegmentStoreTableViewer createSegmentStoreTableViewer(Composite parent) {
        return new SegmentStoreTableViewer(new TableViewer(parent, SWT.FULL_SELECTION | SWT.VIRTUAL), EtherPollAnalysis.ID, false) {
            @Override
            protected void createProviderColumns() {
                super.createProviderColumns();
                Table t = (Table) getControl();
                TableColumn column = t.getColumn(2);
                column.setText("Number of Packets"); //$NON-NLS-1$
                column = t.getColumn(1);
                column.dispose();
                t.setColumnOrder(new int[] { 2, 3, 4, 0, 1});
            }
        };
    }

    @Override
    protected AbstractSegmentStoreDensityViewer createSegmentStoreDensityViewer(Composite parent) {
        return new EtherPollDensityViewer(parent);
    }
}
