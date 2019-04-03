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

import java.text.DecimalFormat;
import java.text.Format;


/**
 * Utility class for text fields
 * @author ebner
 */
public class FieldUtilities {

    /**
     * Get a properly formated DecimalFormat object
     * @return
     */
    public static Format getDecimalFormat(){
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(20);
        format.setGroupingUsed(false);
        return format;
    }

    public static Format getIntegerFormat(){
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(0);
        format.setGroupingUsed(false);
        return format;
    }

}
