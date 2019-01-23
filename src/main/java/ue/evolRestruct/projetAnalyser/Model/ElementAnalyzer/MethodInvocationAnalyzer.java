package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodInvocationAnalyzer {

	private MethodAnalyzer methodAnalyzer;
	private MethodInvocation methodInvocation;
	
	public MethodInvocationAnalyzer(MethodAnalyzer methodAnalyzer, MethodInvocation methodInvocation) {
		this.methodAnalyzer = methodAnalyzer;
		this.methodInvocation = methodInvocation;
	}

	public MethodAnalyzer getMethodAnalyzer() {
		return methodAnalyzer;
	}
	
	public ClassAnalyzer getClassAnalyzer() {
		return getMethodAnalyzer().getParent();
	}

	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}

	public void setMethodInvocation(MethodInvocation methodInvocation) {
		this.methodInvocation = methodInvocation;
	}
}
