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
import java.io.IOException;
import java.io.InputStreamReader;

import lsclipse.LsclipseException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public abstract class ProjectUpdater {
	
	private IProject project;

	public ProjectUpdater(IProject project) {
		super();
		this.project = project;
	}
	
	
	public IProject updateToRevision(String revision) throws LsclipseException {
		String projectDir = project.getLocation().toFile().getAbsolutePath();
		
		try {
			Process p = Runtime.getRuntime().exec(updateCommand(projectDir, revision));
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			while((line = in.readLine()) != null) {
			    System.out.println(line);
			}
			
			p.waitFor();
			
			// rename project 
//			IProjectDescription description = project.getDescription();
//			String oldName = description.getName();
//			String newName = oldName.substring(0, oldName.indexOf("_")+1);
//			newName += revision;
//			description.setName(newName);
//			project.move(description, false, null);
			
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			return ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName());
		} catch (IOException e) {
			e.printStackTrace();
			throw new LsclipseException("Error while executing update!", e);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new LsclipseException("Error while executing update!", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new LsclipseException("Error while executing update!", e);
		}
		
	}
	
	
	protected abstract String updateCommand(String projectDir, String revision);
	
}
