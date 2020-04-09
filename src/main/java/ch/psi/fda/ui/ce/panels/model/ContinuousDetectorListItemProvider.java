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

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.model.v1.ContinuousDimension;
import ch.psi.fda.ui.ce.panels.ListItemProvider;
import ch.psi.fda.model.v1.Detector;
import ch.psi.fda.model.v1.ScalerChannel;
import ch.psi.fda.model.v1.SimpleScalarDetector;
import ch.psi.fda.model.v1.Timestamp;
import ch.psi.fda.ui.ce.panels.ListUtil;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class ContinuousDetectorListItemProvider implements ListItemProvider<Detector> {

    private final String[] detectors = new String[]{"Scalar Detector", "Scaler", "Timestamp"};

    private ContinuousDimension dimension;

    ContinuousDetectorListItemProvider(ContinuousDimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public String[] getItemKeys() {
        // Only support up to 1 timestamp, 16 scaler channels and 8 detectors
        List<String> keys = new ArrayList<String>();
        if(dimension.getTimestamp()==null){
            keys.add(detectors[2]);
        }
        if(dimension.getScaler().size()<16){
            keys.add(detectors[1]);
        }
        if(dimension.getDetector().size()<100){
            keys.add(detectors[0]);
        }
        return(keys.toArray(new String[keys.size()]));
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(detectors[0])){
            SimpleScalarDetector ssd = new SimpleScalarDetector();
            dimension.getDetector().add(ssd);
            return(getItem(ssd));
        }
        else if(key.equals(detectors[1])){
            ScalerChannel sc = new ScalerChannel();
            dimension.getScaler().add(sc);
            return(getItem(sc));
        }
         else if(key.equals(detectors[2])){
            Timestamp td = new Timestamp();
            dimension.setTimestamp(td);
            return(getItem(td));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();

        for(SimpleScalarDetector sd: dimension.getDetector()){
            l.add(getItem(sd));
        }

        for(ScalerChannel sd: dimension.getScaler()){
            l.add(getItem(sd));
        }

        if(dimension.getTimestamp()!=null){
            l.add(getItem(dimension.getTimestamp()));
        }
        return l;
    }

    private Component getItem(Detector detector){

        if(detector instanceof SimpleScalarDetector){
            SimpleScalarDetectorPanel p = new SimpleScalarDetectorPanel((SimpleScalarDetector)detector);
            p.setName("Scalar Detector");
            return(p);
        }
        else if(detector instanceof ScalerChannel){
            ScalerChannelPanel p = new ScalerChannelPanel((ScalerChannel)detector);
            p.setName("Scaler");
            return(p);
        }
        else if(detector instanceof Timestamp){
            TimestampDetectorPanel p = new TimestampDetectorPanel((Timestamp)detector);
            p.setName("Timestamp");
            return(p);
        }

        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof SimpleScalarDetectorPanel){
            SimpleScalarDetector o = ((SimpleScalarDetectorPanel)component).getObject();
            dimension.getDetector().remove(o);
            ModelUtil.getInstance().findInMappingAndRemove(o);
            ModelUtil.getInstance().refreshAll();
        }
        else if(component instanceof ScalerChannelPanel){
            ScalerChannel o = ((ScalerChannelPanel)component).getObject();
            dimension.getScaler().remove(o);
            ModelUtil.getInstance().findInMappingAndRemove(o);
            ModelUtil.getInstance().refreshAll();
        }
        else if(component instanceof TimestampDetectorPanel){
            Timestamp o = ((TimestampDetectorPanel)component).getObject();
            dimension.setTimestamp(null); // Remove timestamp
            ModelUtil.getInstance().findInMappingAndRemove(o);
            ModelUtil.getInstance().refreshAll();
        }
    }

    @Override
    public boolean isEmpty() {
        boolean a = dimension.getDetector().isEmpty();
        boolean b = dimension.getScaler().isEmpty();
        boolean c = dimension.getTimestamp() == null;
        return  a & b & c;
//        return (getItemKeys().length==0);
    }

    @Override
    public int size() {
        int size = 0;
        size = size+dimension.getDetector().size();
        size = size+dimension.getScaler().size();
        if(dimension.getTimestamp()!= null){
            size = size+1;
        }
        return size;
//        return getItemKeys().length;
    }

    @Override
    public void moveItemUp(Component component) {
        if(component instanceof SimpleScalarDetectorPanel){
            Object a = ((SimpleScalarDetectorPanel)component).getObject();
            ListUtil.moveItemUp(dimension.getDetector(), a);
        }
        else if(component instanceof ScalerChannelPanel){
            Object a=  ((ScalerChannelPanel)component).getObject();
            ListUtil.moveItemUp(dimension.getScaler(), a);
        }
        else if(component instanceof TimestampDetectorPanel){
            // ignore
        }

    }

    @Override
    public void moveItemDown(Component component) {
        if(component instanceof SimpleScalarDetectorPanel){
            Object a = ((SimpleScalarDetectorPanel)component).getObject();
            ListUtil.moveItemDown(dimension.getDetector(), a);
        }
        else if(component instanceof ScalerChannelPanel){
            Object a=  ((ScalerChannelPanel)component).getObject();
            ListUtil.moveItemDown(dimension.getScaler(), a);
        }
        else if(component instanceof TimestampDetectorPanel){
            // ignore
        }
    }
    
}
