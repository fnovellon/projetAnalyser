package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

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

import ue.evolRestruct.projetAnalyser.Model.DendroPackage.DendroPaint;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.MethodAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.MethodInvocationAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.GraphAnalyzer.GraphPair;

public class GraphAnalyzer {

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
		return buildPonderalGraph(createPairArray(packageAnalyzer));
	}
	
	
	public static PairArray createPairArray(PackageAnalyzer packageAnalyzer){
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
		
		return pairArray;
	}	
	
	public static void buildDendogramme(PackageAnalyzer packageAnalyzer) {
		buildDendogramme(createPairArray(packageAnalyzer));
	}
	
	public static void buildDendogramme(PairArray pairArray) {
		Graph graph = buildPonderalGraph(pairArray);	
		DendroAnalyzer dendro = new DendroAnalyzer();
		Set<String> set = pairArray.getSetOfNodeString();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()){
		  String element = (String) iterator.next();
		  dendro.add(element);
		}
		
		
		while(!pairArray.isEmpty()) {
			GraphPair maxPair = pairArray.higherPair();
			String nNode = "(" + maxPair.getP1() + " + " + maxPair.getP2() + ")";
			dendro.merge(maxPair.getP1(), maxPair.getP2());
//			System.out.println(nNode);
			
			ArrayList<GraphPair> list = pairArray.nearFrom(maxPair.getP1(), maxPair.getP2());
			
			for (GraphPair pair : list) {
				
	            if(pair.getP1().equals(maxPair.getP1()) || pair.getP1().equals(maxPair.getP2())) {
	            	pairArray.addPair(new GraphPair(nNode, pair.getP2(), pair.value));
	            }
	            else if(pair.getP2().equals(maxPair.getP1()) || pair.getP2().equals(maxPair.getP2())) {
	            	pairArray.addPair(new GraphPair(pair.getP1(), nNode, pair.value));
	            }
	            
	            
	            pairArray.removePair(pair);
			}

	        
	        pairArray.removePair(maxPair);

		}
		
		dendro.buildDendro();
		
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


    
    public static class PairArray {
    	public ArrayList<GraphPair> array = new ArrayList<GraphPair>();
    	
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
    	
    	public PairArray addPair(GraphPair p) {
    		for (GraphPair graphPair : array) {
				if(graphPair.equals(p)) {
					graphPair.setValue(graphPair.getValue()+p.getValue());
					return this;
				}
			}
    		array.add(p);
    		return this;
    	}

    	public ArrayList<GraphPair> nearFrom(String s1, String s2){
    		ArrayList<GraphPair> a = new ArrayList<GraphPair>();
    		
    		for (GraphPair pair : this.array) {
				Boolean matchS1 = (pair.getP1().equals(s1) || pair.getP2().equals(s1));
				Boolean matchS2 = (pair.getP1().equals(s2) || pair.getP2().equals(s2));
				if(matchS1 ^ matchS2) {
					a.add(pair);
				}
			}
    		
    		return a;    		
    	}
    	
    	public GraphPair find(String s1, String s2) {
    		GraphPair pair = new GraphPair(s1, s2);
    		for (GraphPair graphPair : array) {
				if(graphPair.equals(pair)) {
					return graphPair;
				}
			}
    		return null;
    	}
    	
    	public String toString() {
    		String buffer = "";
    		for (GraphPair pair : array) {
    			buffer += pair + "\n";
			}
    		return buffer;
    	}

		public ArrayList<GraphPair> getArray() {
			return array;
		}
		
		public Boolean isEmpty() {
			return this.array.isEmpty();
		}
		
		public GraphPair higherPair() {
			GraphPair max = null;
			for (GraphPair pair : array) {
				if(max == null || (pair.getValue() > max.getValue()) ) {
					max = pair;
				}
			}
			return max;
		}
		
		public PairArray removePair(GraphPair pair) {
			GraphPair f = this.find(pair.getP1(), pair.getP2());
			if(f != null) {
				if(this.array.remove(f)) {
					//System.out.println("REMOVE");
				}
					
			}
			
			return this;
		}
		
		public int sumOfValue() {
			int sum = 0;
			for (GraphPair pair : array) {
				sum += pair.getValue();
			}
			return sum;
		}
		
		public Set<String> getSetOfNodeString(){
			Set<String> s = new HashSet<String>();
			
			for (GraphPair pair : array) {
				s.add(pair.getP1());
				s.add(pair.getP2());
			}
			
			return s;
		}
    }
    
    
    public static class GraphPair{
    	private String p1;
    	private String p2;
    	private Integer value;
    	
    	public GraphPair(String s1, String s2) {
    		this(s1, s2, 1);
    	}
    	
    	public GraphPair(String s1, String s2, Integer value) {
    		this.p1 = s1;
    		this.p2 = s2;
    		this.value = value;
    	}
    	
    	
    	public boolean equals(GraphPair pair) {
    		Boolean b1 = (this.p1.equals(pair.getP1()) && this.p2.equals(pair.getP2()));
    		Boolean b2 = (this.p1.equals(pair.getP2()) && this.p2.equals(pair.getP1()));
    		return (b1 || b2);
    	}
    	
    	public String toString() {
    		return "<" + this.getP1() + ", " + this.getP2() + "> : " + this.getValue();
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
    
    public static class DendroAnalyzer{
    	
    	public ArrayList<NodeCluster> clusters = new ArrayList<NodeCluster>();
    	
    	public void add(String s) {
    		clusters.add(new NodeCluster(null, null, s));
    	}
    	
    	public void merge(String s1, String s2) {
    		NodeCluster n1 = null;
    		NodeCluster n2 = null;
    		
    		for (NodeCluster node : clusters) {
				if(node.value.equals(s1)) {
					n1 = node;
				}
				else if (node.value.equals(s2) ) {
					n2 = node;
				}
			}
    		if(n1 == null || n2 == null) {
    			System.out.println(s1 + " / " + s2);
    			System.out.println("DENDRO : \n" +this);
    		}
    		
    		clusters.add(new NodeCluster(n1, n2, "(" + s1 + " + " + s2 + ")"));
    		
    		clusters.remove(n1);
    		clusters.remove(n2);
    	}
    	
    	public void buildDendro() {
    		
    		for (NodeCluster node : clusters) {
    			JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                DendroPaint dendro = new DendroPaint(DendroPaint.buildDendro(node));
                f.getContentPane().add(dendro);

                f.setSize(800,800);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
    			
			}
    	}
    	
    	public String toString() {
    		String buffer = "";
    		for (NodeCluster node : clusters) {
				buffer += node.toString() + "\n";
			}
    		return buffer;
    	}
    	
    	
    	
    	
    	
    }
    
    public static class NodeCluster{
		
		public String value;
		public NodeCluster left;
		public NodeCluster right;
		
		public NodeCluster(NodeCluster l, NodeCluster r, String v) {
			this.left = l;
			this.right = r;
			this.value = v;
		}
		
		public String toString() {
			String buffer = value;
			buffer += " -->";
			buffer += (left == null) ? "(null" : "("+left.toString();
			buffer += ", ";
			buffer += (right == null) ? "null)" : right.toString()+")";
			return buffer;
		}
		
		
	}

}
