package ue.evolRestruct.projetAnalyser.Model.Visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;

public class TypeDeclarationVisitor extends ASTVisitor {
	public ArrayList<ClassAnalyzer> classList = new ArrayList<ClassAnalyzer>();
	private CompilationUnit parse;
	private PackageAnalyzer currentPackage;

	public TypeDeclarationVisitor(CompilationUnit parse, PackageAnalyzer currentPackage) {
		this.parse = parse;
		this.currentPackage = currentPackage;
	}

	public boolean visit(TypeDeclaration node) {
		ClassAnalyzer nClass = new ClassAnalyzer(node, parse, currentPackage);

		this.classList.add(nClass);
		
		return super.visit(node);
	}

}
