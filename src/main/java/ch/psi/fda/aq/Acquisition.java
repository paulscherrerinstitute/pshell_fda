/**
 * 
 * Copyright 2010 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of
 * merchantability or fitness for a particular purpose. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.psi.fda.aq;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import ch.psi.fda.core.ActionLoop;
import ch.psi.fda.core.Actor;
import ch.psi.fda.core.Manipulation;
import ch.psi.fda.core.Sensor;
import ch.psi.fda.core.actions.ChannelAccessCondition;
import ch.psi.fda.core.actions.ChannelAccessPut;
import ch.psi.fda.core.actions.Delay;
import ch.psi.fda.core.actors.ChannelAccessFunctionActuator;
import ch.psi.fda.core.actors.ChannelAccessLinearActuator;
import ch.psi.fda.core.actors.ChannelAccessTableActuator;
import ch.psi.fda.core.actors.ComplexActuator;
import ch.psi.fda.core.actors.JythonFunction;
import ch.psi.fda.core.actors.PseudoActuatorSensor;
import ch.psi.fda.core.guard.ChannelAccessGuard;
import ch.psi.fda.core.guard.ChannelAccessGuardCondition;
import ch.psi.fda.core.loops.ActorSensorLoop;
import ch.psi.fda.core.loops.cr.CrlogicLoopStream;
import ch.psi.fda.core.loops.cr.CrlogicResource;
import ch.psi.fda.core.loops.cr.ParallelCrlogic;
import ch.psi.fda.core.loops.cr.ScrlogicLoop;
import ch.psi.fda.core.manipulator.JythonManipulation;
import ch.psi.fda.core.scripting.JythonGlobalVariable;
import ch.psi.fda.core.scripting.JythonParameterMapping;
import ch.psi.fda.core.scripting.JythonParameterMappingChannel;
import ch.psi.fda.core.scripting.JythonParameterMappingGlobalVariable;
import ch.psi.fda.core.scripting.JythonParameterMappingID;
import ch.psi.fda.core.sensors.ChannelAccessSensor;
import ch.psi.fda.core.sensors.TimestampSensor;
import ch.psi.fda.model.ModelManager;
import ch.psi.fda.model.v1.Action;
import ch.psi.fda.model.v1.ArrayDetector;
import ch.psi.fda.model.v1.ArrayPositioner;
import ch.psi.fda.model.v1.ChannelAction;
import ch.psi.fda.model.v1.ChannelParameterMapping;
import ch.psi.fda.model.v1.Configuration;
import ch.psi.fda.model.v1.ContinuousDimension;
import ch.psi.fda.model.v1.ContinuousPositioner;
import ch.psi.fda.model.v1.Detector;
import ch.psi.fda.model.v1.DetectorOfDetectors;
import ch.psi.fda.model.v1.DiscreteStepDimension;
import ch.psi.fda.model.v1.DiscreteStepPositioner;
import ch.psi.fda.model.v1.Function;
import ch.psi.fda.model.v1.FunctionPositioner;
import ch.psi.fda.model.v1.Guard;
import ch.psi.fda.model.v1.GuardCondition;
import ch.psi.fda.model.v1.IDParameterMapping;
import ch.psi.fda.model.v1.LinearPositioner;
import ch.psi.fda.model.v1.ParameterMapping;
import ch.psi.fda.model.v1.Positioner;
import ch.psi.fda.model.v1.PseudoPositioner;
import ch.psi.fda.model.v1.Recipient;
import ch.psi.fda.model.v1.Region;
import ch.psi.fda.model.v1.RegionPositioner;
import ch.psi.fda.model.v1.ScalarDetector;
import ch.psi.fda.model.v1.ScalerChannel;
import ch.psi.fda.model.v1.Scan;
import ch.psi.fda.model.v1.ScriptAction;
import ch.psi.fda.model.v1.ScriptManipulation;
import ch.psi.fda.model.v1.ShellAction;
import ch.psi.fda.model.v1.SimpleScalarDetector;
import ch.psi.fda.model.v1.Timestamp;
import ch.psi.fda.model.v1.Variable;
import ch.psi.fda.model.v1.VariableParameterMapping;
import ch.psi.fda.serializer.SerializerTXT;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import ch.psi.jcae.Channel;
import ch.psi.jcae.ChannelDescriptor;
import ch.psi.jcae.ChannelException;
import ch.psi.jcae.ChannelService;
import ch.psi.jcae.impl.type.DoubleTimestamp;
import ch.psi.jcae.util.ComparatorAND;
import ch.psi.jcae.util.ComparatorOR;
import ch.psi.jcae.util.ComparatorREGEX;
import ch.psi.pshell.core.Context;
import ch.psi.pshell.core.ExecutionParameters;
import ch.psi.pshell.core.Setup;
import ch.psi.utils.Str;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Data acquisition engine for performing scans
 * Mapping is specific to scan model version 1.0
 */
public class Acquisition {
	
	private static Logger logger = Logger.getLogger(Acquisition.class.getName());

	private final AcquisitionConfiguration configuration;
	
	private ActionLoop actionLoop;
	private Manipulator manipulator;
	private SerializerTXT serializer;
	
	private List<Manipulation> manipulations;
	private volatile boolean active = false;
	
	private NotificationAgent notificationAgent;
	
	private Handler logHandler = null;
	
	private File datafile;
	
	
	private ChannelService cservice;
	private List<Channel<?>> channels = new ArrayList<>();
	private List<Object> templates = new ArrayList<>();
	
	
	private Configuration configModel;
	
	private HashMap<String, JythonGlobalVariable> jVariableDictionary = new HashMap<String, JythonGlobalVariable>();
        
        private final Map<String, Object> vars;
	
	public Acquisition(ChannelService cservice, AcquisitionConfiguration configuration, Map<String, Object> vars){
		this.cservice = cservice;
		this.configuration = configuration;
		this.actionLoop = null;
		this.manipulations = new ArrayList<Manipulation>();
                this.vars = vars;
	}
        
