package net.floodlightcontroller.MACTracker;

import java.util.*;

import net.floodlightcontroller.core.types.SwitchMACMessagePair;
import net.floodlightcontroller.restserver.IRestApiService;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFType;
import net.floodlightcontroller.core.IFloodlightProviderService;

import java.util.concurrent.ConcurrentSkipListSet;

import net.floodlightcontroller.packet.Ethernet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;

public class MACTracker implements IOFMessageListener, IFloodlightModule,IMACTrackerService {
	
	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;
	protected Set<Long> macAddresses;
	protected ConcurrentCircularBuffer<SwitchMACMessagePair> buffer;
	protected IRestApiService restApi;


	@Override
	public String getName() {
		return "MACTracker";
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		//Hacemos que el modulo forwarding se ejecute antes que nuestro modulo
		return (type.equals(OFType.PACKET_IN) && name.equals("forwarding"));
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IMACTrackerService.class);
		return l;
	}
	
	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		Map<Class<? extends IFloodlightService>, IFloodlightService> m = new HashMap<Class<? extends IFloodlightService>, IFloodlightService>();
		m.put(IMACTrackerService.class, this);
		return m;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		l.add(IRestApiService.class);
	    return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
	    macAddresses = new ConcurrentSkipListSet<Long>();
	    logger = LoggerFactory.getLogger(MACTracker.class);
		buffer = new ConcurrentCircularBuffer<SwitchMACMessagePair>(SwitchMACMessagePair.class,100);
		restApi = context.getServiceImpl(IRestApiService.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		restApi.addRestletRoutable(new MACTrackerRoutable());

	}

	@Override
	public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {

		switch (msg.getType()){
			case PACKET_IN:
				Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
				Long sourceMACHash = eth.getSourceMACAddress().getLong();
				if (!macAddresses.contains(sourceMACHash)) {
					macAddresses.add(sourceMACHash);
					logger.info("New MAC Address: {} seen on switch: {}",
							eth.getSourceMACAddress().toString(),
							sw.getId().toString());

					buffer.add(new SwitchMACMessagePair(sw,msg,sourceMACHash));
				}

				break;

			default:

				break;
		}

		return Command.CONTINUE;
	}

	@Override
	public ConcurrentCircularBuffer<SwitchMACMessagePair> getBuffer() {
		return buffer;
	}
}
