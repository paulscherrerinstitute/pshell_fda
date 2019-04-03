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

package ch.psi.fda.messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Message holding data
 */
public class DataMessage extends Message{
	
	private static final long serialVersionUID = 1L;
	
	private final List<Object> data;
	private List<Metadata> metadata;
	
//	public DataMessage(){
//		this.data = new ArrayList<Object>();
//		this.metadata = new ArrayList<>();
//	}
//	
	public DataMessage(List<Metadata> metadata){
		this.data = new ArrayList<Object>();
		this.metadata = metadata;
	}
	
	public List<Object> getData(){
		return(data);
	}
	public List<Metadata> getMetadata(){
		return metadata;
	}
	public void setMetadata(List<Metadata> metadata){
		this.metadata = metadata;
	}
	
	// Utility functions
	
	@SuppressWarnings("unchecked")
	public <T> T getData(String id){
		int i=0;
		for(Metadata m: metadata){
			if(m.getId().equals(id)){
				return (T) data.get(i);
			}
			i++;
		}
		throw new IllegalArgumentException("No data found for id: "+id);
	}
	
	public Metadata getMetadata(String id){
		for(Metadata m: metadata){
			if(m.getId().equals(id)){
				return m;
			}
		}
		throw new IllegalArgumentException("No data found for id: "+id);
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Message [ ");
		for (Object o : data) {
			if (o.getClass().isArray()) {
				// If the array object is of type double[] display its content
				if (o instanceof double[]) {
					double[] oa = (double[]) o;
					b.append("[ ");
					for (double o1 : oa) {
						b.append(o1);
						b.append(" ");
					}
					b.append("]");
				} else {
					b.append(o.toString());
				}
			} else {
				b.append(o);
			}
			
			b.append(" ");
		}
		b.append("]");
		return b.toString();
	}
}
