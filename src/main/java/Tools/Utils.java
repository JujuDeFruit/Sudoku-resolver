package Tools;

import stev.booleans.And;
import stev.booleans.BooleanFormula;
import stev.booleans.PropositionalVariable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Utils {

    /**
     * Méthode permettant de parser la chaine de caractères représentant le sudoku
     *
     * @param entry la chaine de caractères représentant le sudoku
     * @return une liste de lignes du sudoku
     */
    public static List<List<String>> parser(String entry) throws Exception {
        final int SQUARE = 9;

        if (entry.length() != SQUARE * SQUARE) throw new Exception("Entry is not " + SQUARE * SQUARE + " chars !");

        List<List<String>> lines = new LinkedList<>();

        for(int i = 0; i < SQUARE; i++) {
            final String[] substring = entry.substring(SQUARE * i, SQUARE * (i + 1)).split("");
            List<String> cases = new LinkedList<>();

            for (String value : new LinkedList<>(Arrays.asList(substring))) {
                cases.add(Objects.equals(value, "#") ? "0" : value);
            }

            lines.add(cases);
        }

        return lines;
    }

    /**
     * Méthode servant à afficher le sudoku dans la console et sa solution si elle existe
     *
     * @param grille     la grille de sudoku
     * @param isSolution booléen permettant de déclencher l'affichage de la solution
     */
    public static void printSudoku(List<List<String>> grille, boolean isSolution) {
        System.out.println("----------------------------------------------------");
        System.out.println(isSolution ? "--------------------- Solution ---------------------" : "----------------------------------------------------");

        for(int j = 0; j < grille.size(); j++) {
            final List<String> strings = grille.get(j);
            for (int i = 0; i < strings.size(); i++) {
                System.out.print((Objects.equals(strings.get(i), "0") ? "#" : strings.get(i)) + " ");
                if (i == 2 || i == 5) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if (j == 2 || j == 5) {
                System.out.println("- - -   - - -   - - -");
            }
        }
    }

    /**
     * Méthode qui met le résultat obtenu avec le SATSolver sous une forme de grille de sudoku
     *
     * @param valeurs les valeurs renvoyées par le SATSolver
     * @return Une liste de lignes utilisable par la méthode printSudoku
     */
    public static List<List<String>> creerGrille(List<String> valeurs) {
        List<List<String>> grille = new LinkedList<>();
        for (int i = 1; i <= 9; i++) {
            List<String> ligne = new LinkedList<>();
            for (int j = 1; j <= 9; j++) {
                for (String valeur : valeurs) {
                    final String[] splitted = valeur.split("");
                    if(Objects.equals(splitted[0], Integer.toString(i)) && Objects.equals(splitted[1], Integer.toString(j))) {
                        ligne.add(splitted[2]);
                    }
                }
            }
            grille.add(ligne);
        }
        return grille;
    }

    /**
     * Méthode qui permet de créer une BooleanFormula à partir des variables propositionnelles, du sudoku passé en
     * paramètres, et des quatres conditions à respecter pour que le sudoku soit valide
     *
     * @param grille la grille de sudoku à résoudre
     * @return la BooleanFormula que le SATSolver va devoir résoudre
     */
    public static BooleanFormula creerCnf(List<List<String>> grille) {

        return BooleanFormula.toCnf(new And(
                Conditions.createPropVars(),
                Conditions.contrainteExterne(grille),
                Conditions.ligneCondition(),
                Conditions.colonneCondition(),
                Conditions.blocCondition(),
                Conditions.uniciteCondition()
        ));
    }

    /**
     * Méthode servant à créer une formule propositionnelle à partir de 3 entiers ( ligne | colonne | valeur )
     *
     * @return la variable propositionnelle
     */
    public static PropositionalVariable creerFormulePropositionnelle(int a, int b, int c) {
        return new PropositionalVariable(Integer.toString(a) + Integer.toString(b) + Integer.toString(c));
    }
}
