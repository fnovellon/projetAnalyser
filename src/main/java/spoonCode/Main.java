package spoonCode;

import java.io.IOException;

public class Main {

	public static void main(String[] Args) throws IOException{		
		SpoonTransform<Void> spoon = new SpoonTransform<Void>("./src/original/Vehicule.java", "original.Vehicule");
		
		spoon.addField("annee", Integer.class);
		
		spoon.addMethod("vehiculeRecent", "if(this.annee >= 2010){\n System.out.println(\"le vehicule est recent\");\n}else {\n System.out.println(\"le vehicule est ancient\");\n} ");
		spoon.createFile("./src/transform/Vehicule.java", "transform");
	}

}
