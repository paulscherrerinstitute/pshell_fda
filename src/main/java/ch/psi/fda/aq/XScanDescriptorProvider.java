package ch.psi.fda.aq;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import ch.psi.fda.DescriptorProvider;
import ch.psi.fda.edescriptor.EDescriptor;
import ch.psi.fda.model.ModelManager;
import ch.psi.fda.model.v1.ArrayDetector;
import ch.psi.fda.model.v1.Configuration;
import ch.psi.fda.model.v1.ContinuousPositioner;
import ch.psi.fda.model.v1.Data;
import ch.psi.fda.model.v1.Detector;
import ch.psi.fda.model.v1.LinearPositioner;
import ch.psi.fda.model.v1.Positioner;
import ch.psi.fda.model.v1.PseudoPositioner;
import ch.psi.fda.model.v1.Visualization;
import ch.psi.fda.vdescriptor.LinePlot;
import ch.psi.fda.vdescriptor.VDescriptor;
import ch.psi.fda.vdescriptor.XYSeries;
import ch.psi.fda.vdescriptor.XYZSeries;
import ch.psi.fda.vdescriptor.YSeries;
import ch.psi.fda.vdescriptor.YZSeries;

public class XScanDescriptorProvider implements DescriptorProvider {
	
	private static final Logger logger = Logger.getLogger(XScanDescriptorProvider.class.getName());

	private EDescriptor edescriptor;
	private VDescriptor vdescriptor;
	
	@Override
	public void load(File... files) {
		
		if(files.length<1 || files[0]==null){
			throw new IllegalArgumentException("There need to be at lease one file specified");
		}
		File file = files[0];
		
		if(!file.exists()){
			throw new IllegalArgumentException("File "+file.getAbsolutePath()+" does not exist");
		}
		
		Configuration c;
		try {
			c = ModelManager.unmarshall(file);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Unable to deserialize configuration: "+e.getMessage(), e);
		}
		
		// Set data file name
		// Determine name used for the data file
		String name = file.getName();
		name = name.replaceAll("\\.xml$", "");
		
		if(c.getData()!=null){
			Data data = c.getData();
			// Only update filename if no name is specified in xml file
			if(data.getFileName()==null){
				data.setFileName(name);
			}
		}
		else{
			Data data = new Data();
			data.setFileName(name);
			c.setData(data);
		}
		
		
//		// Override number of executions
//		if(iterations != null){
//			c.setNumberOfExecution(iterations);
//		}
		// Fix configuration if iterations is specified with 0 and no iterations option is specified
		if(c.getNumberOfExecution()==0){
			c.setNumberOfExecution(1);
		}
		
		this.edescriptor = new XScanDescriptor(c);
		this.vdescriptor = mapVisualizations(c.getVisualization());
		
	}
	
