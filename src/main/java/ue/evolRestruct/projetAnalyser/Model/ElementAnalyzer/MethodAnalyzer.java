package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodAnalyzer {

	private MethodDeclaration method;
	private CompilationUnit parse;
	
	public MethodAnalyzer(MethodDeclaration method, CompilationUnit parse) {
		this.method = method;
		this.parse = parse;
	}
	
	public int getNumberOfLines() {
		return (this.parse.getLineNumber(this.method.getStartPosition() + this.method.getLength() - 1) - this.parse.getLineNumber(this.method.getStartPosition())) + 1;
	}
	
	public String toString() {
		return method.getName().toString() + " : " + getNumberOfLines();
	}
}
