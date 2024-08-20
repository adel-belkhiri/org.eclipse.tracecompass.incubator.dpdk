package org.eclipse.tracecompass.incubator.internal.dpdk.core.ethdev.poll.analysis;

import org.eclipse.tracecompass.datastore.core.interval.IHTIntervalReader;
import org.eclipse.tracecompass.datastore.core.serialization.ISafeByteBufferWriter;
import org.eclipse.tracecompass.datastore.core.serialization.SafeByteBufferFactory;
import org.eclipse.tracecompass.segmentstore.core.ISegment;
import org.eclipse.tracecompass.segmentstore.core.segment.interfaces.INamedSegment;
import org.eclipse.tracecompass.tmf.core.model.ICoreElementResolver;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * An Ethernet Poll to a NIC queue, created from a call to the DPDK rte_eth_rx_burst() function,
 * implemented as an {@link ISegment}.
 *
 * @author Adel Belkhiri
 */

public final class EtherPollModel  implements INamedSegment, ICoreElementResolver{

    private final String fThreadName;
    private final int fCpuId;
    private final long fStartTime;
    private final int fPortID;
    private final int fQueueID;
    private final int fNbPollPkts;

    private static final long serialVersionUID = 2589494342105208730L;

    /**
     * The reader for this segment class
     */
    public static final IHTIntervalReader<ISegment> READER = buffer -> new EtherPollModel(buffer.getString(), buffer.getInt(), buffer.getLong(), buffer.getInt(), buffer.getInt(), buffer.getInt());

    /**
     * Constructor
     *
     * @param threadName
     *          Name of the thread polling the Nic
     * @param cpuId
     *          CPU identifier
     * @param startTime
     *          When the poll began
     * @param tid
     *          Thread identifier
     * @param portID
     *          Port identifier
     * @param queueID
     *          Queue identifier
     * @param nbPollPkts
     *          Number of packets retrieved in a single poll
     */
    public EtherPollModel(String threadName, int cpuId, long startTime, int portID, int queueID, int nbPollPkts) {
        super();
        fThreadName = threadName;
        fCpuId = cpuId;
        fStartTime = startTime;
        fPortID = portID;
        fQueueID = queueID;
        this.fNbPollPkts = nbPollPkts;
    }

    @Override
    public int getSizeOnDisk() {
        return SafeByteBufferFactory.getStringSizeInBuffer(fThreadName) + Integer.BYTES + Long.BYTES + 3 * Integer.BYTES;
    }

    @Override
    public void writeSegment(ISafeByteBufferWriter buffer) {
        buffer.putString(fThreadName);
        buffer.putInt(fCpuId);
        buffer.putLong(fStartTime);
        buffer.putInt(fPortID);
        buffer.putInt(fQueueID);
        buffer.putInt(fNbPollPkts);
    }

    @Override
    public long getStart() {
        return fStartTime;
    }

    @Override
    public long getLength() {
        return fNbPollPkts;
    }

    @Override
    public long getEnd() {
        return fStartTime;
    }

    /**
     * Get the name of the polling thread
     *
     * @return Name
     */
    public String getThreadName() {
        return fThreadName;
    }

    /**
     * Get the name of the polled port queue
     *
     * @return Name
     */
    @Override
    public String getName() {
        return "P" + fPortID + " / Q" + fQueueID; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Get the thread ID for this poll
     *
     * @return The ID of the thread
     */
    public int getCpuId() {
        return fCpuId;
    }

    /**
     * Get the port ID that initiated this poll
     *
     * @return The return value of this syscall
     */
    public int getPortID() {
        return fPortID;
    }

    /**
     * Get the queue ID of the port that initiated this poll
     *
     * @return The return value of this syscall
     */
    public int getQueueID() {
        return fQueueID;
    }

    @Override
    public int compareTo(ISegment o) {
        int ret = INamedSegment.super.compareTo(o);
        if (ret != 0) {
            return ret;
        }
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return "Start Time = " + getStart() + //$NON-NLS-1$
                "; Number of packets = " + getLength() + //$NON-NLS-1$
                "; Device = " + getName(); //$NON-NLS-1$
    }

    @Override
    public Multimap<String, Object> getMetadata() {
        Multimap<String, Object> map = HashMultimap.create();
        map.put(Messages.getMessage(Messages.SegmentAspectName_CpuId), fCpuId);
        return map;
    }
}
