package ue.evolRestruct.projetAnalyser.Model.FileSystem;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class PackageStats implements FileSystemStats {

	private ArrayList<FileSystemStats> content;
	private PackageDeclaration packageDeclaration;
	private String name;

	public PackageStats(String name) {
		this.name = name;
		this.content = new ArrayList<>();
	}

	@Override
	public int getNumberOfLines() {
		int sum = 0;
		for (FileSystemStats fileSystemStats : this.content) {
			sum += fileSystemStats.getNumberOfLines();
		}
		return sum;
	}

	@Override
	public ArrayList<TypeDeclaration> getTypeDeclaration() {
		ArrayList<TypeDeclaration> list = new ArrayList<>();
		for (FileSystemStats fileSystemStats : this.content) {
			list.addAll(fileSystemStats.getTypeDeclaration());
		}
		return list;
	}

	@Override
	public ArrayList<MethodDeclaration> getMethodDeclaration() {
		ArrayList<MethodDeclaration> list = new ArrayList<>();
		for (FileSystemStats fileSystemStats : this.content) {
			list.addAll(fileSystemStats.getMethodDeclaration());
		}
		return list;
	}

	@Override
	public ArrayList<FieldDeclaration> getFieldDeclaration() {
		ArrayList<FieldDeclaration> list = new ArrayList<>();
		for (FileSystemStats fileSystemStats : this.content) {
			list.addAll(fileSystemStats.getFieldDeclaration());
		}
		return list;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Boolean isPackage() {
		return true;
	}

	public PackageStats mkpack(String path){
		String[] split = path.split("\\.", 2);
		
		PackageStats pack = this.findPackage(split[0]);
		if(pack != null) {
			if(split.length > 1) {
				return pack.mkpack(split[1]);
			}
			else {
				return pack;
			}
		}
		else {
			PackageStats npack = new PackageStats(split[0]);
			this.content.add(npack);
			if(split.length > 1) {
				return npack.mkpack(split[1]);
			}
			else {
				return npack;
			}
		}
	}
	
	public PackageStats find(String name, Boolean isPackage) {
		for (FileSystemStats fileSystemStats : this.content) {
			if(fileSystemStats.isPackage() || !isPackage) {
				if(fileSystemStats.getName().equals(name)) {
					return (PackageStats) fileSystemStats;
				}
			}
		}
		return null;
	}
	
	public PackageStats findPackage(String name) {
		return this.find(name, true);
	}
	
	public void addFileSystem(FileSystemStats fs) {
		this.content.add(fs);
	}

	@Override
	public String toString() {
		return toString(0);
	}

	
	public String toString(int tabNb) {
		String buff = this.getName() + "\n";
		for (FileSystemStats fileSystemStats : content) {
			for(int i = 0; i < tabNb+1; i++) {
				buff += "\t";
			}
			buff += fileSystemStats.toString(tabNb+1);
		}
		return buff;
	}

}
