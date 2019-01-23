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
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;

public class GraphAnalyzer {
	
	public PackageAnalyzer packageAnalyzer;
	public Graph graph;
		
	public GraphAnalyzer(PackageAnalyzer packageAnalyzer) {
		this.packageAnalyzer = packageAnalyzer;
		
		System.setProperty("org.graphstream.ui.renderer",
		        "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		this.graph = new MultiGraph("CallMethodsGraph");
        graph.addAttribute("ui.stylesheet", style);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        //graph.display();
		
		ArrayList<ClassAnalyzer> list = StatisticsAnalyzer.getAllClasses(packageAnalyzer);
		System.out.println("LISTE");
		for(ClassAnalyzer c : list) {
			System.out.println("\t"+c.getQualifiedName());
		}
		System.out.println("--------------------------------------------");
		System.out.println("--------------------------------------------");
		
		for(ClassAnalyzer currentClass : list) {
			System.out.println("CLASS : " + currentClass.getQualifiedName());
			for(MethodAnalyzer currentMethod : currentClass.getMethods()) {
				//System.out.println("\tMETHOD : " + currentMethod.getName());
				for(MethodInvocation methodInvocation : currentMethod.getInvocatedMethods()) {
					IMethodBinding m = methodInvocation.resolveMethodBinding();
					
					String qualifierName = "";
					if(m == null) {
						if(methodInvocation.getExpression() == null) continue;
						ITypeBinding t = methodInvocation.getExpression().resolveTypeBinding();
						if(t == null) {
							//printRed(methodInvocation.toString());
							continue;
						}
						qualifierName = t.getName().toString();
						qualifierName = t.getQualifiedName();
						System.out.println(qualifierName);
					}
					else {
						//qualifierName = m.getDeclaringClass().getQualifiedName();
					}
					
					if( findClass(list, qualifierName) == null ) {
						//System.out.println("NOT FOUND : " + qualifierName + " ::: " + methodInvocation.getName());
					}
					
					
					/*
					System.out.println("\t\tINVOK : " + methodInvocation.toString());
					Expression expr = methodInvocation.getExpression();
					if(expr == null) {printRed("EXPR NULL"); continue;}
					ITypeBinding tBind = expr.resolveTypeBinding();
					if(tBind == null) {printRed("BIND NULL"); continue;}
					
					ITypeBinding ty = tBind.;
					if(ty == null) {System.out.println("*******"); continue; }
					String qualifiedName = ty.toString();
					ClassAnalyzer fromClass = findClass(list, qualifiedName);					
					if(fromClass == null) {printRed("CLASS NOT FOUND : \"" + qualifiedName + "\""); continue;}//La class n'appartient pas au projet} 
					if(currentClass.getQualifiedName().equals(qualifiedName)) {continue; }
					graph.addEdge(
							currentClass.getQualifiedName() + "." + currentMethod.getName().toString() + " ---> " + qualifiedName + "." + methodInvocation.getName().toString(), 
							currentClass.getQualifiedName(), 
							qualifiedName,
							true);
					System.out.println(currentClass.getQualifiedName() + "." + currentMethod.getName().toString() + " ---> " + qualifiedName + "." + methodInvocation.getName().toString());
					*/
				}
			}
		}
		
		for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
		
		for (Edge edge : graph.getEdgeSet()) {
			//edge.addAttribute("ui.label", edge.getId());
        }

	
	
	}

	public static void printRed(String s) {
		System.out.println("-----------" + s);
	}

	
	private ClassAnalyzer findClass(ArrayList<ClassAnalyzer> list, String name) {
		for(ClassAnalyzer currentClass : list) {
			if( currentClass.getQualifiedName().equals(name) ) {
				return currentClass;
			}
		}
		
		return null;
	}
	
	

    public static void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();

        while (k.hasNext()) {
            Node next = k.next();
            next.setAttribute("ui.class", "marked");
            sleep();
        }
    }

    protected static void sleep() {
        try { Thread.sleep(1000); } catch (Exception e) {}
    }

    protected static String style = 
    		"node {" + 
    		"	size: 10px, 15px;" + 
    		"	shape: box;" + 
    		"	fill-color: green;" + 
    		"	stroke-mode: plain;" + 
    		"	stroke-color: yellow;" + 
    		"}";
    /*
	public static void main(String args[]) {
		Graph graph = new SingleGraph("tutorial 1");

        //graph.addAttribute("ui.stylesheet", styleSheet);
        graph.addAttribute("ui.stylesheet", style);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.display();

        graph.addEdge("AB", "Adrien", "Bastien");
        graph.addEdge("BC", "Bastien", "Caroline");
        graph.addEdge("CA", "Caroline", "Adrien");
        graph.addEdge("AD", "Adrien", "Denis");
        graph.addEdge("DE", "Denis", "Etienne");
        graph.addEdge("DF", "Denis", "Fabien");
        graph.addEdge("EF", "Etienne", "Fabien");

        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }

       // explore(graph.getNode("Adrien"));
    }*/

}
