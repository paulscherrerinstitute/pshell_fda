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

package ch.psi.fda.core.scripting;


/**
 * Mapping of a script parameter to a component via the Id.
 */
public class JythonParameterMappingGlobalVariable extends JythonParameterMapping {
	
	private JythonGlobalVariable globalVariable;
		
	/**
	 * Constuctor
	 * @param variable
	 * @param globalVariable
	 */
	public JythonParameterMappingGlobalVariable(String variable, JythonGlobalVariable globalVariable){
		super(variable);
		this.globalVariable = globalVariable;
	}

	/**
	 * @return the globalVariable
	 */
	public JythonGlobalVariable getGlobalVariable() {
		return globalVariable;
	}

	/**
	 * @param globalVariable the globalVariable to set
	 */
	public void setGlobalVariable(JythonGlobalVariable globalVariable) {
		this.globalVariable = globalVariable;
	}
	
}
