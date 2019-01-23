package spoonOriginal;

public class Vehicule {
	
    public String modele;
    public String marque;
    public String couleur;

    public Vehicule(String ma, String mo, String c) {
        this.marque = ma;
        this.modele = mo;
        this.couleur = c;
    }
    
    public String toString() {
		return "Véhicule " + this.modele + " de la marque " + this.marque + ", de couleur " + this.couleur; 
	}

}