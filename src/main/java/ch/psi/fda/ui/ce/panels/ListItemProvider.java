/*
 *  Copyright (C) 2010 Paul Scherrer Institute
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

package ch.psi.fda.ui.ce.panels;

import java.awt.Component;
import java.util.List;

/**
 * Interface defining the functionality of a ListElement provider
 * @author ebner
 */
public interface ListItemProvider<T> {

    /**
     * Get list of keys of possible components this provider can create
     * @return
     */
    public String[] getItemKeys();

    /**
     * Get a new list item instance for the given key
     * @param key   Key of the component to be created
     * @return  New component for the given key. Returns null if there is no matching
     *          component for the key.
     */
    public Component newItem(String key);


    /**
     * Remove an item from the list
     * @param component
     */
    public void removeItem(Component component);

    /**
     * Get a new list item that is initialized with the given object
     * @param object
     * @return  New GUI component for the passed object
     */
//    public Component getItem(T object);


    /**
     * Get currently managed items
     * @return List of items
     */
    public List<Component> getItems();

    public boolean isEmpty();
    public int size();

    public void moveItemUp(Component component);
    public void moveItemDown(Component component);

}
