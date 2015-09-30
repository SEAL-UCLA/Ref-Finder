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
 * Created on Jul 26, 2004
 */
package tyRuBa.util.pager;

import java.io.File;
import java.net.URL;

/**
 * A "base" location. This is a factory for making resourceIDs (relative to that location).
 * @category FactBase
 * @author riecken
 */
public abstract class Location {

    /**
     * Creates a resourceId for the given path relative to the base.
     */
    public abstract Pager.ResourceId getResourceID(String relativeID);
    
    /** Make a URLLocation. */
    public Location make(final URL theBaseURL) {
        return new URLLocation(theBaseURL);
    }

    /** Make a FileLocation. */
    public Location make(final File theBasePath) {
        return new FileLocation(theBasePath);
    }

}
