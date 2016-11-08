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
package lsclipse.dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BatchDialog extends Dialog {

	public enum VersionControlSystem {
		SVN,
		GIT
	}
	
	private VersionControlSystem vcs;
	private String proj1;
	private String proj2;
	private String configFile;
	private String exportDir;
	private boolean reverseRevisions;

	
	private Combo vcsCombo;
	private Combo initialBaseProject;
	private Combo initialChangedProject;
	private Text configFileText;
	private Text exportDirText;
	private Button reverseRevsButton;
	
	public BatchDialog(Shell parentShell) {
		super(parentShell);
	}

	
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("Select Versions");
		
		setShellStyle(getShellStyle() | SWT.RESIZE); 

		// overall layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);

		// declare some layouts
		GridData ldtDefault = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		ldtDefault.grabExcessHorizontalSpace = true;
		ldtDefault.grabExcessVerticalSpace = true;
		ldtDefault.horizontalAlignment = GridData.FILL;
		ldtDefault.verticalAlignment = GridData.FILL;
		ldtDefault.exclude = false;

		GridLayout panelLayout = new GridLayout();
		panelLayout.numColumns = 1;

		Composite leftPanel = new Composite(parent, 0);
		leftPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL | GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL));
		leftPanel.setLayout(panelLayout);

		
		// Version control system
		Label versionControl = new Label(leftPanel, 0);
		versionControl.setText("Select verison control system:");
		vcsCombo = new Combo(leftPanel, SWT.READ_ONLY);
		vcsCombo.setLayoutData(ldtDefault);
		vcsCombo.add("svn");
		vcsCombo.add("git");

		// initial projects
		Label baseVersion = new Label(leftPanel, 0);
		baseVersion.setText("Initial Base Version:");

		initialBaseProject = new Combo(leftPanel, SWT.READ_ONLY);
		initialBaseProject.setLayoutData(ldtDefault);

		Label changedVersion = new Label(leftPanel, 0);
		changedVersion.setText("Initial Changed Version:");

		initialChangedProject = new Combo(leftPanel, SWT.READ_ONLY);
		initialChangedProject.setLayoutData(ldtDefault);

		// Populate the combo boxes
		for (IProject proj : ResourcesPlugin.getWorkspace().getRoot()
				.getProjects()) {
			initialChangedProject.add(proj.getName());
			initialBaseProject.add(proj.getName());
		}
		
		
		
		// config file
		Label config = new Label(leftPanel, 0);
		config.setText("Config file:");
		configFileText = new Text(leftPanel, 0);
		configFileText.setLayoutData(ldtDefault);
		
		Button buttonSelectDir = new Button(leftPanel, SWT.PUSH);

		buttonSelectDir.setText("Select config file...");
		buttonSelectDir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				FileDialog fileDialog = new FileDialog(BatchDialog.this.getShell());

				fileDialog.setText("Please select the config file and click OK");

				String file = fileDialog.open();
				if(file != null) {
					configFileText.setText(file);
					configFileText.pack();
				}
			}
		});
		
		// reverse revisions
		reverseRevsButton = new Button (leftPanel, SWT.CHECK);
		reverseRevsButton.setText("Reverse revisions (newer ahead of older)");
		reverseRevsButton.setSelection(false);
		
		
		// export dir
		Label export = new Label(leftPanel, 0);
		export.setText("Export dir:");
		exportDirText = new Text(leftPanel, 0);
		exportDirText.setLayoutData(ldtDefault);
		
		buttonSelectDir = new Button(leftPanel, SWT.PUSH);
		buttonSelectDir.setText("Select export directory...");
		buttonSelectDir.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				DirectoryDialog directoryDialog = new DirectoryDialog(BatchDialog.this.getShell());
				directoryDialog.setMessage("Please select a directory and click OK");
				String dir = directoryDialog.open();
				if(dir != null) {
					exportDirText.setText(dir);
					exportDirText.pack();
				}
			}
		});
		
		return parent;
	}

	
	public void okPressed() {
		vcs = vcsCombo.getText().equals("svn")
				? VersionControlSystem.SVN
				: VersionControlSystem.GIT;
		proj1 = initialBaseProject.getText();
		proj2 = initialChangedProject.getText();
		configFile = configFileText.getText();
		exportDir = exportDirText.getText();
		reverseRevisions = reverseRevsButton.getSelection();

		super.okPressed();
	}


	public VersionControlSystem getVersionControlSystem() {
		return vcs;
	}


	public String getConfigFile() {
		return configFile;
	}

	public String getExportDir() {
		return exportDir;
	}


	public String getProj1() {
		return proj1;
	}


	public void setProj1(String proj1) {
		this.proj1 = proj1;
	}


	public String getProj2() {
		return proj2;
	}


	public void setProj2(String proj2) {
		this.proj2 = proj2;
	}


	public boolean getReverseRevisions() {
		return reverseRevisions;
	}

	
}
