package spoonCode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spoon.Launcher;
import spoon.compiler.Environment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtFieldReference;

public class SpoonAnalyze<T>{
		
	private CtClass<T> ctClass;
	public Factory factory;
	
	public SpoonAnalyze(String classFilePath, String className){	
		Launcher launcher = new Launcher();
		launcher.addInputResource(classFilePath);
		Environment e = launcher.getEnvironment();
		e.setCommentEnabled(true);
		e.setAutoImports(true);
		launcher.buildModel();

		factory = launcher.getFactory();
		
		ctClass = (CtClass<T>) launcher.getFactory().Type().get(className);
	}

	
}
