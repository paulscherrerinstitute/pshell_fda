package ch.psi.fda.cdump;

import java.io.File;

import com.google.common.eventbus.EventBus;

import ch.psi.fda.EContainer;
import ch.psi.fda.messages.EndOfStreamMessage;
import ch.psi.fda.serializer.SerializerTXT;
import ch.psi.jcae.ChannelService;

public class CdumpEContainer implements EContainer {

	private final ChannelService cservice;
	private final CdumpEDescriptor edescriptor;
	private final EventBus eventbus;

	private Cdump cdump;
	private SerializerTXT serializer;

	private volatile boolean running = false;
	
	public CdumpEContainer(ChannelService cservice, EventBus eventbus, CdumpEDescriptor edescriptor) {
		this.cservice = cservice;
		this.eventbus = eventbus;
		this.edescriptor = edescriptor;
	}

	@Override
	public void initialize() {
		cdump = new Cdump(cservice, eventbus, new CdumpConfiguration());

		File file = new File(edescriptor.getFileName());
		file.getParentFile().mkdirs(); // Create data base directory
		
		serializer = new SerializerTXT(file);
		serializer.setShowDimensionHeader(false);

		eventbus.register(serializer);
	}

	@Override
	public void execute() {
		running = true;
		try{
			cdump.acquire(edescriptor.getSamplingRate());
			cdump.waitAcquireDone();
		}
		finally{
			running=false;
		}
	}

	@Override
	public void abort() {
		eventbus.post(new EndOfStreamMessage());
		cdump.stop();
		running = false;
		
		eventbus.unregister(serializer);
	}

	@Override
	public boolean isActive() {
		return running;
	}

	@Override
	public void destroy() {
		abort();
	}

}
