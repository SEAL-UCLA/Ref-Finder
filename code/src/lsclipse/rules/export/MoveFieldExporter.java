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
package lsclipse.rules.export;

import java.util.ArrayList;
import java.util.List;

import lsclipse.LSDiffRunner;
import lsclipse.views.Node;

public class MoveFieldExporter extends RefactoringCsvExporter {

	MoveFieldExporter() {}
	
	@Override
	protected String fileName() {
		return "MOVE_FIELD.csv";
	}

	@Override
	protected List<String> columnNames() {
		List<String> columns = new ArrayList<String>();
		columns.add("field in old");
		columns.add("field in new");
		return columns;
	}

	@Override
	protected List<String> parameters(Node refactoring) {
		List<String> paramsWithPosition = new ArrayList<String>();
		String[] parameters = refactoring.params.substring(2, refactoring.params.length() - 2).split("\",\"");
		
		String fieldInOld = parameters[1] + "#" + parameters[0];
		String fieldInNew = parameters[2] + "#" + parameters[0];
		
		paramsWithPosition.add(fieldInOld + "@" + LSDiffRunner.beforePositions.getPosition(fieldInOld).toString());
		paramsWithPosition.add(fieldInNew + "@" + LSDiffRunner.afterPositions.getPosition(fieldInNew).toString());
		
		return paramsWithPosition;
	}

}
