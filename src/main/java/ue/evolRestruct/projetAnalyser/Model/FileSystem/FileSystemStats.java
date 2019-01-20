package ue.evolRestruct.projetAnalyser.Model.FileSystem;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public interface FileSystemStats {
	
	public String getName();
	
	public int getNumberOfLines();
	public ArrayList<TypeDeclaration> getTypeDeclaration();
	public ArrayList<MethodDeclaration> getMethodDeclaration();
	public ArrayList<FieldDeclaration> getFieldDeclaration();
	public default Boolean isPackage() {
		return false;
	}
	public String toString();
	public String toString(int tabNb);
	
	
}
