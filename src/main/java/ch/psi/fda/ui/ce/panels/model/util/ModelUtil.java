/*
 *  Copyright (C) 2011 Paul Scherrer Institute
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.psi.fda.ui.ce.panels.model.util;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.model.v1.Configuration;
import ch.psi.fda.ui.ce.panels.model.ConfigurationPanel;
import ch.psi.fda.model.v1.ContinuousDimension;
import ch.psi.fda.model.v1.Detector;
import ch.psi.fda.model.v1.DiscreteStepDimension;
import ch.psi.fda.model.v1.IDParameterMapping;
import ch.psi.fda.model.v1.LinePlot;
import ch.psi.fda.model.v1.LinePlotArray;
import ch.psi.fda.model.v1.Manipulation;
import ch.psi.fda.model.v1.MatrixPlot;
import ch.psi.fda.model.v1.MatrixPlotArray;
import ch.psi.fda.model.v1.ParameterMapping;
import ch.psi.fda.model.v1.Positioner;
import ch.psi.fda.model.v1.ScalerChannel;
import ch.psi.fda.model.v1.Scan;
import ch.psi.fda.model.v1.ScriptManipulation;
import ch.psi.fda.model.v1.SimpleScalarDetector;
import ch.psi.fda.model.v1.Variable;
import ch.psi.fda.model.v1.Visualization;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.ListContainer;
import ch.psi.fda.ui.ce.panels.model.GlobalVariableParameterMappingPanel;
import ch.psi.fda.ui.ce.panels.model.IDParameterMappingPanel;
import ch.psi.fda.ui.ce.panels.model.LinePlotPanel;
import ch.psi.fda.ui.ce.panels.model.MatrixPlotPanel;
import ch.psi.pshell.ui.App;
import ch.psi.pshell.ui.Processor;
import ch.psi.utils.swing.SwingUtils;
import ch.psi.utils.swing.SwingUtils.OptionResult;
import ch.psi.utils.swing.SwingUtils.OptionType;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
//import org.openide.DialogDisplayer;
//import org.openide.NotifyDescriptor;
//import org.openide.util.Utilities;

/**
 *
 * @author ebner
 */
public class ModelUtil {

    private static ModelUtil instance = new ModelUtil();

    private ModelUtil(){
    }

    /**
     * Get singleton instance
     * @return
     */
    public static ModelUtil getInstance(){
        return instance;
    }

    ConfigurationPanel configurationPanel;
    public void setConfigurationPanel(ConfigurationPanel c){
        configurationPanel = c;
    }
    
    public  ConfigurationPanel getConfigurationPanel(){
        if (configurationPanel!=null){
            return configurationPanel;
        }
        ProcessorFDA cur = ProcessorFDA.getCurrent();
        return (cur==null) ? null : cur.getConfigPanel();
    }
    
    
    /**
     * Get names of all global variables
     * @return
     */
    public List<String> getGlobalVariables(){

        List<String> idList = new ArrayList<String>();

        ConfigurationPanel cp = getConfigurationPanel();
        Scan scan = cp.getObject().getScan();

        for(Variable v: cp.getObject().getVariable()){
            idList.add(v.getName());
        }
        return idList;
    }

    /**
     * Get all id's out of the model
     * @return  List of ids
     */
    public List<String> getIds(){

        List<String> idList = new ArrayList<String>();

        ConfigurationPanel cp = getConfigurationPanel();
        Scan scan = cp.getObject().getScan();

        if(scan!=null){
            // Continuous dimensions
            if(scan.getCdimension()!=null){
                ContinuousDimension dim = scan.getCdimension();
                for(SimpleScalarDetector d: dim.getDetector()){
                    idList.add(d.getId());
                }

                idList.add(dim.getPositioner().getId());

                for(ScalerChannel s: dim.getScaler()){
                    idList.add(s.getId());
                }

                if(dim.getTimestamp() != null){
                    idList.add(dim.getTimestamp().getId());
                }
            }

            // Discrete step dimensions
            for(DiscreteStepDimension dim: scan.getDimension()){
                for(Detector d: dim.getDetector()){
                    idList.add(d.getId());
                }
                for(Positioner p: dim.getPositioner()){
                    idList.add(p.getId());
                }
            }

            // Get Ids of manipulators
            for(Manipulation m: scan.getManipulation()){
                idList.add(m.getId());
            }
           
        }
        return idList;

    }

