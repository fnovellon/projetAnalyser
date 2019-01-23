package ue.evolRestruct.projetAnalyser.Model.Visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodInvocationVisitor extends ASTVisitor {
	
	public ArrayList<MethodInvocation> methosInvocated = new ArrayList<MethodInvocation>();
	
	public boolean visit(MethodInvocation node) {

		this.methosInvocated.add(node);
		
		return super.visit(node);
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

}
