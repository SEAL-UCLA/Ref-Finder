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

import java.io.File;

import org.eclipse.core.resources.IProject;

public class GitProjectUpdater extends ProjectUpdater {

	public GitProjectUpdater(IProject project) {
		super(project);
	}

	@Override
	protected String updateCommand(String projectDir, String revision) {
		return "git"
				+ " --git-dir=" + projectDir + File.separatorChar + ".git"
				+ " --work-tree=" + projectDir
				+ " reset --hard " + revision;
	}

}