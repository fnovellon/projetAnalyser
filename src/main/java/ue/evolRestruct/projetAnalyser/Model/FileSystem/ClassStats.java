package ue.evolRestruct.projetAnalyser.Model.FileSystem;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassStats implements FileSystemStats{
	
	private String name;
	private ArrayList<TypeDeclaration> typeDeclarationArray;
	private ArrayList<MethodDeclaration> methodDeclarationArray;
	private ArrayList<FieldDeclaration> fieldDeclarationArray;
	
	public ClassStats(String name) {
		this.name = name;
		this.typeDeclarationArray = new ArrayList();
		this.methodDeclarationArray = new ArrayList();
		this.fieldDeclarationArray = new ArrayList();
		
	}

	@Override
	public int getNumberOfLines() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<TypeDeclaration> getTypeDeclaration() {
		return this.typeDeclarationArray;
	}

	@Override
	public ArrayList<MethodDeclaration> getMethodDeclaration() {
		return this.methodDeclarationArray;
	}

	@Override
	public ArrayList<FieldDeclaration> getFieldDeclaration() {
		return this.fieldDeclarationArray;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString(int tabNb) {
		return getName() + "\n";
	}

	public void setTypeDeclarationArray(ArrayList<TypeDeclaration> typeDeclarationArray) {
		this.typeDeclarationArray = typeDeclarationArray;
	}
	
	public void setFieldDeclarationArray(ArrayList<FieldDeclaration> fieldDeclarationArray) {
		this.fieldDeclarationArray = fieldDeclarationArray;
	}

	public void setMethodDeclarationArray(ArrayList<MethodDeclaration> methodDeclarationArray) {
		this.methodDeclarationArray = methodDeclarationArray;
	}
}
