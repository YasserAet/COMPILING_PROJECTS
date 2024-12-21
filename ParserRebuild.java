import java.util.*;

public class ParserRebuild {

    // Definition du vocabulaire
    static Set<String> articles = Set.of("le", "la", "les", "une", "un", "des");
    static Set<String> noms = Set.of("souris", "fromage", "téléphone", "matin", "chat", "chien", "voiture");
    static Set<String> verbes = Set.of("mange", "mangent", "charge", "sonne", "court", "parle", "voit");
    static Set<String> groupesAdverbiaux = Set.of("chaque matin", "hier soir");
    static Set<String> groupesTemporels = Set.of("à 6 heures", "pendant la journée");

    // Tokenisation
    public static List<String> tokeniser(String phrase) {
        return Arrays.asList(phrase.toLowerCase().replace(",", "").split(" "));
    }

    // Analyse de toute la phrase
    public static void analyserPhrase(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("La phrase est vide ou incomplète.");
        }

        // Gestion duu groupe adverbiall
        if (tokens.size() > 1 && groupesAdverbiaux.contains(String.join(" ", tokens.subList(0, 2)))) {
            tokens.remove(0);
            tokens.remove(0);
        }

        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("La phrase manque un sujet ou un verbe.");
        }

        // analyserVerbe(tokens);

        // Identififfication du type de phrase
        if (contientVerbe(tokens)) {
            // j ai essaye d'analyser directement le complemment avant le sujet -> eerreur
            // analyserComplement(tokens);
            analyserVerbale(tokens);
            System.out.println("Type de phrase : verbale");
        } else {
            // tokens.remove(0);
            analyserNominale(tokens);
            System.out.println("Type de phrase : nominale");
        }

        if (!tokens.isEmpty()) {
            throw new IllegalArgumentException("Tokens inattendus restants : " + String.join(" ", tokens));
        }

        System.out.println("Phrase valide.");
    }

    // Analyse d une phrase nominale
    public static void analyserNominale(List<String> tokens) {
        if (contientVerbe(tokens)) {
            throw new IllegalArgumentException("Une phrase nominale ne peut pas contenir de verbe.");
        }
        analyserSujet(tokens);
    }

    // Analyse d une phrase verbale
    public static void analyserVerbale(List<String> tokens) {
        analyserSujet(tokens);
        analyserVerbe(tokens);

        // if (!tokens.isEmpty()) analyserComplement(tokens);

        // Imposer un complément ou un groupe temporel
        if (!tokens.isEmpty() && (articles.contains(tokens.get(0)) || groupesTemporels.contains(String.join(" ", tokens)))) {
            analyserComplementOuTemporel(tokens);
        } else {
            throw new IllegalArgumentException("Un complément ou un groupe temporel est attendu après le verbe.");
        }

        System.out.println("Phrase verbale détectée.");
    }

    // Analyse du sujet
    public static void analyserSujet(List<String> tokens) {
        if (tokens.isEmpty() || !articles.contains(tokens.get(0))) {
        
            throw new IllegalArgumentException("Un article est attendu au début du sujet.");
        }
        tokens.remove(0); // Supprimer l'article

        if (tokens.isEmpty() || !noms.contains(tokens.get(0))) {
            throw new IllegalArgumentException("Un nom est attendu après l'article.");
        }
        tokens.remove(0); // Supprimer le nom
    }

    // Analyse du verbe
    public static void analyserVerbe(List<String> tokens) {
        if (tokens.isEmpty() || !verbes.contains(tokens.get(0))) {
            throw new IllegalArgumentException("Un verbe est attendu.");
        }
        tokens.remove(0); // Supprimer le verbe
    }

    // Analyse d un complement ou un groupe temporel
    public static void analyserComplementOuTemporel(List<String> tokens) {
        if (!tokens.isEmpty() && groupesTemporels.contains(String.join(" ", tokens))) {
            tokens.clear();
        } else {
            analyserComplement(tokens);
        }
    }

    // Analyse du complement
    public static void analyserComplement(List<String> tokens) {
        if (tokens.isEmpty() || !articles.contains(tokens.get(0))) {
            throw new IllegalArgumentException("Un article est attendu au début du complément.");
        }
        tokens.remove(0);

        if (tokens.isEmpty() || !noms.contains(tokens.get(0))) {
            throw new IllegalArgumentException("Un nom est attendu après l'article dans le complément.");
        }
        tokens.remove(0); // Supprimer le nom
    }

    // Verification si les tokens contiennent un verbe
    public static boolean contientVerbe(List<String> tokens) {
        return tokens.stream().anyMatch(verbes::contains);
    }

    public static void main(String[] args) {
        String[] phrases = {
            "Le chat mange une souris",
            "Hier soir, un chien court dans la rue",
            "Chaque matin, la voiture charge du fromage",
            "Le téléphone sonne à 6 heures",
            "La souris mange le fromage",
            "Une voiture parle"
        };

        for (String phrase : phrases) {
            List<String> tokens = new ArrayList<>(tokeniser(phrase));
            System.out.println("Analyse de : '" + phrase + "'");
            try {
                analyserPhrase(tokens);
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            System.out.println();
        }
    }
}