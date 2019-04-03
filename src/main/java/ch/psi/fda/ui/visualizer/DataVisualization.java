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

package ch.psi.fda.ui.visualizer;

import java.util.ArrayList;
import java.util.List;

import ch.psi.pshell.plot.Plot;

public class DataVisualization {

	private final Plot plot;
	private final List<XYSeriesDataFilter> series;
	
	public DataVisualization(Plot plot){
		this.plot = plot;
		this.series = new ArrayList<XYSeriesDataFilter>();
	}

	/**
	 * @return the plot
	 */
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @return the series
	 */
	public List<XYSeriesDataFilter> getSeries() {
		return series;
	}
	
}
