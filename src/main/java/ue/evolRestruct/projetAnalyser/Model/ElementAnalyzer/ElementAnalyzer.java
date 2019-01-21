package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

public interface ElementAnalyzer {

	public String getName();
	public int getNumberOfLines();
	public default Boolean isPackage() {
		return false;
	}
	public String toString();
	public String toString(int tabNb);
}