    /**
     * Get object for given id
     * @param id
     * @return
     */
    public Object getObject(String id){
        if(id==null || id.equals("")){
            return null;
        }

        ConfigurationPanel cp = getConfigurationPanel();
        Scan scan = cp.getObject().getScan();

        for(Variable v: cp.getObject().getVariable()){
            if(id.equals(v.getName())){
                return v;
            }
        }

        if(scan!=null){
            // Continuous dimensions
            if(scan.getCdimension()!=null){
                ContinuousDimension dim = scan.getCdimension();
                if(id.equals(dim.getPositioner().getId())){
                    return dim.getPositioner();
                }

                for(SimpleScalarDetector d: dim.getDetector()){
                    if(id.equals(d.getId())){
                         return d;
                    }
                }                

                for(ScalerChannel s: dim.getScaler()){
                    if(id.equals(s.getId())){
                        return s;
                    }
                }


                if(dim.getTimestamp()!=null && id.equals(dim.getTimestamp().getId())){
                    return(dim.getTimestamp());
                }
            }

            // Discrete step dimensions
            for(DiscreteStepDimension dim: scan.getDimension()){
                for(Positioner p: dim.getPositioner()){
                    if(id.equals(p.getId())){
                        return p;
                    }
                }

                for(Detector d: dim.getDetector()){
                    if(id.equals(d.getId())){
                        return d;
                    }
                }
            }

            // Get Ids of manipulators
            for(Manipulation m: scan.getManipulation()){
                if(id.equals(m.getId())){
                    return m;
                }
            }
        }
        return null;
    }


    /**
     * Get id of a given object
     * @param object
     * @return
     */
    public String getId(Object object){
        
        if(object instanceof Positioner){
            return(((Positioner)object).getId());
        }
        else if(object instanceof Detector){
            return(((Detector)object).getId());
        }
        else if(object instanceof Manipulation){
            return(((Manipulation)object).getId());
        }
        else if(object instanceof Variable){
            return(((Variable)object).getName());
        }

        return null;
    }

    /**
     * Find the object in any mapping of the model
     * and delete the mapping.
     *
     * @param object
     */
    public void findInMappingAndRemove(Object object){
        ConfigurationPanel cp = getConfigurationPanel();
        Configuration configuration = cp.getObject();
        Scan scan = configuration.getScan();

        if(scan != null){
            List<Manipulation> removeList = new ArrayList<Manipulation>();
            // Get Ids of manipulators
            for(Manipulation m: scan.getManipulation()){
                if(m instanceof ScriptManipulation){
                    ScriptManipulation sm = (ScriptManipulation)m;
                    for(ParameterMapping p: sm.getMapping()){
                        if(p instanceof IDParameterMapping){
                            if(((IDParameterMapping) p).getRefid().equals(object)){
                                removeList.add(m);
                            }
                        }
                    }
                }
            }

            if(removeList.size()>0){
                // Notify user whether he really wants to delete the manipulations
                /*
                NotifyDescriptor d = new NotifyDescriptor.Confirmation("There are manipulations refering to the deleted object. Do you want to delete these manipulations?", NotifyDescriptor.YES_NO_OPTION);
                Object r = DialogDisplayer.getDefault().notify(d);


                if(r.equals(NotifyDescriptor.YES_OPTION)){
                    // Remove manipulations from the model
                    scan.getManipulation().removeAll(removeList);
                }
                */
                if (App.getInstance().getMainFrame().showOption("Warning", "There are manipulations refering to the deleted object. Do you want to delete these manipulations?", OptionType.YesNo) == OptionResult.Yes) {
                   // Remove manipulations from the model
                    scan.getManipulation().removeAll(removeList);
                
                }
            }
        }

        List<Visualization> rlist = new ArrayList<Visualization>();
        for(Visualization v: configuration.getVisualization()){
            if(v instanceof LinePlot){
                LinePlot lp = (LinePlot)v;
                if(lp.getX().equals(object)){
                    rlist.add(v);
                    continue;
                }
                else if(lp.getY().size()>0){
                    // Check for references in Y
                    boolean found = false;
                    for(Object o: lp.getY()){
                        if (o.equals(object)){
                            found=true;
                            break;

                        }
                    }
                    if(found){
                        rlist.add(v);
                        continue;
                    }
                }
            }
            else if(v instanceof LinePlotArray){
                LinePlotArray lp = (LinePlotArray)v;
                if(lp.getY().size()>0){
                    // Check for references in Y
                    boolean found = false;
                    for(Object o: lp.getY()){
                        if (o.equals(object)){
                            found=true;
                            break;

                        }
                    }
                    if(found){
                        rlist.add(v);
                        continue;
                    }
                }
            }
            else if(v instanceof MatrixPlot){
                MatrixPlot mp = (MatrixPlot)v;
                if(mp.getX().equals(object)){
                    rlist.add(v);
                    continue;
                }
                else if (mp.getY().equals(object)){
                    rlist.add(v);
                    continue;
                }
                else if (mp.getZ().equals(object)){
                    rlist.add(v);
                    continue;
                }
            }
            else if(v instanceof MatrixPlotArray){
                MatrixPlotArray mp = (MatrixPlotArray)v;
                if (mp.getY().equals(object)){
                    rlist.add(v);
                    continue;
                }
                else if (mp.getZ().equals(object)){
                    rlist.add(v);
                    continue;
                }
            }
        }

        if(rlist.size()>0){
            // Notify user whether he really wants to delete the manipulations
            /*
            NotifyDescriptor d = new NotifyDescriptor.Confirmation("There are visualizations refering to the deleted object. Do you want to delete these visualizations?", NotifyDescriptor.YES_NO_OPTION);
            Object r = DialogDisplayer.getDefault().notify(d);


            if(r.equals(NotifyDescriptor.YES_OPTION)){
                // Remove all visualizations
                configuration.getVisualization().removeAll(rlist);
            }
            */
                if (App.getInstance().getMainFrame().showOption("Warning", "There are visualizations refering to the deleted object. Do you want to delete these visualizations?", OptionType.YesNo) == OptionResult.Yes) {
                   // Remove manipulations from the model
                    configuration.getVisualization().removeAll(rlist);
                
                }
            
        }

    }