        ExecutionParameters executionParameters = null;
	
	
	
	/**
	 * Get state of the acquisition engine
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}



	/**
	 * Acquire data
	 * 
	 * @param smodel		Model of the scan
	 * @param getQueue	Flag whether to return a queue or not. If false the return value of the function will be null.
	 * @throws InterruptedException
	 */
	public void initalize(EventBus bus, Configuration smodel){            

		// Create notification agent with globally configured recipients
		notificationAgent = new NotificationAgent(configuration.getSmptServer(), "fda.notification@psi.ch");
		
		// Update recipients list of the Notifiaction Agent
		if(smodel.getNotification()!=null){
			for(Recipient r: smodel.getNotification().getRecipient()){
				notificationAgent.getRecipients().add(r);
			}
		}
		
		
		Date date = new Date();
		// Prepare output directory / create directory if it does not exist
                //File bdir = new File(configuration.replaceMacros(configuration.getDataBaseDirectory(), date, smodel.getData().getFileName()));
                String dir = configuration.getDataBaseDirectory();
                //Priority to Type field
                String fprefix = configuration.getDataFilePrefix();
                if ((smodel.getData().getFileName()!=null) && (!smodel.getData().getFileName().trim().isEmpty())){
                    dir = dir.replace(Setup.TOKEN_EXEC_NAME, smodel.getData().getFileName()); 
                    fprefix = fprefix.replace(Setup.TOKEN_EXEC_NAME, smodel.getData().getFileName()); 
                }
                File bdir = new File(Context.getInstance().getSetup().expandPath(dir));
                
//		bdir.mkdirs();
		//String fprefix = configuration.replaceMacros(configuration.getDataFilePrefix(), date, smodel.getData().getFileName());
                fprefix = Context.getInstance().getSetup().expandPath(fprefix);

		// Construct filenames
		///File xmlfile = new File(bdir, fprefix+smodel.getData().getFileName()+".xml");
		//datafile = new File(bdir, fprefix+smodel.getData().getFileName()+".txt");
		File xmlfile = new File(fprefix+".xml");
                datafile = new File(fprefix+".txt");
                
		// Create required directories
                executionParameters = Context.getInstance().getExecutionPars();
                executionParameters.setDataPath(xmlfile.getParentFile()); //Create base dir and trigger callbacks
                		
		try{
			// Workaround - to avoid that multiple handlers get attached
			// this should be removed when rewriting the acquisition logic
			if(logHandler!=null){
				Logger.getLogger("").removeHandler(logHandler);
			}
			
			//File logfile = new File(bdir, fprefix+smodel.getData().getFileName()+".log");
                        File logfile = new File(fprefix+".log");
			logHandler = new FileHandler(logfile.getAbsolutePath());
			logHandler.setFormatter(new SimpleFormatter());
		    Logger.getLogger("").addHandler(logHandler);
		    
                        //Logger.getLogger("ch.psi.fda").setLevel(Level.FINEST);
		    // Also include log messages of the channel access implementation
		    //Logger.getLogger("com.cosylab.epics.caj").setLevel(Level.INFO);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		// Save a copy of the model to the data directory
		try {
			ModelManager.marshall(smodel, xmlfile);
                        if (Context.getInstance()!=null){
                            Context.getInstance().addDetachedFileToSession(xmlfile);
                        }
		} catch (Exception e) {
			throw new RuntimeException("Unable to serialize scan",e);
		}		

                if (vars!=null){
                    for (String var : vars.keySet()){
                        String id = var;
                        try{
                            //String setter = null;
                            Object value = vars.get(id);
                            if (id.contains(".")){                                
                                //int index = id.lastIndexOf(".");
                                //setter = "set" + Str.capitalizeFirst(id.substring(index + 1));
                                //id = id.substring(0, index);                                
                                String[] tokens = id.split("\\.");
                                if (tokens.length >3){
                                   throw new Exception();
                                }
                                id = tokens[0];
                                Object obj = ModelUtil.getInstance().getObject(id);
                                if (tokens.length == 3){
                                    int index = -1;
                                    String getter = "get" + Str.capitalizeFirst(tokens[1]); 
                                    if (getter.contains("[") && getter.endsWith("]")){
                                        int aux = getter.indexOf("[");
                                        index = Integer.valueOf(getter.substring(aux+1,getter.length()-1 ));
                                        getter = getter.substring(0, aux);                                        
                                    } 
                                    for (Method m: obj.getClass().getMethods()){
                                        if (m.getName().equals(getter)){
                                            obj = m.invoke(obj, new Object[]{});
                                            if (index>=0){
                                                if (obj instanceof List){
                                                    obj = ((List)obj).get(index);
                                                } else {
                                                    obj = Array.get(obj, index);
                                                }
                                            }
                                            break;
                                        }
                                    }                                                                        
                                }                                
                                String setter = "set" + Str.capitalizeFirst(tokens[tokens.length - 1]); 
                                for (Method m: obj.getClass().getMethods()){
                                    if (m.getName().equals(setter)){
                                        m.invoke(obj, new Object[]{value});
                                        break;
                                    }
                                }
                            } else {
                                Object obj = ModelUtil.getInstance().getObject(id);
                                if (obj instanceof Variable){
                                    ((Variable)obj).setValue((Double) value);
                                }
                            } 
                        } catch (Exception ex){
                            throw new RuntimeException("Bad parameter value: " + var);
                        }
                    }
                }
                
                
		logger.fine("Map Model to internal logic");

                if(smodel.getScan().getManipulation()!= null && smodel.getScan().getManipulation().size()>0){      
			// Setup optimized with manipulations
                        //EventBus b = new AsyncEventBus(Executors.newCachedThreadPool());
                        
                        //TODO: Originally it was FixedThreadPool, but it makes the number of threads increase continuously.
                        //EventBus b = new AsyncEventBus(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));                        
                        EventBus b = new AsyncEventBus(Executors.newSingleThreadExecutor());                        
			// Map scan to base model
			// After this call actionLoop and collector will be initialized
			Collector collector = new Collector(b);
			mapScan(collector, smodel);
//			col = collector;
			logger.fine("ActionLoop and Collector initialized");
	
			
	
			// Add manipulator into processing chain
			this.manipulator = new Manipulator(bus, this.manipulations);
			b.register(this.manipulator);						
                }
		else{
			// Setup optimized without manipulations
			Collector collector = new Collector(bus);
			mapScan(collector, smodel);
//			col = collector;
			
		}
                this.serializer = new SerializerTXT(datafile, configuration.getAppendSuffix());
                bus.register(serializer);
	}
	
	/**
	 * Execute acquisition
	 * @throws InterruptedException 
	 */
	public void execute() throws InterruptedException {
		String hostname;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hostname="unknown";
		}
		
		try{
			active = true;
	
			actionLoop.prepare();
			actionLoop.execute();
			actionLoop.cleanup();
	
			notificationAgent.sendNotification("Notification - FDA Execution Finished", "The execution of the FDA on '"+hostname+"' for file '"+datafile.getName()+"' finished successfully\n\nYou received this message because you are listed in the notification list for this data acquisition configuration.", false,true);
		}
		catch(InterruptedException e){
			logger.log(Level.INFO, "Execution interrupted: ", e);
			notificationAgent.sendNotification("Notification - FDA Execution was aborted", "The execution of the FDA on '"+hostname+"' for file '"+datafile.getName()+"' was aborted\n\nYou received this message because you are listed in the notification list for this data acquisition configuration.", false, true);
                        throw e;
		}
		catch(Exception e){
			logger.log(Level.WARNING, "Execution failed: ", e);
			notificationAgent.sendNotification("Notification - FDA Execution Failed", "The execution of the FDA failed on '"+hostname+"' for file '"+datafile.getName()+"'\n\nYou received this message because you are listed in the notification list for this data acquisition configuration.", true,false);
			throw e;
		}
		finally{
			active = false;
		}
	}
	

