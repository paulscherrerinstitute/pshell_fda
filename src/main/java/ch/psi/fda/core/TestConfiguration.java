/**
 * 
 * Copyright 2012 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This code is distributed in the hope that it will be useful, but without any
 * warranty; without even the implied warranty of merchantability or fitness for
 * a particular purpose. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package ch.psi.fda.core;

public class TestConfiguration {
	private static final TestConfiguration instance = new TestConfiguration();
	
	private final String otfPrefix = "MTEST-HW3-OTFX";
	private final String crlogicPrefix = "MTEST-HW3-CRL";
	private final String prefixScaler = "MTEST-HW3:JS";
	private final String server = "MTEST-VME-HW3";
	
	private final String motor1 = "MTEST-HW3:MOT1";
	private final String analogIn1 = "MTEST-HW3-AI1:AI_01";
	
	private final String ioc = "MTEST-VME-HW3.psi.ch";
	
	private TestConfiguration(){
	}
	
	public static TestConfiguration getInstance(){
		return instance;
	}

	/**
	 * @return the prefix
	 */
	public String getCrlogicPrefix() {
		return crlogicPrefix;
	}

	/**
	 * @return the prefixScaler
	 */
	public String getPrefixScaler() {
		return prefixScaler;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @return the motor1
	 */
	public String getMotor1() {
		return motor1;
	}

	/**
	 * @return the analogIn1
	 */
	public String getAnalogIn1() {
		return analogIn1;
	}

	/**
	 * @return the otfPrefix
	 */
	public String getOtfPrefix() {
		return otfPrefix;
	}

	public String getIoc() {
		return ioc;
	}
	
}
