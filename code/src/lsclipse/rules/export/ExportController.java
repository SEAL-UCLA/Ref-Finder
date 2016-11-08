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

import java.util.HashMap;
import java.util.Map;

import lsclipse.views.Node;

public class ExportController {
	
	private static String exportDir;

	private static final String RENAME_METHOD_NAME = "Rename method";
	private static final String ADD_PARAMETER = "Add parameter";
	private static final String MOVE_METHOD = "Move method";
	private static final String REMOVE_PARAMETER = "Remove parameter";
	private static final String REPLACE_MAGIC_NUMBER_WITH_CONSTANT = "Replace magic number with constant";
	private static final String INTRODUCE_EXPLAINING_VARIABLE = "Introduce explaining variable";
	private static final String EXTRACT_METHOD = "Extract method";
	private static final String CONSOLIDATE_DUPLICATE_COND_FRAGMENTS = "Consolidate duplicate cond fragments";
	private static final String INLINE_TEMP = "Inline temp";
	private static final String REPLACE_NESTED_COND_WITH_GUARD_CLAUSES = "Replace nested cond guard clauses";
	private static final String INTRODUCE_NULL_OBJECT = "Introduce null object";
	private static final String EXTRACT_INTERFACE = "Extract interface";
	private static final String REMOVE_ASSIGNMENT_TO_PARAMETERS = "Remove assignment to parameters";
	private static final String MOVE_FIELD = "Move field";
	private static final String DECOMPOSE_CONDITIONAL = "Decompose conditional";
	private static final String REMOVE_CONTROL_FLAG = "Remove control flag";
	private static final String EXTRACT_SUPERCLASS = "Extract superclass";
	private static final String CONSOLIDATE_COND_EXPRESSION = "Consolidate cond expression";
	private static final String REPLACE_EXCEPTION_WITH_TEST = "Replace exception with test";
	private static final String INTRODUCE_LOCAL_EXTENSION = "Introduce local extension";
	private static final String UNIMPLEMENTED_EXPORTER = "Unimplemented exporter";
	private static final String REPLACE_METHOD_WITH_METHOD_OBJECT = "Replace method with method object";
	private static final String INLINE_METHOD = "Inline method";
	private static final String INTRODUCE_ASSERTION = "Introduce assertion";
	private static final String HIDE_METHOD = "Hide method";
	private static final String TEASE_APART_INHERITANCE = "Tease apart inheritance";
	private static final String REPLACE_TEMP_WITH_QUERY = "Replace temp with query";

	private Map<String, RefactoringExporter> csvExporters = new HashMap<String, RefactoringExporter>();
	
	
	public ExportController(String exportDir) {
		super();
		ExportController.exportDir = exportDir;
	}
	
	
	public RefactoringExporter getCsvExporter(Node refactoring) {
		if (RENAME_METHOD_NAME.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(RENAME_METHOD_NAME, new RenameMethodExporter());
		} else if (ADD_PARAMETER.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(ADD_PARAMETER, new AddParameterExporter());
		} else if (MOVE_METHOD.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(MOVE_METHOD, new MoveMethodExporter());
		} else if (REMOVE_PARAMETER.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REMOVE_PARAMETER, new RemoveParameterExporter());
		} else if (REPLACE_MAGIC_NUMBER_WITH_CONSTANT.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REPLACE_MAGIC_NUMBER_WITH_CONSTANT, new ReplaceMagicNumberWithConstantExporter());
		} else if (INTRODUCE_EXPLAINING_VARIABLE.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(INTRODUCE_EXPLAINING_VARIABLE, new IntroduceExplainingVariableExporter());
		} else if (EXTRACT_METHOD.equals(refactoring.getName())) { 
			return addIfNotExistsAndGet(EXTRACT_METHOD, new ExtractMethodExporter());
		} else if (CONSOLIDATE_DUPLICATE_COND_FRAGMENTS.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(CONSOLIDATE_DUPLICATE_COND_FRAGMENTS, new ConsolidateDuplicateCondFragmentsExporter());
		} else if (INLINE_TEMP.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(INLINE_TEMP, new InlineTempExporter());
		} else if (REPLACE_NESTED_COND_WITH_GUARD_CLAUSES.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REPLACE_NESTED_COND_WITH_GUARD_CLAUSES, new ReplaceNestedCondWithGuardClausesExporter());
		} else if (INTRODUCE_NULL_OBJECT.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(INTRODUCE_NULL_OBJECT, new IntroduceNullObjectExporter());
		} else if (EXTRACT_INTERFACE.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(EXTRACT_INTERFACE, new ExtractInterfaceExporter());
		} else if (REMOVE_ASSIGNMENT_TO_PARAMETERS.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REMOVE_ASSIGNMENT_TO_PARAMETERS, new RemoveAssignmentToParametersExporter());
		} else if (MOVE_FIELD.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(MOVE_FIELD, new MoveFieldExporter());
		} else if (DECOMPOSE_CONDITIONAL.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(DECOMPOSE_CONDITIONAL, new DecomposeConditionalExporter());
		} else if (REMOVE_CONTROL_FLAG.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REMOVE_CONTROL_FLAG, new RemoveControlFlagExporter());
		} else if (EXTRACT_SUPERCLASS.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(EXTRACT_SUPERCLASS, new ExtractSuperclassExporter());
		} else if (CONSOLIDATE_COND_EXPRESSION.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(CONSOLIDATE_COND_EXPRESSION, new ConsolidateCondExpressionExporter());
		} else if (REPLACE_EXCEPTION_WITH_TEST.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REPLACE_EXCEPTION_WITH_TEST, new ReplaceExceptionWithTestExporter());
		} else if (INTRODUCE_LOCAL_EXTENSION.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(INTRODUCE_LOCAL_EXTENSION, new IntroduceLocalExtensionExporter());
		} else if (REPLACE_METHOD_WITH_METHOD_OBJECT.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REPLACE_METHOD_WITH_METHOD_OBJECT, new ReplaceMethodWithMethodObjectExporter());
		} else if (INLINE_METHOD.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(INLINE_METHOD, new InlineMethodExporter());
		} else if (INTRODUCE_ASSERTION.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(INTRODUCE_ASSERTION, new IntroduceAssertionExporter());
		} else if (HIDE_METHOD.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(HIDE_METHOD, new HideMethodExporter());
		} else if (TEASE_APART_INHERITANCE.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(TEASE_APART_INHERITANCE, new TeaseApartInheritanceExporter());
		} else if (REPLACE_TEMP_WITH_QUERY.equals(refactoring.getName())) {
			return addIfNotExistsAndGet(REPLACE_TEMP_WITH_QUERY, new ReplaceTempWithQueryExporter());
		}
		 
		else {
			return addIfNotExistsAndGet(UNIMPLEMENTED_EXPORTER, new UnimplementedExporter());
		}
	}
	
	
	public void doAllExport() {
		for (RefactoringExporter refactoringExporter : csvExporters.values()) {
			refactoringExporter.doExport();
		}
	}

	private RefactoringExporter addIfNotExistsAndGet(String refactoringName, RefactoringExporter exporter) {
		if (!csvExporters.containsKey(refactoringName)) {
			csvExporters.put(refactoringName, exporter);
		}
		return csvExporters.get(refactoringName);
	}
	
	public static String getExportDir() {
		return exportDir;
	}
	
}
