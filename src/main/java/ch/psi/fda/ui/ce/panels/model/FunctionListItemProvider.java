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
import ch.psi.fda.model.v1.Function;
import ch.psi.fda.model.v1.Region;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class FunctionListItemProvider implements ListItemProvider<Function> {

    private final String[] items = new String[]{"Function"};

    private Region region;

    public FunctionListItemProvider(Region region){
        this.region = region;
    }

    @Override
    public String[] getItemKeys() {

        // If no continuous dimension is specified return its key. Otherwise return no key
        // (Ensures that only one continuous dimension can be added)
        if(region.getFunction()==null){
            return(items);
        }
        else{
            return new String[] {};
        }
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(items[0])){
            Function d = new Function();
            region.setFunction(d);
            return(getItem(d));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        if(region.getFunction()!=null){
            l.add(getItem(region.getFunction()));
        }
        return l;
    }

    private Component getItem(Function object) {
        if(object instanceof Function){
            FunctionPanel p = new FunctionPanel(object);
            p.setName("");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof FunctionPanel){
            region.setFunction(null); // Remove function from region
        }
    }

    @Override
    public boolean isEmpty() {
        return (region.getFunction()==null);
    }

    @Override
    public int size() {
        if(region.getFunction()==null){
            return 0;
        }
        return 1;
    }

    @Override
    public void moveItemUp(Component component) {
        // Not supported as there is only one continuous dimension
    }

    @Override
    public void moveItemDown(Component component) {
        // Not supported as there is only one continuous dimension
    }
}
