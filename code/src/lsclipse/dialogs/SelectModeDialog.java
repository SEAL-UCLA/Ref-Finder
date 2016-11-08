/* 
*    Ref-Finder
*    Copyright (C) <2015>  <PLSE_UCLA>
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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectModeDialog extends Dialog {
	
	public enum Mode {
		TWO_VER,
		BATCH
	}
	
	private static final String TWO_VERSION_MODE = "2 version mode";
	private static final String BATCH_MODE = "Batch mode";
	
	private Combo comboMode;
	
	private Mode mode;
	
	
	public SelectModeDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public void okPressed() {
		mode = TWO_VERSION_MODE.equals(comboMode.getText())
				? Mode.TWO_VER
				: Mode.BATCH;

		super.okPressed();
	}
	
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("Select mode");

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

		comboMode = new Combo(leftPanel, SWT.READ_ONLY);
		comboMode.setLayoutData(ldtDefault);
		comboMode.add(TWO_VERSION_MODE);
		comboMode.add(BATCH_MODE);
		comboMode.select(0);

		return parent;
	}

	public Mode getMode() {
		return mode;
	}
	
}
