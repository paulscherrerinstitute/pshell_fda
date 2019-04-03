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

import ch.psi.fda.LayoutFDA;
import ch.psi.pshell.core.Context;
import ch.psi.pshell.ui.App;
import java.util.logging.Logger;

public class AcquisitionConfiguration {
	
	
	private static final Logger logger = Logger.getLogger(AcquisitionConfiguration.class.getName());
	
	public final static String FDA_CONFIG_FILE = "ch.psi.fda.xscan.config.file";
	
	private String crlogicPrefix;
	private String crlogicIoc;
	
	/**
	 * Base directory for data. The directory may contain date macros. The string may contain any @see java.text.SimpleDateFormat
	 * patterns within ${ } brackets. The macros are resolved with the actual time while the get method
	 * of this property is called.
	 */
	private String dataBaseDirectory = System.getProperty("user.home");
	/**
	 * Prefix of the data file. The prefix may contain date macros. The string may contain any @see java.text.SimpleDateFormat
	 * patterns within ${ } brackets. The macros are resolved with the actual time while the get method
	 * of this property is called.
	 */
	private String dataFilePrefix = "";
	
	/**
	 * Maximum time for a actor move
	 */
	private Long actorMoveTimeout = 600000l; // 10 Minutes maximum move time 
	
	private String smptServer;
        
        private boolean appendSuffix = true;
	
	
	/**
	 * Default Constructor
	 * The constructor will read the configuration from the /fda.properties file (resource) located in the classpath.
	 */
	public AcquisitionConfiguration(){
		//loadConfiguration(System.getProperty(FDA_CONFIG_FILE));
                
                //Enforce PShell config insted
                crlogicPrefix = App.hasArgument("crlogic.prefix") ? App.getArgumentValue("crlogic.prefix") : "";
                crlogicIoc = App.hasArgument("crlogic.ioc") ? App.getArgumentValue("crlogic.ioc") : "";;		
                smptServer= "mail.psi.ch";
                actorMoveTimeout = App.hasArgument("move.timeout") ? Long.valueOf(App.getArgumentValue("move.timeout")): 600000l; // 10 Minutes maximum move time 
                dataBaseDirectory = Context.getInstance().getSetup().getDataPath();
                appendSuffix = App.hasArgument("fdanosuffix") ? false : true;
                dataFilePrefix = "";
	}
        
        public static String getDataFileNameDefault(){
            String ret = Context.getInstance().getConfig().dataPath;
            ret = ret.replaceAll("./$", "");
            return  ret + "/" + LayoutFDA.getFilePrefix();
        }
	
	/**
	 * Load configuration from properties file
	public void loadConfiguration(String file) {
		Properties properties = new Properties();
		
		File cfile = null;
		// Only read in the property file if a file is specified
		if(file != null){
			cfile = new File(file);
			try {
				properties.load(new FileReader(cfile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Configuration file "+file+" not found", e);
			} catch (IOException e) {
				throw new RuntimeException("Cannot read configuration file "+file, e);
			}
		}
		else{
			logger.warning("No configfile specified !");
		}
		
		// The defaults are set here
		crlogicPrefix = properties.getProperty(AcquisitionConfiguration.class.getPackage().getName()+".crlogic.prefix", "");
		crlogicIoc= properties.getProperty(AcquisitionConfiguration.class.getPackage().getName()+".crlogic.ioc", "");
		
		dataBaseDirectory = properties.getProperty(AcquisitionConfiguration.class.getPackage().getName()+".data.dir",".");
		if(cfile!=null && dataBaseDirectory.matches("^\\.\\.?/.*")){ // if basedir starts with . or .. we assume the data directory to be relative to the directory of the configuration file
			dataBaseDirectory = cfile.getParentFile().getAbsolutePath()+"/"+dataBaseDirectory; 
		}
		dataFilePrefix = properties.getProperty(AcquisitionConfiguration.class.getPackage().getName()+".data.filePrefix","");
		
		
		actorMoveTimeout = new Long(properties.getProperty(AcquisitionConfiguration.class.getPackage().getName()+".actorMoveTimeout","600000"));
		
		smptServer= properties.getProperty(AcquisitionConfiguration.class.getPackage().getName()+".notification.host","mail.psi.ch");
		
	}
	 */
	
	/**
	 * Replace ${name} and ${date} macro given string
	 * @param string
	 * @param date
	 * @param name
	 * @return
	public String replaceMacros(String string, Date date, String name){
		String newString = string;
		
		// Replace scan name macros
		newString = newString.replaceAll("\\$\\{name\\}", name);
		
		
		// Replace date macros
		Pattern pattern = Pattern.compile("\\$\\{[a-z,A-Z,-,_,:]*\\}");
        Matcher matcher = pattern.matcher(newString);
        while(matcher.find()){
        	String datePattern = matcher.group();
        	datePattern = datePattern.replaceAll("\\$\\{", "");
        	datePattern = datePattern.replaceAll("\\}", "");
        	SimpleDateFormat datef = new SimpleDateFormat(datePattern);
        	newString = matcher.replaceFirst(datef.format(date));
        	matcher = pattern.matcher(newString);
        }
		return newString;
	}
        */ 


	public String getCrlogicPrefix() {
		return crlogicPrefix;
	}

	public void setCrlogicPrefix(String crlogicPrefix) {
		this.crlogicPrefix = crlogicPrefix;
	}
	
	public void setCrlogicIoc(String crlogicIoc) {
		this.crlogicIoc = crlogicIoc;
	}
	public String getCrlogicIoc() {
		return crlogicIoc;
	}
	

	public String getDataBaseDirectory() {
		return dataBaseDirectory;
	}

	public void setDataBaseDirectory(String dataBaseDirectory) {
		this.dataBaseDirectory = dataBaseDirectory;
	}

	public String getDataFilePrefix() {
                if((dataFilePrefix == null) || (dataFilePrefix.trim().length()==0)){
                    return getDataFileNameDefault();
                }
		return dataFilePrefix;
	}

	public void setDataFilePrefix(String dataFilePrefix) {
		this.dataFilePrefix = dataFilePrefix;
	}

	public Long getActorMoveTimeout() {
		return actorMoveTimeout;
	}

	public void setActorMoveTimeout(Long actorMoveTimeout) {
		this.actorMoveTimeout = actorMoveTimeout;
	}

	public String getSmptServer() {
		return smptServer;
	}

	public void setSmptServer(String smptServer) {
		this.smptServer = smptServer;
	}
        
        public void setAppendSuffix(boolean value){
            appendSuffix = value;
        }

        public boolean getAppendSuffix(){
            return appendSuffix;
        }
        
}