    /**
     * WORKAROUNDS
     */

    /**
     * Workaround for updating lists
     */
    public void refreshAll(){
        ConfigurationPanel cp = getConfigurationPanel();
        refreshLists(cp);
        cp.revalidate();
        cp.repaint();
    }

    private void refreshLists(JPanel c){
        for(Component co: c.getComponents()){

            if(co instanceof ListContainer<?>){
                ListContainer<?> lc = (ListContainer<?>)co;
                lc.relayout();
            }

            if(co instanceof JPanel){
                refreshLists((JPanel)co);
            }
        }
    }

    /**
     * Workaround for updating ids
     */
    public void refreshIds(){
        ConfigurationPanel cp = getConfigurationPanel();
        refreshIds(cp);
        cp.revalidate();
        cp.repaint();
    }

    private void refreshIds(JPanel c){
        for(Component co: c.getComponents()){
            if(co instanceof LinePlotPanel){
                LinePlotPanel lc = (LinePlotPanel)co;
                lc.updateIds();
            }

            if(co instanceof MatrixPlotPanel){
                MatrixPlotPanel lc = (MatrixPlotPanel)co;
                lc.updateIds();
            }

            if(co instanceof IDParameterMappingPanel){
                IDParameterMappingPanel lc = (IDParameterMappingPanel)co;
                lc.updateIds();
            }

            if(co instanceof GlobalVariableParameterMappingPanel){
                GlobalVariableParameterMappingPanel lc = (GlobalVariableParameterMappingPanel)co;
                lc.updateIds();
            }

            if(co instanceof JPanel){
                refreshIds((JPanel)co);
            }
        }
    }


    /**
     * Workaround save - detect whether something has changed
     * @return
     */
    public boolean wasModified(){
        ConfigurationPanel cp = getConfigurationPanel();
        return wasModified(cp);
    }

    private boolean wasModified(JPanel c){
        boolean modified = false;
        if(c!=null){

            // Component itself is an editable component
            if(c instanceof EditableComponent){
                EditableComponent lc = (EditableComponent)c;
                modified = lc.modified();
                if(modified){
                    return modified;
                }
            }

            for(Component co: c.getComponents()){
//                if(co instanceof EditableComponent){
//                    EditableComponent lc = (EditableComponent)co;
//                    modified = lc.modified();
//                    if(modified){
//                        Logger.getLogger(ModelUtil.class.getName()).log(Level.INFO, "Modified object: "+lc);
//                        break;
//                    }
//                }

                if(co instanceof JPanel){
                    modified = wasModified((JPanel)co);
                    if(modified){
                        break;
                    }
                }
            }
        }
        return modified;
    }

    /**
     * Workaround save - clear modified flag
     * @return
     */
    public void clearModified(){
        ConfigurationPanel cp = getConfigurationPanel();
        clearModified(cp);
    }

    private void clearModified(JPanel c){
        if(c!=null){

            if(c instanceof EditableComponent){
                    // Clear modified
                    EditableComponent lc = (EditableComponent)c;
                    lc.clearModified();
                }

            for(Component co: c.getComponents()){

                if(co instanceof JPanel){
                    clearModified((JPanel)co);
                }
            }
        }
    }
}
