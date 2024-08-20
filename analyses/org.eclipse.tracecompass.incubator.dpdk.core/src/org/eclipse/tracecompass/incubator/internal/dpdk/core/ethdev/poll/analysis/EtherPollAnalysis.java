package org.eclipse.tracecompass.incubator.internal.dpdk.core.ethdev.poll.analysis;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.timing.core.segmentstore.AbstractSegmentStoreAnalysisEventBasedModule;
import org.eclipse.tracecompass.datastore.core.interval.IHTIntervalReader;
import org.eclipse.tracecompass.incubator.internal.dpdk.core.analysis.DpdkEthdevEventLayout;
import org.eclipse.tracecompass.segmentstore.core.ISegment;
import org.eclipse.tracecompass.segmentstore.core.ISegmentStore;
import org.eclipse.tracecompass.segmentstore.core.SegmentComparators;
import org.eclipse.tracecompass.segmentstore.core.SegmentStoreFactory.SegmentStoreType;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.segment.ISegmentAspect;
import com.google.common.collect.ImmutableList;
import static org.eclipse.tracecompass.common.core.NonNullUtils.nullToEmptyString;

/**
 * The {@link EtherPollAnalysis} class calculates statistics related to the polling of NIC ports by a DPDK application.
 * These statistics include the number of packets retrieved from a single invocation of the eth_rx_burst() function.
 *
 * @author Adel Belkhiri
 */
public class EtherPollAnalysis extends AbstractSegmentStoreAnalysisEventBasedModule {


        /**
         * The ID of this analysis
         */
        public static final String ID = "org.eclipse.tracecompass.incubator.internal.dpdk.core.ethdev.poll.analysis"; //$NON-NLS-1$
        private static final int VERSION = 2;

        private static final Collection<ISegmentAspect> BASE_ASPECTS = ImmutableList.of(PollThreadNameAspect.INSTANCE, PollCpuIdAspect.INSTANCE, DeviceNameAspect.INSTANCE);

        /**
         * Constructor
         */
        public EtherPollAnalysis() {
            // do nothing
        }

        @Override
        public String getId() {
            return ID;
        }

        @Override
        public Iterable<ISegmentAspect> getSegmentAspects() {
            return BASE_ASPECTS;
        }

        @Override
        protected int getVersion() {
            return VERSION;
        }

        @Override
        protected @NonNull SegmentStoreType getSegmentStoreType() {
            return SegmentStoreType.OnDisk;
        }

        @Override
        protected AbstractSegmentStoreAnalysisRequest createAnalysisRequest(ISegmentStore<ISegment> pollStore, IProgressMonitor monitor) {
            return new EtherPollAnalysisRequest(pollStore, monitor);
        }

        @Override
        protected @NonNull IHTIntervalReader<ISegment> getSegmentReader() {
            return EtherPollModel.READER;
        }

        /**
         * class EtherPollAnalysisRequest
         */
        private class EtherPollAnalysisRequest extends AbstractSegmentStoreAnalysisRequest {
            private final IProgressMonitor fMonitor;

            public EtherPollAnalysisRequest(ISegmentStore<ISegment> pollStore, IProgressMonitor monitor) {
                super(pollStore);
                fMonitor = monitor;
            }

            @Override
            public void handleData(final ITmfEvent event) {
                super.handleData(event);
                final String eventName = event.getName();

                if (eventName.equals(DpdkEthdevEventLayout.eventEthdevRxqBurst())) {
                    Integer nbRxPkts = event.getContent().getFieldValue(Integer.class, DpdkEthdevEventLayout.fieldNbRxPkts());
                    if(Objects.requireNonNull(nbRxPkts) > 0) {
                        long startTime = event.getTimestamp().toNanos();
                        String threadName = event.getContent().getFieldValue(String.class, DpdkEthdevEventLayout.fieldThreadName());
                        Integer cpuId = event.getContent().getFieldValue(Integer.class, DpdkEthdevEventLayout.fieldCpuId());
                        Integer portId = event.getContent().getFieldValue(Integer.class, DpdkEthdevEventLayout.fieldPortId());
                        Integer queueId = event.getContent().getFieldValue(Integer.class, DpdkEthdevEventLayout.fieldQueueId());

                        EtherPollModel poll = new EtherPollModel(threadName, Objects.requireNonNull(cpuId), startTime, Objects.requireNonNull(portId), Objects.requireNonNull(queueId), Objects.requireNonNull(nbRxPkts));
                        getSegmentStore().add(poll);
                    }
                }
            }

