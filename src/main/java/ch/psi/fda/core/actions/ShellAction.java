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

package ch.psi.fda.core.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.psi.fda.core.Action;

/**
 * Action that executes a specified command when it is executed.
 */
public class ShellAction implements Action{
	
	private static Logger logger = Logger.getLogger(ShellAction.class.getName());

	private volatile Process process;
	private volatile boolean abort = false;
	private boolean checkExitValue = true;
	private int exitValue = 0;
	
	/**
	 * Name (full path if it is not in the system path) of the script to execute when
	 * the execute() function of this action is invoked.
	 */
	private final String script;
	
	/**
	 * @param script	Name of the command to execute when this action is invoked
	 * 
	 * @throws IllegalArgumentException		Specified script does not exist
	 */
	public ShellAction(String script){
		String[] scri = script.split("[ ,\t]");
		File s = new File(scri[0]);
		if(!s.exists()){
			throw new IllegalArgumentException("Script "+script+" does not exist.");
		}
		this.script = script;
	}
	
	@Override
	public void execute() throws InterruptedException {
		try{
			abort = false;
			logger.fine("Execute script "+script);
			process = Runtime.getRuntime().exec(new String[]{"/bin/bash","-c",script});
			int exitVal = process.waitFor();
			
			// Log output of the shell script if loglevel is finest
			if(logger.isLoggable(Level.FINEST)){
				logger.finest("STDOUT [BEGIN]");
				// Ideally the readout of the stream should be in parallel to the processing of the script! I.e. the output appears in the log as it is generated by the script!
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				while((line=reader.readLine()) != null){
					logger.finest(line);
				}
				logger.finest("STDOUT [END]");
				
				logger.finest("STDERR [BEGIN]");
				// Ideally the readout of the stream should be in parallel to the processing of the script! I.e. the output appears in the log as it is generated by the script!
				reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				line = null;
				while((line=reader.readLine()) != null){
					logger.finest(line);
				}
				logger.finest("STDERR [END]");
			}
			
			logger.fine("Script ["+script+"] return value: "+exitVal);
			
			if(abort){
				throw new RuntimeException("Script ["+script+"] was aborted");
			}
			else{
				// Check script exit value to 0 if != 0 then throw an runtime exception
				if(checkExitValue && exitVal != exitValue){
					throw new RuntimeException("Script ["+script+"] returned with an exit value not equal to 0");
				}
			}
			process = null;
		}
		catch(IOException e){
			throw new RuntimeException("Unable to execute script: "+script,e);
		}
	}

	@Override
	public void abort() {
		abort=true;
		if(process!=null){
			process.destroy();
		}
	}

	public boolean isCheckExitValue() {
		return checkExitValue;
	}

	public void setCheckExitValue(boolean checkExitValue) {
		this.checkExitValue = checkExitValue;
	}

	public int getExitValue() {
		return exitValue;
	}

	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}	
}
