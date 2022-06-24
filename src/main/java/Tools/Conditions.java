package Tools;

import stev.booleans.*;

import javax.rmi.CORBA.Util;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Conditions {

    /**
     * Fonction servant à créer les 729 variables propositionnelles
     */
    public static BooleanFormula createPropVars() {
        And and = new And();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                Or or = new Or();
                for (int k = 1; k <= 9; k++) {
                    or = new Or(or, Utils.creerFormulePropositionnelle(i, j, k));
                }
                and = new And(or, and);
            }
        }
        return and;
    }

    /**
     * Méthode permettant d'implémenter la condition de chiffre unique sur la ligne
     * @return l'objet BooleanCondition propre au sudoku
     */
    public static BooleanFormula ligneCondition() {
        And and = new And();
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Or or = new Or();
                for (int k = 0; k < 9; k++) {
                    or = new Or(or, Utils.creerFormulePropositionnelle(i + 1, k + 1, j + 1));
                }
                and = new And(or, and);
            }
        }
        return and;
    }

    /**
     * Méthode implémentant la règle : Chaque chiffre doit apparaître exactement une fois dans chaque colonne de la
     * grille
     * @return l'objet BooleanCondition propre au sudoku
     */
    public static BooleanFormula colonneCondition() {
        And and = new And();
        for(int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Or or = new Or();
                for (int k = 0; k < 9; k++) {
                    or = new Or(or, Utils.creerFormulePropositionnelle(k + 1, i + 1, j + 1));
                }
                and = new And(or, and);
            }
        }
        return and;
    }

    /**
     * Méthode implémentant la règle : Chaque chiffre doit apparaître exactement une fois dans chacune des neuf
     * sous-grilles de taille 3×3
     * @return l'objet BooleanCondition propre au sudoku
     */
    public static BooleanFormula blocCondition() {
        And and = new And();
        for(int k = 0; k < 9; k++) {
            for (int g = 0; g < 9; g++) {
                Or or = new Or();
                for (int t = 0; t < 9; t++) {
                    or = new Or(or, Utils.creerFormulePropositionnelle(((g / 3) * 3) + (t / 3) + 1, (g % 3) * 3 + (t % 3) + 1, k + 1));
                }
                and = new And(or, and);
            }
        }
        return and;
    }

    /**
     * Méthode implémentant la règle : Chaque case ne peut contenir qu’un seul chiffre
     * @return l'objet BooleanCondition propre au sudoku
     */
    public static BooleanFormula uniciteCondition() {
        LinkedList<BooleanFormula> nots = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int k = 0; k < 9; k++) {
                    for (int l = 0; l < 9; l++) {
                        if(l == k) continue;
                        PropositionalVariable var1 = Utils.creerFormulePropositionnelle(i + 1, j + 1, k + 1);
                        PropositionalVariable var2 = Utils.creerFormulePropositionnelle(i + 1,j + 1,l + 1);
                        nots.add(new Not(new And(var1, var2)));
                    }
                }
            }
        }
        return new And(nots);
    }

    /**
     * Méthode permettant de d'ajouter les conditions qui correspondent aux indices fournis pour la résolution du sudoku
     * @param grille la grille de sudoku avec les indices
     * @return l'objet BooleanCondition propre au sudoku
     */
    public static BooleanFormula contrainteExterne(List<List<String>> grille) {
        List<BooleanFormula> nots = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!Objects.equals(grille.get(i).get(j), "0")) {
                    nots.add(Utils.creerFormulePropositionnelle(i + 1,j + 1, Integer.parseInt(grille.get(i).get(j))));
                }
            }
        }
        return new And(nots);
    }
}