	/**
	 * Create a vdescriptor out of the scan description
	 * @param vl
	 * @return
	 */
	public static VDescriptor mapVisualizations(List<Visualization> vl){
		VDescriptor vd = new VDescriptor();
		
		
		for(Visualization v: vl){
			if(v instanceof ch.psi.fda.model.v1.LinePlot){
				ch.psi.fda.model.v1.LinePlot lp = (ch.psi.fda.model.v1.LinePlot) v;
				
				String x = getId(lp.getX());
				
				LinePlot lineplot = new LinePlot(lp.getTitle());
				List<Object> l = lp.getY();
				for(Object o: l){
					String y = getId(o);
					lineplot.getData().add(new XYSeries(x, y));
				}
				
				vd.getPlots().add(lineplot);
			}
			else if(v instanceof ch.psi.fda.model.v1.LinePlotArray){
				// Array visualization
				ch.psi.fda.model.v1.LinePlotArray lp = (ch.psi.fda.model.v1.LinePlotArray) v;
				
				LinePlot lineplot = new LinePlot(lp.getTitle());
				// Create data filter for visualization
				List<Object> l = lp.getY();
				for(Object o: l){
					String idY = getId(o);
					
					// TODO Need to actually check if minX of 
					lineplot.setMinX(new Double(lp.getOffset()));
					lineplot.setMaxX(new Double(lp.getOffset()+lp.getSize()));
                                        lineplot.setMaxSeries(lp.getMaxSeries());
					lineplot.getData().add(new YSeries(idY));
				}
                                vd.getPlots().add(lineplot);
			}
			else if(v instanceof ch.psi.fda.model.v1.MatrixPlot){
				
				// MatrixPlot does currently not support RegionPositioners because of the
				// plotting problems this would cause. If regions of the positioner have different
				// step sizes it is not easily possible (without (specialized) rasterization) to plot the data.
				
				ch.psi.fda.model.v1.MatrixPlot mp = (ch.psi.fda.model.v1.MatrixPlot) v;
				

				double minX, maxX;
				int nX;
				double minY, maxY;
				int nY;
				
				String idX, idY, idZ;
				
				// X Axis
				if(mp.getX() instanceof LinearPositioner){
					LinearPositioner linp = ((LinearPositioner)mp.getX());
					idX = linp.getId();
					
					minX = (Math.min(linp.getStart(), linp.getEnd()));
					maxX = (Math.max(linp.getStart(), linp.getEnd()));
					nX = ((int) Math.floor((Math.abs(maxX-minX))/linp.getStepSize()) + 1);
				}
				else if(mp.getX() instanceof PseudoPositioner){
					PseudoPositioner pp = ((PseudoPositioner)mp.getX());
					idX = pp.getId();
					minX = (1); // Count starts at 1
					maxX = (pp.getCounts());
					nX = (pp.getCounts());
				}
				else if(mp.getX() instanceof ContinuousPositioner){
					ContinuousPositioner conp = ((ContinuousPositioner)mp.getX());
					idX = conp.getId();
					
					minX = (Math.min(conp.getStart(), conp.getEnd()));
					maxX = (Math.max(conp.getStart(), conp.getEnd()));
					nX = ((int) Math.floor((Math.abs(maxX-minX))/conp.getStepSize()) + 1);
				}
				else{
					// Fail as we cannot determine the min, max and number of steps
					throw new RuntimeException(mp.getX().getClass().getName()+" is not supported as x-axis of a MatrixPlot");
				}
				
				// Y Axis
				if(mp.getY() instanceof LinearPositioner){
					LinearPositioner linp = ((LinearPositioner)mp.getY());
					idY = linp.getId();
					minY = (Math.min(linp.getStart(), linp.getEnd()));
					maxY = (Math.max(linp.getStart(), linp.getEnd()));
					nY = ((int) Math.floor((Math.abs(maxY-minY))/linp.getStepSize()) + 1);
				}
				else if(mp.getY() instanceof PseudoPositioner){
					PseudoPositioner pp = ((PseudoPositioner)mp.getY());
					idY = pp.getId();
					minY = (1); // Count starts at 1
					maxY = (pp.getCounts());
					nY = (pp.getCounts());
				}
				else{
					// Fail as we cannot determine the min, max and number of steps
					throw new RuntimeException(mp.getY().getClass().getName()+" is not supported as y-axis of a MatrixPlot");
				}
				
				// Z Dimension
				idZ = getId(mp.getZ());

				
				ch.psi.fda.vdescriptor.MatrixPlot matrixplot = new ch.psi.fda.vdescriptor.MatrixPlot(mp.getTitle());
				matrixplot.setMinX(minX);
				matrixplot.setMaxX(maxX);
				matrixplot.setnX(nX);
				matrixplot.setMinY(minY);
				matrixplot.setMaxY(maxY);
				matrixplot.setnY(nY);
                                matrixplot.setType(mp.getType());
				
				matrixplot.getData().add(new XYZSeries(idX, idY, idZ));
				vd.getPlots().add(matrixplot);
			}
			else if(v instanceof ch.psi.fda.model.v1.MatrixPlotArray){
				// Support for 2D waveform plots
				ch.psi.fda.model.v1.MatrixPlotArray mp = (ch.psi.fda.model.v1.MatrixPlotArray) v;
				
				// Get size of the array detector
				int arraySize = 0;
				Object o = mp.getZ();
				if(o instanceof ArrayDetector){
					ArrayDetector ad = (ArrayDetector) o;
					arraySize = ad.getArraySize();
				}
				else{
					// Workaround
					arraySize = mp.getSize(); // of array is from a manipulation the size is not known. Then the size will indicate the size of the array to display
				}
				
				int offset = mp.getOffset();
				// Determine size for array
				int size = mp.getSize();
				if(size>0 && offset+size<arraySize){
					size = mp.getSize();
				}
				else{
					size=arraySize-offset;
				}
				
				
				double minY, maxY;
				int nY;
				
				double minX = offset;
				double maxX = offset+size-1;
				int nX = size;
				
				String idY, idZ;
				
				// Y Axis
				if(mp.getY() instanceof LinearPositioner){
					LinearPositioner linp = ((LinearPositioner)mp.getY());
					idY = linp.getId();
					
					minY = (Math.min(linp.getStart(), linp.getEnd()));
					maxY = (Math.max(linp.getStart(), linp.getEnd()));
					nY = ((int) Math.floor((Math.abs(maxY-minY))/linp.getStepSize()) + 1);
				}
				else if(mp.getY() instanceof PseudoPositioner){
					PseudoPositioner pp = ((PseudoPositioner)mp.getY());
					idY = pp.getId();
					minY = (1); // Count starts at 1
					maxY = (pp.getCounts());
					nY = (pp.getCounts());
				}
				else if(mp.getY() instanceof ContinuousPositioner){
					ContinuousPositioner conp = ((ContinuousPositioner)mp.getY());
					idY = conp.getId();
					
					minY = (Math.min(conp.getStart(), conp.getEnd()));
					maxY = (Math.max(conp.getStart(), conp.getEnd()));
					nY = ((int) Math.floor((Math.abs(maxY-minY))/conp.getStepSize()) + 1);
				}
				else{
					// Fail as we cannot determine the min, max and number of steps
					throw new RuntimeException(mp.getY().getClass().getName()+" is not supported as x-axis of a MatrixPlot");
				}
				
				
				// Z Dimension
				idZ = getId(mp.getZ());

				ch.psi.fda.vdescriptor.MatrixPlot matrixplot = new ch.psi.fda.vdescriptor.MatrixPlot(mp.getTitle());
				matrixplot.setMinX(minX);
				matrixplot.setMaxX(maxX);
				matrixplot.setnX(nX);
				matrixplot.setMinY(minY);
				matrixplot.setMaxY(maxY);
				matrixplot.setnY(nY);
                                matrixplot.setType(mp.getType());
				
				matrixplot.getData().add(new YZSeries(idY, idZ));
				vd.getPlots().add(matrixplot);
				
			}
			else{
				logger.warning(v.getClass().getName()+" is not supported as visualization type");
			}
		}
		return vd;
	}
	
	/**
	 * Retrieve id string of the passed object
	 * @param object
	 * @return	Id string of object
	 */
	private static String getId(Object object){
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
		// For testing purposes
		else if(object instanceof String){
			id = (String) object;
		}
		else{
			throw new RuntimeException("Unable to identify id of object reference "+object);
		}
		return id;
	}
	

	@Override
	public EDescriptor getEDescriptor() {
		return edescriptor;
	}

	@Override
	public VDescriptor getVDescriptor() {
		return vdescriptor;
	}

	@Override
	public Class<?> getEDescriptorClass() {
		return XScanDescriptor.class;
	}

}