            @Override
            public void handleCompleted() {
                super.handleCompleted();
            }

            @Override
            public void handleCancel() {
                fMonitor.setCanceled(true);
                super.handleCancel();
            }
        }

        /**
        *
        * Aspect Classes
        *
        */
        private static final class DeviceNameAspect implements ISegmentAspect {
            @NonNull public static final ISegmentAspect INSTANCE = new DeviceNameAspect();

            private DeviceNameAspect() {
                // Do nothing
            }

            @Override
            public String getHelpText() {
                return nullToEmptyString(Messages.SegmentAspectHelpText_Device);
            }

            @Override
            public String getName() {
                return nullToEmptyString(Messages.SegmentAspectName_Device);
            }

            @Override
            public @Nullable Comparator<?> getComparator() {
                return (ISegment segment1, ISegment segment2) -> {
                    if (segment1 == null) {
                        return 1;
                    }
                    if (segment2 == null) {
                        return -1;
                    }
                    if (segment1 instanceof EtherPollModel && segment2 instanceof EtherPollModel) {
                        int res = ((EtherPollModel) segment1).getName().compareTo(((EtherPollModel) segment2).getName());
                        return (res != 0 ? res : SegmentComparators.INTERVAL_START_COMPARATOR.thenComparing(SegmentComparators.INTERVAL_END_COMPARATOR).compare(segment1, segment2)); //TODO: check end end comparator
                    }
                    return 1;
                };
            }

            @Override
            public @Nullable String resolve(ISegment segment) {
                if (segment instanceof EtherPollModel) {
                    return ((EtherPollModel) segment).getName();
                }
                return EMPTY_STRING;
            }
        }


        private static final class PollCpuIdAspect implements ISegmentAspect {
            @NonNull public static final ISegmentAspect INSTANCE = new PollCpuIdAspect();

            private PollCpuIdAspect() {
                // Do nothing
            }

            @Override
            public String getHelpText() {
                return nullToEmptyString(Messages.SegmentAspectHelpText_CpuId);
            }

            @Override
            public String getName() {
                return nullToEmptyString(Messages.SegmentAspectName_CpuId);
            }

            @Override
            public @Nullable Comparator<?> getComparator() {
                return (ISegment segment1, ISegment segment2) -> {
                    if (segment1 == null) {
                        return 1;
                    }
                    if (segment2 == null) {
                        return -1;
                    }
                    if (segment1 instanceof EtherPollModel && segment2 instanceof EtherPollModel) {
                        int res = Integer.compare(((EtherPollModel) segment1).getCpuId(), ((EtherPollModel) segment2).getCpuId());
                        return (res != 0 ? res : SegmentComparators.INTERVAL_START_COMPARATOR.thenComparing(SegmentComparators.INTERVAL_END_COMPARATOR).compare(segment1, segment2));
                    }
                    return 1;
                };
            }

            @Override
            public @Nullable Integer resolve(ISegment segment) {
                if (segment instanceof EtherPollModel) {
                    return ((EtherPollModel) segment).getCpuId();
                }
                return -1;
            }
        }


        private static final class PollThreadNameAspect implements ISegmentAspect {
            @NonNull public static final ISegmentAspect INSTANCE = new PollThreadNameAspect();

            private PollThreadNameAspect() {
                // Do nothing
            }

            @Override
            public String getHelpText() {
                return nullToEmptyString(Messages.SegmentAspectHelpText_ThreadName);
            }

            @Override
            public String getName() {
                return nullToEmptyString(Messages.SegmentAspectName_ThreadName);
            }

            @Override
            public @Nullable Comparator<?> getComparator() {
                return (ISegment segment1, ISegment segment2) -> {
                    if (segment1 == null) {
                        return 1;
                    }
                    if (segment2 == null) {
                        return -1;
                    }
                    if (segment1 instanceof EtherPollModel && segment2 instanceof EtherPollModel) {
                        int res = Integer.compare(((EtherPollModel) segment1).getCpuId(), ((EtherPollModel) segment2).getCpuId());
                        return (res != 0 ? res : SegmentComparators.INTERVAL_START_COMPARATOR.thenComparing(SegmentComparators.INTERVAL_END_COMPARATOR).compare(segment1, segment2));
                    }
                    return 1;
                };
            }

            @Override
            public @Nullable Object resolve(ISegment segment) {
                if (segment instanceof EtherPollModel) {
                    return ((EtherPollModel) segment).getThreadName();
                }
                return EMPTY_STRING;
            }
        }
    }
