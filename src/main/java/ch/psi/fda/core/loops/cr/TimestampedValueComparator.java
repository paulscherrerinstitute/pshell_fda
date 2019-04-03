/**
 * 
 * Copyright 2010 Paul Scherrer Institute. All rights reserved.
 * 
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This code is distributed in the hope that it will be useful,
 * but without any warranty; without even the implied warranty of
 * merchantability or fitness for a particular purpose. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this code. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.psi.fda.core.loops.cr;

import java.util.Comparator;

/**
 * Comparator for comparint 2 timestamped values
 */
public class TimestampedValueComparator implements Comparator<TimestampedValue> {

	@Override
	public int compare(TimestampedValue o1, TimestampedValue o2) {
		if (o1.getTimestamp() < o2.getTimestamp()) {
			return -1;
		} else if (o1.getTimestamp() > o2.getTimestamp()) {
			return 1;
		} else if (o1.getTimestamp() == o2.getTimestamp()) {
			if (o1.getNanosecondsOffset() < o2.getNanosecondsOffset()) {
				return -1;
			} else if (o1.getNanosecondsOffset() > o2.getNanosecondsOffset()) {
				return 1;
			}
		}
		return 0;
	}
}