	public void destroy(){
		if(actionLoop != null){
			logger.finest("Destroy managed resources");
			
			for(Channel<?> c: channels){
				try {
					c.destroy();
				} catch (ChannelException e) {
					logger.severe("Unable to destroy channel "+c.getName() + ": " + e.getMessage());
				}
			}
			for(Object o: templates){
				try {
					cservice.destroyAnnotatedChannels(o);
				} catch (ChannelException e) {
					logger.severe("Unable to destroy channels of template "+o.getClass().getName() + ": " + e.getMessage());
				}
			}
			
		}
		
		// Clear global variables Jython
		jVariableDictionary.clear();
                
                executionParameters.setDataPath(null);
		
		// Remove log handler
		if(logHandler!=null){
			logger.fine("Close log handler");
			logHandler.close();
    			Logger.getLogger("").removeHandler(logHandler);
		}
	}

	public void abort(){
            if(actionLoop != null){
                try{
                    actionLoop.abort();
                }
                catch (Exception ex){
                    Logger.getLogger(ActorSensorLoop.class.getName()).log(Level.WARNING,null,ex);
                }                         		
            }
	}
	
	public String getDatafileName(){
		return(datafile.getName());
	}
	
	/**
	 * Retrieve id string of the passed object
	 * @param object
	 * @return	Id string of object
	 */
	private static String resolveIdRef(Object object){
		String id;
		if(object instanceof Positioner){
			id = ((Positioner)object).getId();
		}
		else if (object instanceof Detector){
			id = ((Detector)object).getId();
		}
		else if (object instanceof ch.psi.fda.model.v1.Manipulation){
			id = ((ch.psi.fda.model.v1.Manipulation)object).getId();
		}
		else{
			throw new RuntimeException("Unable to identify id of object reference "+object);
		}
		return id;
	}
	
	
	
