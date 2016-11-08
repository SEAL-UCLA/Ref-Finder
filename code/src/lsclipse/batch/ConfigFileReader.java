/* 
*    Ref-Finder
*    Copyright (C) <2016> University of Szeged
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
package lsclipse.batch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lsclipse.LsclipseException;

public class ConfigFileReader {

	private String configFile;
	
	public ConfigFileReader(String configFile) {
		this.configFile = configFile;
	}
	
	
	public BatchConfig getBatchConfig(boolean reverse) throws LsclipseException {
		try {
			List<String> revisions = new ArrayList<String>();
			List<String> revisionComments = new ArrayList<String>(); // string after the first comma

			BufferedReader reader = new BufferedReader(new FileReader(configFile));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) continue;
				if (line.isEmpty()) continue;
				
				int firstCommaIndex = line.indexOf(';');

				String revision = line.substring(0, firstCommaIndex);
				String revComment = line.substring(firstCommaIndex+1, line.length());

				revisions.add(revision);
				revisionComments.add(revComment);
			}
			reader.close();

			return reverse ? new ReverseBatchConfig(revisions, revisionComments) : new BatchConfig(revisions, revisionComments);

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new LsclipseException("Problem with revision config file", ex);
		}
	}
	
	
}
