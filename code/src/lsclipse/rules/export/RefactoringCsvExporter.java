package lsclipse.rules.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import lsclipse.views.Node;

public abstract class RefactoringCsvExporter extends RefactoringExporter {

	private List<String> refactorings;
	
	public RefactoringCsvExporter() {
		refactorings = new ArrayList<String>();
	}
	
	@Override
	void doExport() {
		try {
			new File(ExportController.getExportDir()).mkdirs();
			PrintWriter pw = new PrintWriter(ExportController.getExportDir() + File.separator + fileName());
			List<String> columns = columnNames();
			for (int i = 0; i < columns.size(); ++i) {
				pw.print(columns.get(i) + ((i == columns.size() - 1) ? "" : ";"));
			}
			pw.println();
			
			for (String line : refactorings) {
				pw.println(line);
			}
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addRefactoring(Node refactoring) {
		List<String> parameters = parameters(refactoring);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < parameters.size(); ++i) {
			sb.append( parameters.get(i) + ((i == parameters.size() - 1) ? "" : ";") );
		}
		String line = sb.toString().replace('#', '.').replace("%", "");
		refactorings.add(line);
	}
	
	
	protected abstract String fileName();
	protected abstract List<String> columnNames();
	protected abstract List<String> parameters(Node refactoring);
	
	
}