	/**
	 * Map scan to base model
	 * @param scan
	 */
	private void mapScan(Collector collector, Configuration configuration){
		this.configModel = configuration;
		Scan scan = configuration.getScan();
		
		for(Variable v: configuration.getVariable()){
			JythonGlobalVariable var = new JythonGlobalVariable();
			var.setName(v.getName());
			var.setValue(v.getValue());
			jVariableDictionary.put(v.getName(), var);
			v.getValue();
		}
		
		// Map continuous dimension
		if(scan.getCdimension() != null){
			ActionLoop aLoop = mapContinuousDimension(scan.getCdimension());
			actionLoop = aLoop;
			collector.addEventBus(aLoop.getEventBus());
		}
		
		// Map discrete step dimensions
		for(DiscreteStepDimension d: scan.getDimension()){
			ActorSensorLoop l = mapDiscreteStepDimension(d);
			collector.addEventBus(l.getEventBus());
			if(actionLoop != null){
				l.getActionLoops().add(actionLoop);
			}
			actionLoop = l;
		}
		
		// No dimensions where specified for scan
		if(actionLoop == null){
			actionLoop = new ActorSensorLoop();
		}
		
		// Map pre actions to pre actions of the top level dimension
		actionLoop.getPreActions().addAll(mapActions(scan.getPreAction()));
		
		// Map post actions to post actions of the top level dimension
		actionLoop.getPostActions().addAll(mapActions(scan.getPostAction()));
		
		
		// TODO need to be removed! and done differently !!!!
		// Handle iterations by adding a pseudo dimension and setting the 
		// datagroup flag in the main loop
		if(configuration.getNumberOfExecution()>1){
			// Create Iterations pseudo loop
			ActorSensorLoop l = new ActorSensorLoop();
			PseudoActuatorSensor a = new PseudoActuatorSensor("iterations", configuration.getNumberOfExecution());
			l.getActors().add(a);
			l.getActionLoops().add(actionLoop);
			actionLoop.setDataGroup(true);  // Need to add setDataGroup to ActionLoop interface
			
			// Set toplevel action loop
			actionLoop = l;
			collector.addEventBus(l.getEventBus());
		}
		
		// handling manipulations
		for(ch.psi.fda.model.v1.Manipulation m : scan.getManipulation()){
			if(m instanceof ScriptManipulation){
				ScriptManipulation sm = (ScriptManipulation) m;
				
				List<JythonParameterMapping> mapping = new ArrayList<JythonParameterMapping>();
				for(ParameterMapping pm: sm.getMapping()){
					if(pm instanceof IDParameterMapping){
						String refid = resolveIdRef(((IDParameterMapping)pm).getRefid());
						mapping.add( new JythonParameterMappingID(pm.getVariable(), refid));
					}
					else if(pm instanceof ChannelParameterMapping){
						ChannelParameterMapping cpm = (ChannelParameterMapping) pm;
						if(cpm.getType().equals("String")){
							mapping.add( new JythonParameterMappingChannel<String>(cpm.getVariable(), createChannel(String.class, cpm.getChannel())));
						}
						else if(cpm.getType().equals("Integer")){
							mapping.add( new JythonParameterMappingChannel<Integer>(cpm.getVariable(), createChannel(Integer.class, cpm.getChannel())));
						}
						else if(cpm.getType().equals("Double")){
							mapping.add( new JythonParameterMappingChannel<Double>(cpm.getVariable(), createChannel(Double.class, cpm.getChannel())));
						}
						else{
							logger.warning("Channel type ["+cpm.getType()+"] is not supported for mapping");
						}
					}
					else if(pm instanceof VariableParameterMapping){
						VariableParameterMapping vp = (VariableParameterMapping) pm;
						Variable v = (Variable)vp.getName();
						JythonGlobalVariable var = jVariableDictionary.get(v.getName());
						var.setValue(v.getValue());
						mapping.add(new JythonParameterMappingGlobalVariable(vp.getVariable(), var));
					}
				}
				
				JythonManipulation manipulation = new JythonManipulation(sm.getId(), sm.getScript(), mapping, sm.isReturnArray());
				
				if(configuration.getData()!=null){ // Safety
					manipulation.setVariable("FILENAME", configuration.getData().getFileName());
					manipulation.setVariable("DATAFILE", datafile.getAbsoluteFile());
				}
				
				this.manipulations.add(manipulation);
			}
		}
	}
	
	/**
	 * Map a model action to base actions
	 * @param actions
	 * @return
	 */
	private List<ch.psi.fda.core.Action> mapActions(List<Action> actions){
		List<ch.psi.fda.core.Action> alist = new ArrayList<ch.psi.fda.core.Action>();
		for(Action a: actions){
			if(a instanceof ChannelAction){
				ChannelAction ca = (ChannelAction) a;
				
				String operation = ca.getOperation(); // Default = put
				String type=ca.getType(); // Default = String
				
				if(operation.equals("put")){
					Long timeout = null;
					if(ca.getTimeout()!=null){
						timeout = Math.round(ca.getTimeout()*1000);
					}
					if(type.equals("String")){
						alist.add(new ChannelAccessPut<String>(createChannel(String.class, ca.getChannel()), ca.getValue(), false, timeout));
					}
					else if(type.equals("Integer")){
						alist.add(new ChannelAccessPut<Integer>(createChannel(Integer.class, ca.getChannel()), new Integer(ca.getValue()), false, timeout));
					}
					else if(type.equals("Double")){
						alist.add(new ChannelAccessPut<Double>(createChannel(Double.class,ca.getChannel()), new Double(ca.getValue()), false, timeout));
					}
				}
				else if(operation.equals("putq")){
					if(type.equals("String")){
						alist.add(new ChannelAccessPut<String>(createChannel(String.class,ca.getChannel()), ca.getValue(), true, null));
					}
					else if(type.equals("Integer")){
						alist.add(new ChannelAccessPut<Integer>(createChannel(Integer.class,ca.getChannel()), new Integer(ca.getValue()), true, null));
					}
					else if(type.equals("Double")){
						alist.add(new ChannelAccessPut<Double>(createChannel(Double.class,ca.getChannel()), new Double(ca.getValue()), true, null));
					}
				}
				else if(operation.equals("wait")){
					Long timeout = null ; // Default timeout = wait forever
					if(ca.getTimeout()!=null){
						timeout = Math.round(ca.getTimeout()*1000);
					}
					if(type.equals("String")){
						alist.add(new ChannelAccessCondition<String>(createChannel(String.class,ca.getChannel()), ca.getValue(), timeout));
					}
					else if(type.equals("Integer")){
						alist.add(new ChannelAccessCondition<Integer>(createChannel(Integer.class,ca.getChannel()), new Integer(ca.getValue()), timeout));
					}
					else if(type.equals("Double")){
						alist.add(new ChannelAccessCondition<Double>(createChannel(Double.class,ca.getChannel()), new Double(ca.getValue()), timeout));
					}
				}
				else if(operation.equals("waitREGEX")){
					Long timeout = null ; // Default timeout = wait forever
					if(ca.getTimeout()!=null){
						timeout = Math.round(ca.getTimeout()*1000);
					}
					if(type.equals("String")){
						alist.add(new ChannelAccessCondition<>(createChannel(String.class, ca.getChannel()), ca.getValue(), new ComparatorREGEX(), timeout));
					}
					else{
						logger.warning("Operation "+operation+" wity type "+type+" for action is not supported");
					}
				}
				else if(operation.equals("waitOR")){
					Long timeout = null ; // Default timeout = wait forever
					if(ca.getTimeout()!=null){
						timeout = Math.round(ca.getTimeout()*1000);
					}
					
					if(type.equals("Integer")){
						alist.add(new ChannelAccessCondition<>(createChannel(Integer.class,ca.getChannel()), new Integer(ca.getValue()), new ComparatorOR(), timeout));
					}
					else{
						logger.warning("Operation "+operation+" wity type "+type+" for action is not supported");
					}
				}
				else if(operation.equals("waitAND")){
					Long timeout = null ; // Default timeout = wait forever
					if(ca.getTimeout()!=null){
						timeout = Math.round(ca.getTimeout()*1000);
					}
					if(type.equals("Integer")){
						alist.add(new ChannelAccessCondition<>(createChannel(Integer.class,ca.getChannel()), new Integer(ca.getValue()), new ComparatorAND(), timeout));
					}
					else {
						logger.warning("Operation "+operation+" wity type "+type+" for action is not supported");
					}
				}
				else{
					// Operation not supported
					logger.warning("Operation "+operation+" for action is not supported");
				}
				
				// Translate delay attribute to delay action
				if(ca.getDelay()!=null){
					Double x = ca.getDelay()*1000;
					alist.add(new Delay(x.longValue()));
				}
				
			}
			else if(a instanceof ShellAction){
				ShellAction sa = (ShellAction) a;
				String com = sa.getCommand().replaceAll("\\$\\{DATAFILE\\}", datafile.getAbsolutePath());
				com = com.replaceAll("\\$\\{FILENAME\\}", datafile.getName().replaceAll("\\.\\w*$", ""));
				ch.psi.fda.core.actions.ShellAction action = new ch.psi.fda.core.actions.ShellAction(com);
				action.setCheckExitValue(sa.isCheckExitValue());
				action.setExitValue(sa.getExitValue());
				alist.add(action);
			}
			else if(a instanceof ScriptAction){
				
				ScriptAction sa = (ScriptAction) a;
				
				// TODO set global variables DATAFILE and FILENAME
				
				// TODO create Jython Action
				Map<String, Channel<?>> mapping = new HashMap<>();
				for(ChannelParameterMapping ma: sa.getMapping()){
					if(ma.getType().equals("String")){
						mapping.put(ma.getVariable(), createChannel(String.class, ma.getChannel()));
					}
					else if(ma.getType().equals("Integer")){
						mapping.put(ma.getVariable(), createChannel(Integer.class, ma.getChannel()));
					}
					else if(ma.getType().equals("Double")){
						mapping.put(ma.getVariable(), createChannel(Double.class, ma.getChannel()));
					}
					else{
						logger.warning("Channel type ["+ma.getType()+"] is not supported for mapping");
					}
				}
				
				Map<String,Object> gobjects = new HashMap<>();
				gobjects.put("FILENAME", datafile.getName().replaceAll("\\.\\w*$", ""));
				gobjects.put("DATAFILE", datafile.getAbsoluteFile());
				ch.psi.fda.core.actions.JythonAction ja = new ch.psi.fda.core.actions.JythonAction(sa.getScript(), mapping, gobjects);
				
				alist.add(ja);
			}
		}
		return(alist);
	}
	
