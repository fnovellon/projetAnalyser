package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodAnalyzer {

	private MethodDeclaration method;
	private CompilationUnit parse;
	private ClassAnalyzer parent;
	
	public MethodAnalyzer(MethodDeclaration method, CompilationUnit parse, ClassAnalyzer parent) {
		this.method = method;
		this.parse = parse;
		this.setParent(parent);
	}
	
	public int getNumberOfLines() {
		return (this.parse.getLineNumber(this.method.getStartPosition() + this.method.getLength() - 1) - this.parse.getLineNumber(this.method.getStartPosition())) + 1;
	}
	
	public int getNumberOfParameters() {
		return method.parameters().size();
	}
	
	public String toString() {
		return method.getName().toString() + " from class " + this.getParent().getName();
	}

	public ClassAnalyzer getParent() {
		return parent;
	}

	public void setParent(ClassAnalyzer parent) {
		this.parent = parent;
	}
}
