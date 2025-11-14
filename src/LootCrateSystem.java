import exceptions.NegativeAmountException;
import exceptions.NotEnoughCreditsException;
import exceptions.PlayerNotFoundException;
import models.LootCrate;
import models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootCrateSystem {

    // Map til at gemme spillere efter navn
    private final Map<String, Player> players = new HashMap<>();

    // Map til at gemme crates efter id
    private final Map<String, LootCrate> crates = new HashMap<>();

    public static void main(String[] args) {
        LootCrateSystem app = new LootCrateSystem();
        app.seed();        // Opretter data til test
        app.runTests();    // Kører scenarierne beskrevet i opgaven
    }

    // Opretter spillere og crates
    private void seed() {
        players.put("Tim", new Player("Tim", 100));
        players.put("Robert", new Player("Robert", 20));

        crates.put("Basic", new LootCrate("Basic", 25, List.of("Wooden Sword", "Health Potion", "5 Gems")));
        crates.put("Epic", new LootCrate("Epic", 75, List.of("Epic Sword", "Armor Chest", "50 Gems")));
    }

    // Finder en spiller, kaster PlayerNotFoundException hvis spilleren ikke findes
    private Player findPlayer(String username) throws PlayerNotFoundException {
        Player p = players.get(username);
        if (p == null) {
            throw new PlayerNotFoundException("Spiller findes ikke: " + username);
        }
        return p;
    }

    // Kører de fire scenarier som opgaven kræver
    private void runTests() {
        System.out.println("======================================");
        System.out.println("            LootCrate Tests           ");
        System.out.println("======================================");
        System.out.println();

        // Scenarie 1, en spiller åbner en crate
        System.out.println("------------- Scenarie 1 -------------");
        try {
            Player tim = findPlayer("Tim");
            LootCrate basic = crates.get("Basic");
            String reward = basic.openFor(tim);
            System.out.println(tim.getUsername() + " åbnede " + basic.getId() +
                    " og fik: " + reward + ", Credits tilbage: " + tim.getCredits());
        } catch (PlayerNotFoundException | NotEnoughCreditsException e) {
            System.out.println("Fejl i test 1: " + e.getMessage());
        }
        System.out.println("--------------------------------------");
        System.out.println();

        // Scenarie 2, en spiller har ikke nok credits
        System.out.println("------------- Scenarie 2 -------------");
        try {
            Player robert = findPlayer("Robert");
            LootCrate epic = crates.get("Epic");
            String reward = epic.openFor(robert);
            System.out.println("Uventet succes: " + reward);
        } catch (PlayerNotFoundException e) {
            System.out.println("Fejl i test 2, mangler spiller: " + e.getMessage());
        } catch (NotEnoughCreditsException e) {
            System.out.println("Korrekt fanget fejl: " + e.getMessage());
        } finally {
            System.out.println("Test 2 afsluttet");
        }
        System.out.println("--------------------------------------");
        System.out.println();

        // Scenarie 3, negative credits, unchecked exception
        System.out.println("------------- Scenarie 3 -------------");
        try {
            Player tim = findPlayer("Tim");
            tim.addCredits(-10);
            System.out.println("Uventet, negative credits accepteret");
        } catch (PlayerNotFoundException e) {
            System.out.println("Fejl i test 3, mangler spiller: " + e.getMessage());
        } catch (NegativeAmountException e) {
            System.out.println("Korrekt fanget unchecked fejl: " + e.getMessage());
        }
        System.out.println("--------------------------------------");
        System.out.println();

        // Scenarie 4, spiller findes ikke
        System.out.println("------------- Scenarie 4 -------------");
        try {
            Player ghost = findPlayer("Ghost");
            LootCrate basic = crates.get("Basic");
            String reward = basic.openFor(ghost);
            System.out.println("Uventet succes: " + reward);
        } catch (PlayerNotFoundException e) {
            System.out.println("Korrekt fanget fejl: " + e.getMessage());
        } catch (NotEnoughCreditsException e) {
            System.out.println("Fejl i test 4, dette burde ikke ske: " + e.getMessage());
        }
        System.out.println("--------------------------------------");
        System.out.println();


        System.out.println("======================================");
        System.out.println("       Inventory for hver spiller     ");
        System.out.println("======================================");

        for (Player p : players.values()) {
            System.out.println(p.getUsername() + " inventory: " + p.getInventory());
        }

        System.out.println("======================================");
        System.out.println("              Slut tests              ");
        System.out.println("======================================");
    }
}
