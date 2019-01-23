package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import ue.evolRestruct.projetAnalyser.Model.Visitor.MethodInvocationVisitor;

public class MethodAnalyzer {

	private MethodDeclaration method;
	private CompilationUnit parse;
	private ClassAnalyzer parent;
	
	public MethodAnalyzer(MethodDeclaration method, CompilationUnit parse, ClassAnalyzer parent) {
		this.method = method;
		this.parse = parse;
		this.setParent(parent);
	}
	
	public ArrayList<MethodInvocation> getInvocatedMethods() {
		MethodInvocationVisitor visitor = new MethodInvocationVisitor();
		
		method.accept(visitor);
		
		return visitor.methosInvocated;
	}
	
	public int getNumberOfLines() {
		return (this.parse.getLineNumber(this.method.getStartPosition() + this.method.getLength() - 1) - this.parse.getLineNumber(this.method.getStartPosition())) + 1;
	}
	
	public int getNumberOfParameters() {
		return method.parameters().size();
	}
	
	public MethodDeclaration getNode() {
		return method;
	}
	
	public String getSignature() {
		String completeMethod = this.method.toString();
		final String regex = "(.*)\\{.*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(completeMethod);
		System.out.println("Match : ");
		while (matcher.find()) {
			System.out.println("-"+matcher.group(1)+"-");
        }
		System.out.println("---------------------");
		
		return "";
	}
	
	public String toString() {
		return method.getName().toString() + " from class " + this.getParent().getName();
	}
	
	public String getName() {
		return method.getName().toString();
	}

	public ClassAnalyzer getParent() {
		return parent;
	}

	public void setParent(ClassAnalyzer parent) {
		this.parent = parent;
	}
}
