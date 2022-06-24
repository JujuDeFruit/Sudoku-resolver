import Tools.SATSolver;
import Tools.Utils;

import java.util.*;

/**
 * Devoir 2 : l'expert du sudoku
 * @author Guillaume Bayon BAYG31129807
 * @author Julien Raynal RAYJ12059909
 */
public class Main {

    /**
     * Méthode principale
     *
     * @param args la chaîne de caractère représentant le sudoku ( exemple : #26###81#3##7#8##64###5###7#5#1#7#9###39#51###4#3#2#5#1###3###25##2#4##9#38###46#)
     */
    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("No args");
            return;
        }

        long time = 0;

        try {
            final List<List<String>> grille = Utils.parser(args[0]);

            Utils.printSudoku(grille, false);

            final long init = System.nanoTime();

            SATSolver.setGrille(grille);
            SATSolver.setClauses();
            SATSolver.resolve();

            final List<String> results = SATSolver.getResults();

            System.out.println((System.nanoTime() - init) / 1_000_000_000.0 + " secs");

            Utils.printSudoku(Utils.creerGrille(results), true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
