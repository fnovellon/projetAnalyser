package ue.evolRestruct.projetAnalyser.Model.Visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
	private ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private int linesCount = 0;
	private CompilationUnit parse;
	
	public MethodDeclarationVisitor(CompilationUnit parse) {
		this.parse = parse;
	}
	
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		linesCount = parse.getLineNumber(node.getStartPosition() + node.getLength()) - parse.getLineNumber(node.getStartPosition());
		return super.visit(node);
	}
	

	
	public ArrayList<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public int getLinesCount() {
		return this.linesCount;
	}
}
