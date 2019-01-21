package ue.evolRestruct.projetAnalyser.Model.Visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;

public class TypeDeclarationVisitor extends ASTVisitor {
	public ArrayList<ClassAnalyzer> classList = new ArrayList<ClassAnalyzer>();
	private CompilationUnit parse;

	public TypeDeclarationVisitor(CompilationUnit parse) {
		this.parse = parse;
	}

	public boolean visit(TypeDeclaration node) {
		ClassAnalyzer nClass = new ClassAnalyzer(node, parse);

		this.classList.add(nClass);
		
		return super.visit(node);
	}

}
