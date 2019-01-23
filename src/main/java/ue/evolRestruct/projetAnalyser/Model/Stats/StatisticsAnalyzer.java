package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.util.ArrayList;
import java.util.Comparator;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ClassAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ElementAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.MethodAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;

public class StatisticsAnalyzer{
	
	public  int numberOfClassesd(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				sum += numberOfClasses((PackageAnalyzer) e);
			}
			else {
				sum++;
			}
		}
		return sum;
	}
	
	public static int numberOfClasses(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				sum += numberOfClasses((PackageAnalyzer) e);
			}
			else {
				sum++;
			}
		}
		return sum;
	}
	
	public static int numberOfLinesOfCode(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		sum = packageAnalyzer.getNumberOfLines();
		return sum;
	}
	
	public static int numberOfMethods(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				sum += numberOfMethods((PackageAnalyzer) e);
			}
			else {
				sum += ((ClassAnalyzer) e).getMethods().size();
			}
		}
		return sum;
	}
	
	public static int numberOfPackages(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				sum = 1 +numberOfPackages((PackageAnalyzer) e);
			}
		}
		return sum;
	}
	
	public static double averageNumberOfMethodsByClass(PackageAnalyzer packageAnalyzer) {
		double methodsCount = numberOfMethods(packageAnalyzer);
		double classCount = numberOfClasses(packageAnalyzer);

		return (methodsCount / classCount);
	}
	
	private static int numberOfLinesOfCodeInMethod(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				sum += numberOfLinesOfCodeInMethod((PackageAnalyzer) e);
			}
			else {
				for(MethodAnalyzer method : ((ClassAnalyzer) e).getMethods()) {
					sum += method.getNumberOfLines();
				}
			}
		}
		
		return sum;
	}
	
	public static double averageNumberOfLinesOfCodeByMethod(PackageAnalyzer packageAnalyzer) {
		double methodLOC = numberOfLinesOfCodeInMethod(packageAnalyzer);
		double methodCount = numberOfMethods(packageAnalyzer);

		return (methodLOC / methodCount);
	}
	
	public static int numberOfFields(PackageAnalyzer packageAnalyzer) {
		int sum = 0;
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				sum += numberOfMethods((PackageAnalyzer) e);
			}
			else {
				sum += ((ClassAnalyzer) e).getFields().size();
			}
		}
		
		return sum;
	}
	
	public static double averageNumberOfFieldsByClass(PackageAnalyzer packageAnalyzer) {
		double classCount = numberOfClasses(packageAnalyzer);
		double fieldCount = numberOfFields(packageAnalyzer);
		
		return (fieldCount / classCount);
	}
	
	public static ArrayList<ClassAnalyzer> getAllClasses(PackageAnalyzer packageAnalyzer) {
		ArrayList<ClassAnalyzer> classList = new ArrayList<ClassAnalyzer>();
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				classList.addAll(getAllClasses((PackageAnalyzer) e));
			}
			else {
				classList.add((ClassAnalyzer) e); 
			}
		}
		return classList;
	}
	
	public static ArrayList<ClassAnalyzer> biggestClassesByNumberOfMethods(PackageAnalyzer packageAnalyzer, double percent) {
		ArrayList<ClassAnalyzer> classList = getAllClasses(packageAnalyzer);
			
		classList.sort(new Comparator<ClassAnalyzer>() {
		    @Override
		    public int compare(ClassAnalyzer left, ClassAnalyzer right) {
		        return right.getMethods().size() - left.getMethods().size();
		    }
		});
		
		int numberFromPercent = classList.size() / 10;
		if (numberFromPercent < 1) {
			numberFromPercent = 1;
		}
		
		ArrayList<ClassAnalyzer> resClasses = new ArrayList<ClassAnalyzer>();
		
		for(int i = 0; i < numberFromPercent; i++) {
			resClasses.add(classList.get(i));
		}
		
		
		return resClasses;
	}
	public static ArrayList<ClassAnalyzer> biggestClassesByNumberOfFields(PackageAnalyzer packageAnalyzer, double percent) {
		ArrayList<ClassAnalyzer> classList = getAllClasses(packageAnalyzer);
		
		classList.sort(new Comparator<ClassAnalyzer>() {
		    @Override
		    public int compare(ClassAnalyzer left, ClassAnalyzer right) {
		        return right.getFields().size() - left.getFields().size();
		    }
		});
		
		int numberFromPercent = classList.size() / 10;
		if (numberFromPercent < 1) {
			numberFromPercent = 1;
		}
		
		ArrayList<ClassAnalyzer> resClasses = new ArrayList<ClassAnalyzer>();
		
		for(int i = 0; i < numberFromPercent; i++) {
			resClasses.add(classList.get(i));
		}
		
		
		return resClasses;
	}
	public static ArrayList<ClassAnalyzer> biggestClassesByNumberOfMethodsAndByNumberOfFields(PackageAnalyzer packageAnalyzer, double percent) {
		ArrayList<ClassAnalyzer> byMethod = biggestClassesByNumberOfMethods(packageAnalyzer, percent);
		ArrayList<ClassAnalyzer> byField = biggestClassesByNumberOfFields(packageAnalyzer, percent);
		
		ArrayList<ClassAnalyzer> intersectionList = new ArrayList<ClassAnalyzer>();
		
		for(ClassAnalyzer classAnalyzer : byMethod) {
			if(byField.contains(classAnalyzer)) {
				intersectionList.add(classAnalyzer);
            }
		}
		
		return intersectionList;
	}
	public static ArrayList<ClassAnalyzer> classesWithMoreThenXMethods(PackageAnalyzer packageAnalyzer, int number) {
		ArrayList<ClassAnalyzer> classList = getAllClasses(packageAnalyzer);
		
		ArrayList<ClassAnalyzer> resClasses = new ArrayList<ClassAnalyzer>();
		for(ClassAnalyzer classAnalyzer : classList) {
			if(classAnalyzer.getMethods().size() > number) {
				resClasses.add(classAnalyzer);
			}
		}
		return resClasses;
	}
	
	private static ArrayList<MethodAnalyzer> getAllMethods(PackageAnalyzer packageAnalyzer) {
		ArrayList<MethodAnalyzer> methodList = new ArrayList<MethodAnalyzer>();
		
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				methodList.addAll(getAllMethods((PackageAnalyzer) e));
			}
			else {
				methodList.addAll(( (ClassAnalyzer) e).getMethods()); 
			}
		}
		
		return methodList;
	}
	
	public static ArrayList<MethodAnalyzer> biggestMethodesByNumberOfLinesOfCode(PackageAnalyzer packageAnalyzer, double percent) {
		ArrayList<MethodAnalyzer> methodList = getAllMethods(packageAnalyzer);
		
		methodList.sort(new Comparator<MethodAnalyzer>() {
		    @Override
		    public int compare(MethodAnalyzer left, MethodAnalyzer right) {
		        return right.getNumberOfLines() - left.getNumberOfLines();
		    }
		});
		
		int numberFromPercent = methodList.size() / 10;
		if (numberFromPercent < 1) {
			numberFromPercent = 1;
		}
		
		ArrayList<MethodAnalyzer> resClasses = new ArrayList<MethodAnalyzer>();
		
		for(int i = 0; i < numberFromPercent; i++) {
			resClasses.add(methodList.get(i));
		}
		
		
		return resClasses;
	}
	
	public static int maximumNumberOfParameters(PackageAnalyzer packageAnalyzer) {
		int max = 0;
		
		for(ElementAnalyzer e : packageAnalyzer.getContent()){
			if(e.isPackage()) {
				max = Math.max(max, maximumNumberOfParameters((PackageAnalyzer) e));
			}
			else {
				for(MethodAnalyzer method : ((ClassAnalyzer) e).getMethods()) {
					max = Math.max(max, method.getNumberOfParameters());
				}
			}
		}
		
		return max;
	}
	
	
	
	
}