	/**
	 * Map a discrete step dimension onto a actor sensor loop
	 * @param dimension
	 * @return
	 */
	private ActorSensorLoop mapDiscreteStepDimension(DiscreteStepDimension dimension){
		ActorSensorLoop aLoop = new ActorSensorLoop(dimension.isZigzag());
		// Set split flag of action loop (default is false)
		aLoop.setDataGroup(dimension.isDataGroup()); 
		
		// Mapping dimension pre-actions
		aLoop.getPreActions().addAll(mapActions(dimension.getPreAction()));
		
		Long moveTimeout = this.configuration.getActorMoveTimeout();
		
		// Mapping positioners
		Double stime = 0d;
		for(DiscreteStepPositioner p: dimension.getPositioner()){
			
			if(p.getSettlingTime()>stime){
				stime = p.getSettlingTime();
			}
			
			if(p instanceof LinearPositioner){
				LinearPositioner lp =(LinearPositioner) p;
				ChannelAccessLinearActuator<?> a;
				if(lp.getType().equals("String")){
					a = new ChannelAccessLinearActuator<String>(createChannel(Double.class, lp.getName()), createChannel(String.class, lp.getDone()), lp.getDoneValue(), lp.getDoneDelay(), lp.getStart(), lp.getEnd(), lp.getStepSize(), moveTimeout);
				}
				else if(lp.getType().equals("Double")){
					a = new ChannelAccessLinearActuator<Double>(createChannel(Double.class, lp.getName()), createChannel(Double.class,lp.getDone()), Double.parseDouble(lp.getDoneValue()), lp.getDoneDelay(), lp.getStart(), lp.getEnd(), lp.getStepSize(), moveTimeout);
				}
				else{
					// Default
					a = new ChannelAccessLinearActuator<Integer>(createChannel(Double.class, lp.getName()), createChannel(Integer.class,lp.getDone()), Integer.parseInt(lp.getDoneValue()), lp.getDoneDelay(), lp.getStart(), lp.getEnd(), lp.getStepSize(), moveTimeout);
				}
				
				a.setAsynchronous(lp.isAsynchronous());
				Actor actuator = a;
				
				aLoop.getActors().add(actuator);
				
				// Add a sensor for the readback
				String name = lp.getReadback();
				if(name==null){
					name = lp.getName();
				}
				ChannelAccessSensor<Double> sensor = new ChannelAccessSensor<Double>(lp.getId(), createChannel(Double.class, name), configModel.isFailOnSensorError());
				aLoop.getSensors().add(sensor);
			}
			else if(p instanceof FunctionPositioner){
				FunctionPositioner lp =(FunctionPositioner) p;
				
				// Create function object
				JythonFunction function = mapFunction(lp.getFunction());
				
				
				// Create actuator
				ChannelAccessFunctionActuator<?> a;
				if(lp.getType().equals("String")){
					a = new ChannelAccessFunctionActuator<String>(createChannel(Double.class,lp.getName()), createChannel(String.class,lp.getDone()), lp.getDoneValue(), lp.getDoneDelay(), function, lp.getStart(), lp.getEnd(), lp.getStepSize(), moveTimeout);
				}
				else if(lp.getType().equals("Double")){
					a = new ChannelAccessFunctionActuator<Double>(createChannel(Double.class, lp.getName()), createChannel(Double.class, lp.getDone()), Double.parseDouble(lp.getDoneValue()), lp.getDoneDelay(), function, lp.getStart(), lp.getEnd(), lp.getStepSize(), moveTimeout);
				}
				else{
					// Default
					a = new ChannelAccessFunctionActuator<Integer>(createChannel(Double.class, lp.getName()), createChannel(Integer.class, lp.getDone()), Integer.parseInt(lp.getDoneValue()), lp.getDoneDelay(), function, lp.getStart(), lp.getEnd(), lp.getStepSize(), moveTimeout);
				}
				
				a.setAsynchronous(lp.isAsynchronous());
				Actor actuator = a;
				
				aLoop.getActors().add(actuator);
				
				// Add a sensor for the readback
				String name = lp.getReadback();
				if(name==null){
					name = lp.getName();
				}
				ChannelAccessSensor<Double> sensor = new ChannelAccessSensor<Double>(lp.getId(), createChannel(Double.class, name), configModel.isFailOnSensorError());
				aLoop.getSensors().add(sensor);
			}
			else if (p instanceof ArrayPositioner){
				ArrayPositioner ap = (ArrayPositioner) p;
				String[] positions = (ap.getPositions().trim()).split(" +");
				double[] table = new double[positions.length];
				for(int i=0;i<positions.length;i++){
					table[i] = Double.parseDouble(positions[i]);
				}
				
				ChannelAccessTableActuator<?> a;
				if(p.getType().equals("String")){
					a = new ChannelAccessTableActuator<String>(createChannel(Double.class, p.getName()), createChannel(String.class, p.getDone()), p.getDoneValue(), p.getDoneDelay(), table, moveTimeout);
				}
				else if(p.getType().equals("Double")){
					a = new ChannelAccessTableActuator<Double>(createChannel(Double.class, p.getName()), createChannel(Double.class, p.getDone()), Double.parseDouble(p.getDoneValue()), p.getDoneDelay(), table, moveTimeout);
				}
				else{
					// Default
					a = new ChannelAccessTableActuator<Integer>(createChannel(Double.class, p.getName()), createChannel(Integer.class, p.getDone()), Integer.parseInt(p.getDoneValue()), p.getDoneDelay(), table, moveTimeout);
				}
				
				a.setAsynchronous(p.isAsynchronous());
				Actor actuator = a;
				
				aLoop.getActors().add(actuator);
				
				// Add a sensor for the readback
				String name = ap.getReadback();
				if(name==null){
					name = ap.getName();
				}
				ChannelAccessSensor<Double> sensor = new ChannelAccessSensor<Double>(ap.getId(), createChannel(Double.class, name), configModel.isFailOnSensorError());
				aLoop.getSensors().add(sensor);
			}
			else if (p instanceof RegionPositioner){
				RegionPositioner rp = (RegionPositioner) p;
				
				ComplexActuator actuator = new ComplexActuator();
				/*
				 * Regions are translated into a complex actor consisting of a LinearActuator
				 * If consecutive regions are overlapping, i.e. end point of region a equals the
				 * start point of region b then the start point for the LinearActuator of region b
				 * is changes to its next step (start+/-stepSize depending on whether end position of the 
				 * region is > or < start of the region)
				 */
				Region lastRegion = null;
				for(Region r: rp.getRegion()){
					// Normal region
					if(r.getFunction()==null){

						// Check whether regions are consecutive
						double start = r.getStart();
						if(lastRegion!=null && start == lastRegion.getEnd()){ // TODO verify whether double comparison is ok 
							if(r.getStart()<r.getEnd()){
								start = start+r.getStepSize();
							}
							else{
								start=start-r.getStepSize();
							}
						}
						
						// Create actuator
						ChannelAccessLinearActuator<?> act;
						if(rp.getType().equals("String")){
							act = new ChannelAccessLinearActuator<String>(createChannel(Double.class, rp.getName()), createChannel(String.class, rp.getDone()), rp.getDoneValue(), rp.getDoneDelay(), start, r.getEnd(), r.getStepSize(), moveTimeout);
						}
						else if(rp.getType().equals("Double")){
							act = new ChannelAccessLinearActuator<Double>(createChannel(Double.class, rp.getName()), createChannel(Double.class, rp.getDone()), Double.parseDouble(rp.getDoneValue()), rp.getDoneDelay(), start, r.getEnd(), r.getStepSize(), moveTimeout);
						}
						else{
							act = new ChannelAccessLinearActuator<Integer>(createChannel(Double.class, rp.getName()), createChannel(Integer.class, rp.getDone()), Integer.parseInt(rp.getDoneValue()), rp.getDoneDelay(), start, r.getEnd(), r.getStepSize(), moveTimeout);
						}
						
						act.setAsynchronous(rp.isAsynchronous());
						Actor a = act;
						
						ComplexActuator ca = new ComplexActuator();
						ca.getActors().add(a);
						ca.getPreActions().addAll(mapActions(r.getPreAction()));
						actuator.getActors().add(ca);
						lastRegion = r;
					}
					else{
						// Function based region
						
						// Cannot check whether the regions are consecutive as the function 
						// used might change the start value to something else
						// [THIS LIMITATION NEEDS TO BE SOMEHOW RESOLVED IN THE NEXT VERSIONS]
						JythonFunction function = mapFunction(r.getFunction());
						ChannelAccessFunctionActuator<?> act;
						if(rp.getType().equals("String")){
							act = new ChannelAccessFunctionActuator<String>(createChannel(Double.class,rp.getName()), createChannel(String.class,rp.getDone()), rp.getDoneValue(), rp.getDoneDelay(), function, r.getStart(), r.getEnd(), r.getStepSize(), moveTimeout);
						}
						else if(rp.getType().equals("Double")){
							act = new ChannelAccessFunctionActuator<Double>(createChannel(Double.class, rp.getName()), createChannel(Double.class, rp.getDone()), Double.parseDouble(rp.getDoneValue()), rp.getDoneDelay(), function, r.getStart(), r.getEnd(), r.getStepSize(), moveTimeout);
						}
						else{
							// Default
							act = new ChannelAccessFunctionActuator<Integer>(createChannel(Double.class, rp.getName()), createChannel(Integer.class, rp.getDone()), Integer.parseInt(rp.getDoneValue()), rp.getDoneDelay(), function, r.getStart(), r.getEnd(), r.getStepSize(), moveTimeout);
						}
						
						act.setAsynchronous(rp.isAsynchronous());
						Actor a = act;
						
						ComplexActuator ca = new ComplexActuator();
						ca.getActors().add(a);
						ca.getPreActions().addAll(mapActions(r.getPreAction()));
						actuator.getActors().add(ca);
						lastRegion = r;
					}
				}
				aLoop.getActors().add(actuator);
				
				// Add a sensor for the readback
				String name = rp.getReadback();
				if(name==null){
					name = rp.getName();
				}
				ChannelAccessSensor<Double> sensor = new ChannelAccessSensor<Double>(rp.getId(), createChannel(Double.class, name), configModel.isFailOnSensorError());
				aLoop.getSensors().add(sensor);
			}
			else if(p instanceof PseudoPositioner){
				PseudoPositioner pp =(PseudoPositioner) p;
				PseudoActuatorSensor actorSensor = new PseudoActuatorSensor(pp.getId(), pp.getCounts());
				
				// Register as actor
				aLoop.getActors().add(actorSensor);
				
				// Register as sensor
				aLoop.getSensors().add(actorSensor);
			}
			else{
				// Not supported
				logger.warning("Mapping for "+p.getClass().getName()+" not available");
			}
		}
		
		// Translate settling time to post actor action
		// Only add the post actor action if the settling time is > 0
		if(stime>0){
			Double delay = stime*1000;
			aLoop.getPostActorActions().add(new Delay(delay.longValue()));
		}
		
		// Map actions between positioner and detector
		aLoop.getPreSensorActions().addAll(mapActions(dimension.getAction()));
		
		
		// Map guard (if specified)
		Guard g = dimension.getGuard();
		if(g != null){
			// Map conditions
			List<ChannelAccessGuardCondition<?>> conditions = new ArrayList<>();
			for(GuardCondition con: g.getCondition()){
				if(con.getType().equals("Integer")){
					conditions.add(new ChannelAccessGuardCondition<Integer>(createChannel(Integer.class, con.getChannel()), new Integer(con.getValue())));
				}
				else if(con.getType().equals("Double")){
					conditions.add(new ChannelAccessGuardCondition<Double>(createChannel(Double.class, con.getChannel()), new Double(con.getValue())));
				}
				else{
					conditions.add(new ChannelAccessGuardCondition<String>(createChannel(String.class, con.getChannel()), con.getValue()));
				}
			}
			// Create guard and add to loop
			ChannelAccessGuard guard = new ChannelAccessGuard(conditions);
			aLoop.setGuard(guard);
		}
		
		// Map detectors
		for(Detector detector : dimension.getDetector()){
			mapDetector(aLoop, detector);
		}
		
		
		// Mapping dimension post-actions
		aLoop.getPostActions().addAll(mapActions(dimension.getPostAction()));
		
		
		return aLoop;
	}
	
