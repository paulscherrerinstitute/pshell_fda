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

import ch.psi.fda.ui.ce.panels.ListItemProvider;
import ch.psi.fda.model.v1.ScalarDetector;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class DiscreteStepScalarDetectorListItemProvider implements ListItemProvider<ScalarDetector> {

    private List<ScalarDetector> list;

    private final String[] detectors = new String[]{"Scalar Detector"};

    public DiscreteStepScalarDetectorListItemProvider(List<ScalarDetector> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(detectors);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(detectors[0])){
            ScalarDetector sd = new ScalarDetector();
            list.add(sd);
            return(getItem(sd));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(ScalarDetector s: list){
            l.add(getItem(s));
        }
        return l;
    }

    private Component getItem(ScalarDetector object) {
        if(object instanceof ScalarDetector){
            ScalarDetectorPanel p = new ScalarDetectorPanel(object);
            p.setName("Scalar D");
            return(p);
        }

        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof ScalarDetectorPanel){
            list.remove(((ScalarDetectorPanel)component).getObject());
        }
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void moveItemUp(Component component) {
        ListUtil.moveItemUp(list, getObject(component));
    }

    @Override
    public void moveItemDown(Component component) {
        ListUtil.moveItemDown(list, getObject(component));
    }

    private Object getObject(Component component){
        if(component instanceof ScalarDetectorPanel){
            return (((ScalarDetectorPanel)component).getObject());
        }
        return null;
    }
}
