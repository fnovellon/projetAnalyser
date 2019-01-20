package ue.evolRestruct.projetAnalyser.Model.Visitor;

import java.awt.List;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;


public class FieldDeclarationVisitor extends ASTVisitor {
	private ArrayList<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	
	public boolean visit(FieldDeclaration field) {
		fields.add(field);
		return super.visit(field);
	}
	
	public  ArrayList<FieldDeclaration> getFields() {
		return fields;
	}
	
	
}
