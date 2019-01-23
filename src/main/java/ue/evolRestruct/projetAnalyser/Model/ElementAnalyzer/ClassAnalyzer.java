package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassAnalyzer implements ElementAnalyzer {
	
	private TypeDeclaration node;
	private CompilationUnit parse;
	private PackageAnalyzer parent;
	
	private ArrayList<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private ArrayList<MethodAnalyzer> methods = new ArrayList<MethodAnalyzer>(); 
	
	
	public ClassAnalyzer(TypeDeclaration node, CompilationUnit parse, PackageAnalyzer parent) {
		this.node = node;
		this.parse = parse;
		this.setParent(parent);
		
		for(FieldDeclaration field : node.getFields()) {
			fields.add(field);
		}
		for(MethodDeclaration method : node.getMethods()) {
			methods.add(new MethodAnalyzer(method, parse, this));
		}
	}


	@Override
	public int getNumberOfLines() {
		int sum = 2;
		for(MethodAnalyzer method : methods) {
			sum += method.getNumberOfLines();
		}
		sum += fields.size();

		return sum;
	}

	@Override
	public String getName() {
		return this.getTypeDeclarationName(node);
	}
	
	public String getSimpleName() {
		return node.getName().toString();
	}
	
	public String getQualifiedName() {
		String typeDeclarationName = this.getTypeDeclarationName(node);
		String typeDeclarationNameWithoutEnd = typeDeclarationName.substring(0, typeDeclarationName.length()-6);
		
		
		return this.getParent().getName()+"."+typeDeclarationNameWithoutEnd;
	}
	
	@Override
	public String toString(int tabNb) {
		String buffer = "";
		buffer = this.toString() + "\n";
		
		return buffer;
	}
	
	public String toString() {
		return getName() + " in package " + this.getParent().getName();
		/*
		 		for(FieldDeclaration fd : fields) {
			for(int i = 0; i < tabNb+1; i++) {
				buffer += "\t";
			}
			buffer += fd.toString().replace("\n", "");
			buffer += "\n";
		}
		for(MethodAnalyzer ma : methods) {
			for(int i = 0; i < tabNb+1; i++) {
				buffer += "\t";
			}
			buffer += ma.toString().replace("\n", "");
			buffer += "\n";
		} 
		 
		 */
	}
	
	
	
	
	private String getTypeDeclarationName(TypeDeclaration node) {
		String className = node.getName().toString() + ".class";
		TypeDeclaration top = getOuterClass(node);
		while ( top != null ) {
			className = top.getName() + "." + className;
			top = getOuterClass(top);
		}
		return className;
	}
	
	private static TypeDeclaration getOuterClass(ASTNode node) {
		do {
			node= node.getParent();
		} while (node != null && node.getNodeType() != ASTNode.TYPE_DECLARATION);

		return (TypeDeclaration) node;
	}


	public ArrayList<FieldDeclaration> getFields() {
		return fields;
	}


	public void setFields(ArrayList<FieldDeclaration> fields) {
		this.fields = fields;
	}


	public ArrayList<MethodAnalyzer> getMethods() {
		return methods;
	}


	public void setMethods(ArrayList<MethodAnalyzer> methods) {
		this.methods = methods;
	}


	public PackageAnalyzer getParent() {
		return parent;
	}


	public void setParent(PackageAnalyzer parent) {
		this.parent = parent;
	}


}
