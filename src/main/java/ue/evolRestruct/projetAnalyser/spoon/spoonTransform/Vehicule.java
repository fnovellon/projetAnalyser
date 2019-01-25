package ue.evolRestruct.projetAnalyser.spoon.spoonTransform;

public class Vehicule {
    /* ajout de l'attribut annee */
    private Integer annee;

    public String modele;

    public String marque;

    public String couleur;

    public Vehicule(String ma, String mo, String c, Integer annee) {
        this.marque = ma;
        this.modele = mo;
        this.couleur = c;
        this.annee = annee;
    }

    public String toString() {
        return "Vehicule de l'"+ "annee : " + annee
        + "modele : " + modele
        + "marque : " + marque
        + "couleur : " + couleur
        ;
    }

    /* méthode vehiculeRecent ajouté avec SPOON */
    public void vehiculeRecent() {
        if(this.annee >= 2010){
         System.out.println("le vehicule est recent");
        }else {
         System.out.println("le vehicule est ancient");
        } ;
    }
}