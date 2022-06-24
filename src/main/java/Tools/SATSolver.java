package Tools;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import stev.booleans.BooleanFormula;

import java.util.*;

public class SATSolver {

    private final static ISolver solver = SolverFactory.newDefault();
    private static List<List<String>> grille;
    private static int[] model;
    private static BooleanFormula cnf;

    /**
     * Méthode qui crée la formule finale à résoudre par le SATSolver
     *
     * @param grille_ la grille de sudoku
     */
    public static void setGrille(List<List<String>> grille_) {
        grille = grille_;
        cnf = Utils.creerCnf(grille);
    }

    /**
     * Méthode qui permet de récupérer les clauses à ajouter au ISolver
     */
    public static void setClauses() throws ContradictionException {
        for (int[] clause : cnf.getClauses()) {
            solver.addClause(new VecInt(clause));
        }
    }

    /**
     * Méthode qui résoud le sudoku à l'aide du ISolver de Sat4j
     */
    public static void resolve() throws Exception {
        if (grille.size() != 9) throw new Exception("Veuillez insérer la grille avant de résoudre le problème !");

        if(solver.isSatisfiable()) {
            // Les valeurs contenues dans le model sont les index cnf contenants  les bonnes valeurs
            model = solver.model();
        } else throw new Exception("Le problème n'a pas de solutions !");
    }

    /**
     * Méthode permettant d'extraire les résultats du modèle produit par le ISolver
     *
     * @return la solution au sudoku donné
     */
    public static List<String> getResults() throws Exception {
        if(model.length <= 0) resolve();

        Map<String,Integer> res = cnf.getVariablesMap();
        List<String> result = new LinkedList<>();
        for (Integer val : Arrays.stream(model).filter(j -> j > 0).toArray()) {
            if(res.containsValue(val)) {
                int i = Arrays.asList(res.values().toArray()).indexOf(val);
                result.add(res.keySet().toArray()[i].toString());
            }
        }
        return result;
    }
}
