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

public class SpoonTransform<T>{
		
	private CtClass<T> ctClass;
	public Factory factory;
	
	public SpoonTransform(String classFilePath, String className){	
		Launcher launcher = new Launcher();
		launcher.addInputResource(classFilePath);
		Environment e = launcher.getEnvironment();
		e.setCommentEnabled(true);
		e.setAutoImports(true);
		launcher.buildModel();

		factory = launcher.getFactory();
		
		ctClass = (CtClass<T>) launcher.getFactory().Type().get(className);
	}
	
	
	public void addField(String nom, Class<?> class1){
		CtComment com = factory.Core().createComment();
		com.setContent("ajout de l'attribut " + nom);
		
		CtField<?> field = factory.Core().createField();
		
		field.addComment(com);
		field.setSimpleName(nom);
		field.setType(factory.Code().createCtTypeReference(class1));
		field.addModifier(ModifierKind.PRIVATE);
		ctClass.addFieldAtTop(field);
		
		modifyConstructor(nom, class1);
		
		modifyToString();
	}
	
	
	public void addMethod(String nom, String contenu){
		CtMethod<Void> ctMethod = factory.Core().createMethod();
		CtComment com = factory.Core().createComment();
		com.setContent("méthode " + nom + " ajouté avec SPOON");

		ctMethod.addComment(com);
		ctMethod.setSimpleName(nom);
		ctMethod.setVisibility(ModifierKind.PUBLIC);
		ctMethod.setType(factory.Type().createReference(void.class));
		ctMethod.setBody(factory.Code().createCodeSnippetStatement(contenu));
		
		ctClass.addMethod(ctMethod);
	}
	
	private void modifyConstructor(String nom, Class<?> fieldClass){		
		CtConstructor<T> constructor = (CtConstructor<T>) ctClass.getConstructors().toArray()[0];
		CtParameter param = factory.Core().createParameter();
		CtBlock<?> ctBlock = constructor.getBody();

		param.setType(factory.Type().createReference(fieldClass));
		param.setSimpleName(nom);
		fieldClass.getTypeName();
		constructor.addParameter(param);
		ctBlock.addStatement(factory.Code().createCodeSnippetStatement("this." + nom + " = " + nom));
		constructor.setBody(ctBlock);
		ctBlock = constructor.getBody();
	}
	
	
	private void modifyToString(){
		CtMethod<?> ctMethod = ctClass.getMethod("toString");

		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("return ");
		stringBuilder.append("\"" + ctClass.getSimpleName() + " de l'\"");

		for(CtFieldReference<?> reference : ctClass.getAllFields()){
			stringBuilder.append("+ \"");
			stringBuilder.append(reference.getSimpleName());
			stringBuilder.append(" : \" + ");
			stringBuilder.append(reference.getSimpleName());
			stringBuilder.append("\n");
		}
		
		List<CtStatement> statements = new ArrayList<CtStatement>();
		statements.add(factory.Code().createCodeSnippetStatement(stringBuilder.toString()));
		
		ctMethod.getBody().setStatements(statements);
	}
	
	
	public void createFile(String classFilePath, String packageName) throws IOException{
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(classFilePath));
		
		bufferedWriter.write("package " + packageName + ";");
		bufferedWriter.write("\n\n");
		bufferedWriter.write(ctClass.toString());
	    
		bufferedWriter.close();
	}
	
}