	/**
	 * Map function 
	 * @param f	Function object in the model
	 * @return	Internal function object
	 */
	private JythonFunction mapFunction(Function f){
		HashMap<String, Object> map = new HashMap<>();
		for(ParameterMapping m: f.getMapping()){
			if(m instanceof VariableParameterMapping){
				VariableParameterMapping vp = (VariableParameterMapping)m;
				Variable v = (Variable)vp.getName();
				JythonGlobalVariable var = jVariableDictionary.get(v.getName());
				var.setValue(v.getValue());
				map.put(vp.getVariable(), var);
                          
			} else if(m instanceof ChannelParameterMapping){
                              ChannelParameterMapping cpm = (ChannelParameterMapping) m;
                                JythonGlobalVariable var = new JythonGlobalVariable();
                                var.setName(cpm.getVariable());                    
                                if(cpm.getType().equals("String")){
                                    map.put(cpm.getVariable(),createChannel(String.class, cpm.getChannel()));
                                }
                                else if(cpm.getType().equals("Integer")){
                                    map.put(cpm.getVariable(),createChannel(Integer.class, cpm.getChannel()));                                    
                                }
                                else if(cpm.getType().equals("Double")){
                                    map.put(cpm.getVariable(),createChannel(Double.class, cpm.getChannel())); 
                                }
                                else{
                                        logger.warning("Channel type ["+cpm.getType()+"] is not supported for mapping");
                                }
                        }
		}
		JythonFunction function = new JythonFunction(f.getScript(), map);
		return function;
	}
	
