package ue.evolRestruct.projetAnalyser.Model.Visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationVisitor extends ASTVisitor {
	private ArrayList<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	private int linesCount = 0;
	private CompilationUnit parse;
	
	public TypeDeclarationVisitor(CompilationUnit parse) {
		this.parse = parse;
	}
	
	public boolean visit(TypeDeclaration node) {
		//System.out.println(node.getName().toString());
		if( !node.isInterface() ) {
			types.add(node);
			linesCount = parse.getLineNumber(node.getStartPosition() + node.getLength()) - parse.getLineNumber(node.getStartPosition());
		}
		return super.visit(node);
	}
	
	public ArrayList<TypeDeclaration> getTypes() {
		return types;
	}
	
	public int getLinesCount() {
		return this.linesCount;
	}
	
}
