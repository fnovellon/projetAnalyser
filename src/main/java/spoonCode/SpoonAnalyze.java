package spoonCode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
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

public class SpoonAnalyze<T>{
		
	public Factory factory;
	
	public SpoonAnalyze(String classFilePath, String className){	
		Launcher launcher = new Launcher();
		launcher.addInputResource(classFilePath);
		Environment e = launcher.getEnvironment();
		e.setCommentEnabled(true);
		
		launcher.buildModel();
		factory = launcher.getFactory();
	}
	
	public void analyze() {
		for(CtType<?> ctType : factory.Type().getAll()) {
			Collection<CtMethod<?>> methods = ctType.getMethods();
			System.out.println("Liste des attibuts de la classe : " + ctType.getAllFields().toString() );
			System.out.println("classe " + ctType.getQualifiedName() + " : ");
			System.out.println("-- méthode(s) déclarée(s) dans cette classe : ");
			for(CtMethod<?> m : methods){
				System.out.println("---- " + m.getSimpleName());
				
				for(CtInvocation<?> methodInvocation : (List<CtInvocation>) Query.getElements(m, new TypeFilter<CtInvocation>(CtInvocation.class))){
					if(methodInvocation.getTarget().getType() != null)
						if(isProjectClass(methodInvocation.getTarget().getType().toString()))
							System.out.println("------ méthode appelée : " + methodInvocation.getExecutable().getSimpleName() + " provenant de " + methodInvocation.getTarget().getType().toString());
				}
			}
		}
	}
	
	public boolean isProjectClass(String className)
	{
		for(CtType<?> ctType : factory.Type().getAll())
		{
			if(ctType.getQualifiedName().equals(className))
				return true;
		}
		
		return false;
	}
	
	public static void main(String args[]) {
		SpoonAnalyze<Void> spoon = new SpoonAnalyze<Void>("./src/main/java/ue/evolRestruct/projetAnalyser/Model/ElementAnalyzer/ClassAnalyzer.java", " ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer");
		spoon.analyze();
	}

	
}
