package ue.evolRestruct.projetAnalyser.Model.ElementAnalyzer;

import java.util.ArrayList;

public class PackageAnalyzer implements ElementAnalyzer {
	
	private ArrayList<ElementAnalyzer> content;
	
	private String name;
	private PackageAnalyzer parent;
	
	public PackageAnalyzer(String name, PackageAnalyzer parent) {
		this();
		this.name = name;
		this.parent = parent;
	}
	
	private PackageAnalyzer() {
		this.content = new ArrayList<ElementAnalyzer>();
	}

	@Override
	public String getName() {
		return this.name;/*
		if(parent.getParent() == null) {
			return this.name;			
		}
		return this.parent.getName() + "." + this.name;*/
	}

	@Override
	public Boolean isPackage() {
		return true;
	}
	
	@Override
	public int getNumberOfLines() {
		int sum = 0;
		for (ElementAnalyzer fileSystemStats : this.content) {
			sum += fileSystemStats.getNumberOfLines();
		}
		return sum;
	}

	public PackageAnalyzer mkpack(String path){
		String[] split = path.split("\\.", 2);
		
		PackageAnalyzer pack = this.findPackage(split[0]);
		if(pack != null) {
			if(split.length > 1) {
				return pack.mkpack(split[1]);
			}
			else {
				return pack;
			}
		}
		else {
			PackageAnalyzer npack = new PackageAnalyzer(split[0], this);
			this.content.add(npack);
			if(split.length > 1) {
				return npack.mkpack(split[1]);
			}
			else {
				return npack;
			}
		}
	}
	
	public PackageAnalyzer find(String name, Boolean isPackage) {
		for (ElementAnalyzer fileSystemStats : this.content) {
			if(fileSystemStats.isPackage() || !isPackage) {
				if(fileSystemStats.getName().equals(name)) {
					return (PackageAnalyzer) fileSystemStats;
				}
			}
		}
		return null;
	}
	
	public PackageAnalyzer findPackage(String name) {
		return this.find(name, true);
	}
	
	public void addFileSystem(ElementAnalyzer fs) {
		this.content.add(fs);
	}

	@Override
	public String toString() {
		return toString(0);
	}

	
	public String toString(int tabNb) {
		String buff = this.getName() + "\n";
		for (ElementAnalyzer fileSystemStats : content) {
			for(int i = 0; i < tabNb+1; i++) {
				buff += "\t";
			}
			buff += fileSystemStats.toString(tabNb+1);
		}
		return buff;
	}

	public ArrayList<ElementAnalyzer> getContent() {
		return content;
	}

	public void setContent(ArrayList<ElementAnalyzer> content) {
		this.content = content;
	}

	public PackageAnalyzer getParent() {
		return parent;
	}

	public void setParent(PackageAnalyzer parent) {
		this.parent = parent;
	}



	
}