	private void mapDetector(ActorSensorLoop aLoop, Detector detector){
		if(detector instanceof ScalarDetector){
			ScalarDetector sd = (ScalarDetector) detector;
			
			// Add pre actions
			aLoop.getPreSensorActions().addAll(mapActions(sd.getPreAction()));
			
			// Add sensor
			Sensor sensor;
			if(sd.getType().equals("String")){
				sensor = new ChannelAccessSensor<>(sd.getId(), createChannel(String.class,sd.getName()), configModel.isFailOnSensorError());
			}
			else{
				sensor = new ChannelAccessSensor<>(sd.getId(), createChannel(Double.class,sd.getName()), configModel.isFailOnSensorError());
			}
			
			aLoop.getSensors().add(sensor);
		}
		else if (detector instanceof ArrayDetector){
			ArrayDetector ad = (ArrayDetector) detector;
			
			// Add pre actions
			aLoop.getPreSensorActions().addAll(mapActions(ad.getPreAction()));
			
			// Ad sensor
			Sensor sensor = new ChannelAccessSensor<>(ad.getId(), createChannel(double[].class, ad.getName(), ad.getArraySize()), configModel.isFailOnSensorError());
			aLoop.getSensors().add(sensor);
		}
		else if (detector instanceof DetectorOfDetectors){
			DetectorOfDetectors dd = (DetectorOfDetectors) detector;
			
			// Add pre actions
			aLoop.getPreSensorActions().addAll(mapActions(dd.getPreAction()));
			
			for(Detector d: dd.getDetector()){
				// Recursively call mapping method
				mapDetector(aLoop, d);
			}
		}
		else if (detector instanceof Timestamp){
			Timestamp dd = (Timestamp) detector;
			
			// Ad sensor
			TimestampSensor sensor = new TimestampSensor(dd.getId());
			aLoop.getSensors().add(sensor);
		}
		else{
			// Not supported
			logger.warning("Detector type "+detector.getClass().getName()+" not supported");
		}
	}
	
