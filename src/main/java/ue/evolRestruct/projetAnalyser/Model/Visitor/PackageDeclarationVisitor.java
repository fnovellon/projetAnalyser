package ue.evolRestruct.projetAnalyser.Model.Visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

public class PackageDeclarationVisitor extends ASTVisitor {
	private PackageDeclaration nodes;
	
	public boolean visit(PackageDeclaration pack) {
		this.nodes = pack;
		return super.visit(pack);
	}
	
	public PackageDeclaration getPackages() {
		return nodes;
	}
	
}