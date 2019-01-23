package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.MethodAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.MethodInvocationAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.GraphAnalyzer.GraphPair;

public class GraphAnalyzer {

		
	public GraphAnalyzer() {		
			
	}

	
	private static ClassAnalyzer findClass(ArrayList<ClassAnalyzer> list, String name) {
		for(ClassAnalyzer currentClass : list) {
			if( currentClass.getSimpleName().equals(name) ) {
				return currentClass;
			}
		}
		
		return null;
	}
	
	private static String resolveClassName(MethodInvocation methodInvocation) {
		String className = null;
		
		IMethodBinding m = methodInvocation.resolveMethodBinding();		
		if(m == null) {
			if(methodInvocation.getExpression() == null) return null;
			ITypeBinding t = methodInvocation.getExpression().resolveTypeBinding();
			if(t == null) {
				//System.err.println("UNRESOLVABLE : " + methodInvocation.toString());
				return null;
			}
			className = t.getName().toString();
			//className = t.getQualifiedName();
		}
		else {
			className = m.getDeclaringClass().getName().toString();
			//className = m.getDeclaringClass().getQualifiedName();
		}
		
		return className;
	}
	
	private static Graph createEmptyGraph() {
		System.setProperty("org.graphstream.ui.renderer",
		        "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("CalledMethodsGraph");
        graph.addAttribute("ui.stylesheet", style);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        return graph;
	}
	
	public static Graph buildGeneralGraph(PackageAnalyzer packageAnalyzer) {
		Graph graph = createEmptyGraph();
        
        ArrayList<ClassAnalyzer> listOfClasses = StatisticsAnalyzer.getAllClasses(packageAnalyzer);
        for(MethodInvocationAnalyzer methodInvocationAnalyzer : StatisticsAnalyzer.getAllMethodInvocation(packageAnalyzer)) {

        	String className = projectClassMethodCall(listOfClasses, methodInvocationAnalyzer);
        	if(className == null) continue;

        	ClassAnalyzer currentClass = methodInvocationAnalyzer.getClassAnalyzer();
        	MethodAnalyzer currentMethod = methodInvocationAnalyzer.getMethodAnalyzer();
        	MethodInvocation methodInvocation = methodInvocationAnalyzer.getMethodInvocation();
        	
			graph.addEdge(
					currentClass.getQualifiedName() + "." + currentMethod.getName().toString() + " ---> " + className + "." + methodInvocation.getName().toString(), 
					currentClass.getSimpleName(), 
					className,
					true);
        }

		
		for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
		
		for (Edge edge : graph.getEdgeSet()) {
			//edge.addAttribute("ui.label", edge.getId());
        }	
		
		return graph;
	}
	
	private static String projectClassMethodCall(ArrayList<ClassAnalyzer> listOfClasses, MethodInvocationAnalyzer methodInvocationAnalyzer) {
		ClassAnalyzer currentClass = methodInvocationAnalyzer.getClassAnalyzer();
    	MethodAnalyzer currentMethod = methodInvocationAnalyzer.getMethodAnalyzer();
    	MethodInvocation methodInvocation = methodInvocationAnalyzer.getMethodInvocation();
    	
    	String className = resolveClassName(methodInvocation);
		if(className == null) {
			return null;
		}
		
		ClassAnalyzer findClass = findClass(listOfClasses, className);
		if( findClass == null ) {
			//System.err.println("NOT FOUND : " + className + " ::: " + methodInvocation.getName());
			return null;
		}
		
		if( findClass.getSimpleName().equals(currentClass.getSimpleName()) ) {
			return null;
		}
		
		return className;
	}
	
	public static Graph buildPonderalGraph(PairArray pairArray) {
		Graph graph = createEmptyGraph();
		double sum = 0;
		for (GraphPair pair : pairArray.getArray()) {
			sum += pair.getValue();
		}
		
		Integer id = 0;
		for (GraphPair pair : pairArray.getArray()) {
			double moyenne =  (((double) pair.getValue()) / sum);
			
			graph.addEdge(
					id.toString(), 
					pair.getP1(), 
					pair.getP2());
			graph.getEdge(id.toString()).addAttribute("ui.label", moyenne);
			
			id++;
		}
		
		for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
		
		return graph;
	}
	
	public static Graph buildPonderalGraph(PackageAnalyzer packageAnalyzer) {
		PairArray pairArray = new PairArray();
		
		ArrayList<ClassAnalyzer> listOfClasses = StatisticsAnalyzer.getAllClasses(packageAnalyzer);
		
		for(MethodInvocationAnalyzer methodInvocationAnalyzer : StatisticsAnalyzer.getAllMethodInvocation(packageAnalyzer)){
			String className = projectClassMethodCall(listOfClasses, methodInvocationAnalyzer);
        	if(className == null) continue;

        	ClassAnalyzer currentClass = methodInvocationAnalyzer.getClassAnalyzer();
        	MethodAnalyzer currentMethod = methodInvocationAnalyzer.getMethodAnalyzer();
        	MethodInvocation methodInvocation = methodInvocationAnalyzer.getMethodInvocation();
        	
        	pairArray.addPair(currentClass.getSimpleName(), className);
		}
		
		return buildPonderalGraph(pairArray);
	}

	
	

    public static void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
            sleep();
        }
    }

    private static void sleep() {
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    private static String style = 
    		"node {" + 
    		"	size: 10px, 10px;" + 
    		"	shape: box;" + 
    		"	fill-color: red;" + 
    		"	stroke-mode: plain;" + 
    		"	stroke-color: red;" + 
    		"   z-index: 2;" +
    		"   text-background-mode: plain;" +
    		"   text-offset: 10px;"+
    		"}" +
    		"edge {" + 
    		"	z-index: 1;" +	
    		"   text-background-mode: plain;" +
    		"   text-offset: 20px;"+
    		"}";
    
    public static class PairArray {
    	private ArrayList<GraphPair> array = new ArrayList<GraphPair>();
    	
    	public PairArray addPair(String s1, String s2) {
    		GraphPair pair = new GraphPair(s1, s2);
    		for (GraphPair graphPair : array) {
				if(graphPair.equals(pair)) {
					graphPair.setValue(graphPair.getValue()+1);
					return this;
				}
			}
    		array.add(pair);
    		return this;
    	}
    	
    	public String toString() {
    		String buffer = "";
    		for (GraphPair graphPair : array) {
    			buffer += "<" + graphPair.getP1() + ", " + graphPair.getP2() + "> : " + graphPair.getValue() + "\n";
			}
    		return buffer;
    	}

		public ArrayList<GraphPair> getArray() {
			return array;
		}
    }
    
    
    public static class GraphPair{
    	private String p1;
    	private String p2;
    	private Integer value;
    	
    	public GraphPair(String s1, String s2) {
    		this.p1 = s1;
    		this.p2 = s2;
    		this.value = 1;
    	}
    	
    	
    	public boolean equals(GraphPair pair) {
    		Boolean b1 = (this.p1.equals(pair.getP1()) && this.p2.equals(pair.getP2()));
    		Boolean b2 = (this.p1.equals(pair.getP2()) && this.p2.equals(pair.getP1()));
    		return (b1 || b2);
    	}


		public String getP1() {
			return p1;
		}


		public void setP1(String p1) {
			this.p1 = p1;
		}


		public String getP2() {
			return p2;
		}


		public void setP2(String p2) {
			this.p2 = p2;
		}


		public Integer getValue() {
			return value;
		}


		public void setValue(Integer value) {
			this.value = value;
		}
    }

}
