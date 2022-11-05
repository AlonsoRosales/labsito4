package net.floodlightcontroller.MACTracker;

import net.floodlightcontroller.core.types.SwitchMACMessagePair;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;

public class MACTrackerResource extends ServerResource {

    @Get("json")
    public List<SwitchMACMessagePair> retrieve() {
        IMACTrackerService pihr = (IMACTrackerService)getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
        List<SwitchMACMessagePair> l = new ArrayList<SwitchMACMessagePair>();
        l.addAll(java.util.Arrays.asList(pihr.getBuffer().snapshot()));
        return l;
    }
}
