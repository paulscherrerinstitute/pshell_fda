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

package ch.psi.fda.ui.ce.panels;

/**
 * Metadata of a component. The metadata consists of properties of a component like
 * whether the component is optional, its default value, ...
 * 
 * @author ebner
 */
public class ComponentMetadata {

    private final boolean mandatory;
    private final String defaultValue;


    public ComponentMetadata(boolean mandatory){
        this(mandatory, "");
    }

    public ComponentMetadata(boolean mandatory, String defaultValue){
        this.mandatory = mandatory;
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isMandatory() {
        return mandatory;
    }

}
