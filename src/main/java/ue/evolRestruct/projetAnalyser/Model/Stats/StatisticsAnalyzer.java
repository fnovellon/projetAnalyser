package ue.evolRestruct.projetAnalyser.Model.Stats;

import java.util.ArrayList;

import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.ElementAnalyzer;
import ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer.PackageAnalyzer;

public class StatisticsAnalyzer{
	
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
		//TODO
		return 0;
	}
	
	public static int numberOfPackages(PackageAnalyzer packageAnalyzer) {
		//TODO
		return 0;
	}
	public static double averageNumberOfMethodsByClass(PackageAnalyzer packageAnalyzer) {
		//TODO
		return 0;
	}
	public static double averageNumberOfLinesOfCodeByMethod(PackageAnalyzer packageAnalyzer) {
		//TODO
		return 0;
	}
	public static double averageNumberOfFieldsByClass(PackageAnalyzer packageAnalyzer) {
		//TODO
		return 0;
	}
	public static ArrayList<String> biggestClassesByNumberOfMethods(PackageAnalyzer packageAnalyzer, double percent) {
		//TODO
		return null;
	}
	public static ArrayList<String> biggestClassesByNumberOfFields(PackageAnalyzer packageAnalyzer, double percent) {
		//TODO
		return null;
	}
	public static ArrayList<String> biggestClassesByNumberOfMethodsAndByNumberOfFields(PackageAnalyzer packageAnalyzer, double percent) {
		//TODO
		return null;
	}
	public static ArrayList<String> classesWithMoreThenXMethods(PackageAnalyzer packageAnalyzer, int number) {
		//TODO
		return null;
	}
	public static ArrayList<String> biggestMethodesByNumberOfLinesOfCode(PackageAnalyzer packageAnalyzer, double percent) {
		//TODO
		return null;
	}
	public static int maximumNumberOfParameters(PackageAnalyzer packageAnalyzer) {
		//TODO
		return 0;
	}
	
	
	
	
}