	/**
	 * Map OTF dimension onto a OTF loop
	 * @param dimension
	 * @return
	 */
	private ActionLoop mapContinuousDimension(ContinuousDimension dimension) {

		ActionLoop aLoop = null;

		boolean hcrOnly = true;
		for (SimpleScalarDetector detector : dimension.getDetector()) {
			if (detector.isScr()) {
				hcrOnly = false;
				break;
			}
		}

		// Create loop
		boolean zigZag = dimension.isZigzag(); // default value is false

		CrlogicLoopStream actionLoop = new CrlogicLoopStream(cservice, configuration.getCrlogicPrefix(), configuration.getCrlogicIoc(), zigZag);

		actionLoop.getPreActions().addAll(mapActions(dimension.getPreAction()));

		// Map positioner
		ContinuousPositioner p = dimension.getPositioner();
		double backlash = 0;
		if (p.getAdditionalBacklash() != null) {
			backlash = p.getAdditionalBacklash();
		}
		actionLoop.setActuator(p.getId(), p.getName(), p.getReadback(), p.getStart(), p.getEnd(), p.getStepSize(), p.getIntegrationTime(), backlash);

		// Map sensors
		// ATTENTION: the sequence of the mapping depends on the sequence in the
		// schema file !
		for (SimpleScalarDetector detector : dimension.getDetector()) {
			if (!detector.isScr()) {
				actionLoop.getSensors().add(new CrlogicResource(detector.getId(), detector.getName()));
			}
		}

		for (ScalerChannel detector : dimension.getScaler()) {
			actionLoop.getSensors().add(new CrlogicResource(detector.getId(), "SCALER" + detector.getChannel(), true));
		}

		Timestamp tdetector = dimension.getTimestamp();
		if (tdetector != null) {
			actionLoop.getSensors().add(new CrlogicResource(tdetector.getId(), "TIMESTAMP"));
		}

		actionLoop.getPostActions().addAll(mapActions(dimension.getPostAction()));

		if (hcrOnly) {
			// There are no additional channels to be read out while taking data
			// via hcrlogic
			// Therefore we just register the hcr loop as action loop

			aLoop = actionLoop;
		} else {
			List<Channel<DoubleTimestamp>> sensors = new ArrayList<>();
			List<String> ids = new ArrayList<>();

			for (SimpleScalarDetector detector : dimension.getDetector()) {
				if (detector.isScr()) {
					ids.add(detector.getId());
					sensors.add(createChannel(DoubleTimestamp.class, detector.getName(), true));
				}
			}
			// Create soft(ware) based crlogic
			ScrlogicLoop scrlogic = new ScrlogicLoop(ids, sensors);

			// Create parallel logic
			ParallelCrlogic pcrlogic = new ParallelCrlogic(actionLoop, scrlogic);

			aLoop = pcrlogic;
		}
		return aLoop;
	}
	
	private <T> Channel<T> createChannel(Class<T> type, String name, boolean monitor){
		try {
			if(name== null){
				return null;
			}
			
			Channel<T> c = cservice.createChannel(new ChannelDescriptor<T>(type, name, monitor) );
			channels.add(c);
			return c;
		} catch (ChannelException | InterruptedException | TimeoutException e) {
			throw new RuntimeException("Unable to create channel: "+name,e);
		}
		
	}
	
	/**
	 * Create channel and remember to be able to destroy channels at the end
	 * @param name
	 * @param type
	 * @return	null if the name of the channel is null, otherwise the channel
	 */
	private <T> Channel<T> createChannel(Class<T> type, String name){
		try {
			if(name== null){
				return null;
			}
			
			Channel<T> c = cservice.createChannel(new ChannelDescriptor<T>(type, name) );
			channels.add(c);
			return c;
		} catch (ChannelException | InterruptedException | TimeoutException e) {
			throw new RuntimeException("Unable to create channel: "+name,e);
		}
		
	}
	
	private <T> Channel<T> createChannel(Class<T> type, String name, int size){
		try {
			if(name== null){
				return null;
			}
			
			Channel<T> c = cservice.createChannel(new ChannelDescriptor<T>(type, name, false, size) );
			channels.add(c);
			return c;
		} catch (ChannelException | InterruptedException | TimeoutException e) {
			throw new RuntimeException("Unable to create channel: "+name,e);
		}
		
	}	
}
