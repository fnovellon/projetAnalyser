package ue.evolRestruct.projetAnalyser.spoon.spoonCode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.AllServiceListener;

import spoon.Launcher;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.Query;
import spoon.reflect.visitor.filter.TypeFilter;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.GraphAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Stats.GraphAnalyzer.PairArray;

public class SpoonAnalyze<T> {
		
	public static Factory factory;
	private static ArrayList<File> javaFiles;
	public static PairArray pairArray = new PairArray();
	static ArrayList<String> allClasses = new ArrayList<>();

	
	public SpoonAnalyze(String classFilePath){
		Launcher launcher = new Launcher();
		launcher.addInputResource(classFilePath);		
		launcher.buildModel();
		
		factory = launcher.getFactory();
	}
	
	private static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				javaFiles.add(fileEntry);
			}
		}
		return javaFiles;
	}
	
	public void analyze() {
		for(CtType<?> ctType : factory.Type().getAll()) {
			Collection<CtMethod<?>> methods = ctType.getMethods();
			//System.out.println("classe " + ctType.getQualifiedName() + " : ");
			//System.out.println("Liste des attibuts de la classe : " + ctType.getAllFields().toString());
			//System.out.println("-- méthode(s) déclarée(s) dans cette classe : ");
			for(CtMethod<?> m : methods){
				//System.out.println("---- " + m.getSimpleName());
				for(CtInvocation<?> methodInvocation : (List<CtInvocation>) Query.getElements(m, new TypeFilter<CtInvocation>(CtInvocation.class))){
					if(methodInvocation != null && methodInvocation.getTarget() != null && methodInvocation.getTarget().getType() != null) {
						if(!ctType.getQualifiedName().equals(methodInvocation.getTarget().getType().toString())){
							//System.out.println("------ méthode appelée : " + methodInvocation.getExecutable().getSimpleName() + " provenant de " + methodInvocation.getTarget().getType().toString());	
							if(verifyIfOurClasses(methodInvocation.getTarget().getType().toString())) {
								this.pairArray.addPair(ctType.getQualifiedName(), methodInvocation.getTarget().getType().toString());
							}		
						}
					}
				}
			}
		}
	}
	
	public static boolean verifyIfOurClasses(String classeATester) {
		for(String c : allClasses) {
			if(c.equals(classeATester)) {
				return true;
			}
		}
		return false;
	}
	
	public static void collectAllClasses() {
		for(CtType<?> ctType : factory.Type().getAll()) {
			allClasses.add(ctType.getQualifiedName());
		}		
		//System.out.println("teeeest" + allClasses);
	}
	
	public static void checkAllProject(String path) {
		javaFiles = listJavaFilesForFolder(new File(path));		
		int cpt = 1;
		for (File fileEntry : javaFiles) {
			//System.out.println("Fichier numéro " + cpt);
			//System.out.println(fileEntry.toString());
			SpoonAnalyze<Void> spoon = new SpoonAnalyze<Void>(fileEntry.toString());
			spoon.collectAllClasses();
			spoon.analyze();
			cpt++;
		}
	}
		
	public static void main(String args[]) {
		checkAllProject("./");
		
		try{
			GraphAnalyzer.buildPonderalGraph(pairArray).display();
		}
		catch (Exception e){
			System.err.println(e);
		}
	}

}
