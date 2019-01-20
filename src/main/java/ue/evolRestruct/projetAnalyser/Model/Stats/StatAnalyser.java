package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.Name;

import ue.evolRestruct.projetAnalyser.Model.FileSystem.ClassStats;
import ue.evolRestruct.projetAnalyser.Model.FileSystem.PackageStats;
import ue.evolRestruct.projetAnalyser.Model.Visitor.FieldDeclarationVisitor;
import ue.evolRestruct.projetAnalyser.Model.Visitor.MethodDeclarationVisitor;
import ue.evolRestruct.projetAnalyser.Model.Visitor.PackageDeclarationVisitor;
import ue.evolRestruct.projetAnalyser.Model.Visitor.TypeDeclarationVisitor;

public class StatAnalyser {

	private String folderPath;
	private File folder;
	private ArrayList<File> javaFiles;
	private final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_181\\lib\\rt.jar";

	private List<TypeDeclaration> types;
	private HashMap<String, PackageDeclaration> packages;
	private List<MethodDeclaration> methods;
	private List<FieldDeclaration> fields;

	public StatAnalyser(String folderPath) throws IOException {
		this.folderPath = folderPath;
		this.folder = new File(folderPath);
		this.javaFiles = listJavaFilesForFolder(folder);

		this.parseCallVisitors();
	}

	public static void main(String[] args) {
		try {
			StatAnalyser s = new StatAnalyser("./");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parseCallVisitors() throws IOException {
		this.types = new ArrayList<TypeDeclaration>();
		this.packages = new HashMap<String, PackageDeclaration>();
		this.methods = new ArrayList<MethodDeclaration>();
		this.fields = new ArrayList<FieldDeclaration>();

		PackageStats root = new PackageStats("root");

		for (File fileEntry : this.javaFiles) {

			String content = FileUtils.readFileToString(fileEntry);
			CompilationUnit parse = parse(content.toCharArray());
			//System.out.println(fileEntry.getName());


			PackageDeclarationVisitor packageVisitor = new PackageDeclarationVisitor();
			parse.accept(packageVisitor);
			PackageDeclaration packdecl = packageVisitor.getPackages();

			PackageStats currentPack;
			if(packdecl != null) {
				Name packageName = packdecl.getName();
				currentPack = root.mkpack(packageName.toString());
			}
			else {
				currentPack = root;				
			}

			TypeDeclarationVisitor typeVisitor = new TypeDeclarationVisitor(parse);
			parse.accept(typeVisitor);

			MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor(parse);
			parse.accept(methodVisitor);

			FieldDeclarationVisitor fieldVisitor = new FieldDeclarationVisitor();
			parse.accept(fieldVisitor);

			ClassStats nClass = new ClassStats(fileEntry.getName());

			nClass.setTypeDeclarationArray(typeVisitor.getTypes());
			nClass.setMethodDeclarationArray(methodVisitor.getMethods());
			nClass.setFieldDeclarationArray(fieldVisitor.getFields());

			currentPack.addFileSystem(nClass);

			//parse.getLineNumber(node.getStartPosition() + node.getLength()) - parse.getLineNumber(node.getStartPosition());

		}	
		System.out.println(root.toString());

	}

	private ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	private CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { this.folderPath }; 
		String[] classpath = {jrePath};

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	public int getNumberOfClasses() {
		return this.types.size();
	}

	public int getNumberOfMethods() {
		return this.methods.size();
	}

	public int getNumberOfPackages() {
		return this.packages.size();
	}

	public int getNumberOfFields() {
		return this.fields.size();
	}

	public double getAverageMethodsPerClass() {
		return (this.getNumberOfMethods() / this.getNumberOfClasses());
	}

	public double getAverageFieldsPerClass() {
		return (this.getNumberOfFields() / this.getNumberOfClasses());
	}

	private int getNumberOfLine(ASTNode node) {
		return 0;
	}

	public double getAverageNumberOfLinesPerMethod() {
		int sum = 0;
		for(MethodDeclaration m : this.methods) {
			//t lines = 
		}


		return 0;
	}






}
