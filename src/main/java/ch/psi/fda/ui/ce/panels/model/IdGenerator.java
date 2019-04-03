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

/**
 *
 * @author ebner
 */
public class IdGenerator {
    public static String generateId(){
        long millis = System.currentTimeMillis();
        int digits = 1000000;
        String id = String.format("id%06.0f", (millis - (Math.floor(millis / digits) * digits)));
        return(id);
    }

}
