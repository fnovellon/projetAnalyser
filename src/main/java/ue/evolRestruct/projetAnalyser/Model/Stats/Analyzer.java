package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Name;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ElementAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.Visitor.PackageDeclarationVisitor;
import ue.evolRestruct.projetAnalyser.Model.Visitor.TypeDeclarationVisitor;

public class Analyzer {

	private String folderPath;
	private File folder;
	private ArrayList<File> javaFiles;
	private final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_181\\lib\\rt.jar";


	public Analyzer(String folderPath) throws IOException {
		this.folderPath = folderPath;
		this.folder = new File(folderPath);
		this.javaFiles = listJavaFilesForFolder(folder);

		this.parseCallVisitors();
	}

	public static void main(String[] args) {
		try {
			//Analyzer analyzer = new Analyzer("C:\\Users\\flnov\\OneDrive\\Documents\\ExtractProjetWorkSpace\\projectTest");
			Analyzer analyzer = new Analyzer("./");
			PackageAnalyzer project = analyzer.parseCallVisitors();
			GraphAnalyzer graph = new GraphAnalyzer(project);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PackageAnalyzer parseCallVisitors() throws IOException {

		PackageAnalyzer root = new PackageAnalyzer("root", null);

		for (File fileEntry : this.javaFiles) {
			CompilationUnit parse = parse(FileUtils.readFileToString(fileEntry, (String) null).toCharArray());


			PackageDeclarationVisitor packageVisitor = new PackageDeclarationVisitor();
			parse.accept(packageVisitor);
			PackageDeclaration packdecl = packageVisitor.getPackages();

			PackageAnalyzer currentPack;
			if(packdecl != null) {
				Name packageName = packdecl.getName();
				currentPack = root.mkpack(packageName.toString());
			}
			else {
				currentPack = root;				
			}
			
			TypeDeclarationVisitor typeVisitor = new TypeDeclarationVisitor(parse, currentPack);
			parse.accept(typeVisitor);
			for(ClassAnalyzer fs : typeVisitor.getClassList()) {
				currentPack.addFileSystem(fs);
			}

			
		}	
	
		
		return root;
	}
	
	public void printStats(PackageAnalyzer root) {
		System.out.println(root.toString());
		System.out.println("Nombre de classes : " + StatisticsAnalyzer.numberOfClasses(root));
		System.out.println("Nombre de lignes de code : " + StatisticsAnalyzer.numberOfLinesOfCode(root));
		System.out.println("Nombre de méthodes : " + StatisticsAnalyzer.numberOfMethods(root));
		System.out.println("Nombre de packages : " + StatisticsAnalyzer.numberOfPackages(root));
		System.out.println("Nombre moyen de méthodes par classe : " + StatisticsAnalyzer.averageNumberOfMethodsByClass(root));
		System.out.println("Nombre moyen de lignes de code par méthode : " + StatisticsAnalyzer.averageNumberOfLinesOfCodeByMethod(root));
		System.out.println("Nombre moyen de d'attributs par classe : " + StatisticsAnalyzer.averageNumberOfFieldsByClass(root));
		System.out.println("Les 10% des classes qui possèdent le plus grand nombre de méthodes : " + StatisticsAnalyzer.biggestClassesByNumberOfMethods(root, 10));
		System.out.println("Les 10% des classes qui possèdent le plus grand nombre d'attributs : " + StatisticsAnalyzer.biggestClassesByNumberOfFields(root, 10));
		System.out.println("Les classes qui font partie en même temps des deux catégories précédentes : " + StatisticsAnalyzer.biggestClassesByNumberOfMethodsAndByNumberOfFields(root, 10));
		int X = 3;
		System.out.println("Les classes qui possèdent plus de X=" + X + " méthodes : " + StatisticsAnalyzer.classesWithMoreThenXMethods(root, X));
		System.out.println("Les 10% des méthdoes qui possèdent le plus grand nombre de lignes de code : " + StatisticsAnalyzer.biggestMethodesByNumberOfLinesOfCode(root, 10));
		System.out.println("Nombre maximum de paramètres : " + StatisticsAnalyzer.maximumNumberOfParameters(root));	
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








}
