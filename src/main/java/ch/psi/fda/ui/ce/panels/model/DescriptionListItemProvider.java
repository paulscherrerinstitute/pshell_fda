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
import ch.psi.fda.model.v1.Configuration;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class DescriptionListItemProvider implements ListItemProvider<String> {

    private final String[] items = new String[]{"Description"};

    private Configuration configuration;

    public DescriptionListItemProvider(Configuration c){
        this.configuration = c;
    }

    @Override
    public String[] getItemKeys() {

        // If no continuous dimension is specified return its key. Otherwise return no key
        // (Ensures that only one continuous dimension can be added)
        if(configuration.getDescription()==null){
            return(items);
        }
        else{
            return new String[] {};
        }
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(items[0])){
            return(getItem(configuration));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        if(configuration.getDescription()!=null){
            l.add(getItem(configuration));
        }
        return l;
    }

    private Component getItem(Configuration object) {
        if(object instanceof Configuration){
            DescriptionPanel p = new DescriptionPanel(object);
            p.setName("");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof DescriptionPanel){
            configuration.setDescription(null); // Remove description
        }
    }

    @Override
    public boolean isEmpty() {
        return (configuration.getDescription()==null);
    }

    @Override
    public int size() {
        if(configuration.getDescription()==null){
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
