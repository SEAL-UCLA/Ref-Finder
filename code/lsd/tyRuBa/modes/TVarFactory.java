/* 
*    Ref-Finder
*    Copyright (C) <2015>  <PLSE_UCLA>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
 * Created on May 21, 2003
 *
 */
package tyRuBa.modes;

import java.util.HashMap;

/**
 * A TVarFactory creates TVar and makes sure that the same TVar is not created twice
 */
public class TVarFactory {
	
	private HashMap hm = new HashMap();

	public TVar makeTVar(String name) {
		TVar result = (TVar) hm.get(name);
		if (result == null) {
			result = Factory.makeTVar(name);
			hm.put(name, result);
		}
		return result;
	}
	
}
