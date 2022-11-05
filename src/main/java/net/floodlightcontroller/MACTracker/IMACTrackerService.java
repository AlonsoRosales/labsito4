package net.floodlightcontroller.MACTracker;

import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.SwitchMACMessagePair;

public interface IMACTrackerService extends IFloodlightService {
    public ConcurrentCircularBuffer<SwitchMACMessagePair> getBuffer();
}
