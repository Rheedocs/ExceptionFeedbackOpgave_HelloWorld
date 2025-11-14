import exceptions.NegativeAmountException;
import exceptions.NotEnoughCreditsException;
import exceptions.PlayerNotFoundException;
import models.LootCrate;
import models.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LootCrateSystem {

    // Map til at gemme spillere efter navn
    private final Map<String, Player> players = new HashMap<>();

    // Map til at gemme crates efter id
    private final Map<String, LootCrate> crates = new HashMap<>();

    public static void main(String[] args) {
        LootCrateSystem app = new LootCrateSystem();
        app.seed();        // Opretter data til test
        app.runTests();    // Kører scenarierne beskrevet i opgaven
        app.runMenu();     // Køre en simpel menu der kan vælge en spiller, åbne crates, tilføje credits og se inventory
    }

    // Opretter spillere og crates
    private void seed() {
        players.put("tim", new Player("Tim", 100));
        players.put("robert", new Player("Robert", 20));

        crates.put("basic", new LootCrate("Basic", 25, List.of("Wooden Sword", "Health Potion", "5 Gems")));
        crates.put("epic", new LootCrate("Epic", 75, List.of("Epic Sword", "Armor Chest", "50 Gems")));
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
            Player tim = findPlayer("tim");
            LootCrate basic = crates.get("basic");
            String reward = basic.openFor(tim);
            System.out.println(tim.getUsername() + " åbnede en " + basic.getId() +
                    " loot crate, og fik: " + reward + ", Credits tilbage: " + tim.getCredits());
        } catch (PlayerNotFoundException | NotEnoughCreditsException e) {
            System.out.println("Fejl i test 1: " + e.getMessage());
        }
        System.out.println("--------------------------------------");
        System.out.println();

        // Scenarie 2, en spiller har ikke nok credits
        System.out.println("------------- Scenarie 2 -------------");
        try {
            Player robert = findPlayer("robert");
            LootCrate epic = crates.get("epic");
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
            Player tim = findPlayer("tim");
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
            Player mikkel = findPlayer("mikkel");
            LootCrate basic = crates.get("basic");
            String reward = basic.openFor(mikkel);
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
            System.out.println("--------------------------------------");
        }

        System.out.println("======================================");
        System.out.println("              Slut tests              ");
        System.out.println("======================================");
    }

    // Laver en menu med try/catch
    public void runMenu() {
        Scanner scanner = new Scanner(System.in);
        Player currentPlayer = null;
        String choice = "";

        while (!choice.equals("0")) {

            System.out.println("======================================");
            System.out.println("             LootCrate Menu           ");
            System.out.println("======================================");
            System.out.println("1. Vælg spiller");
            System.out.println("2. Åbn crate");
            System.out.println("3. Tilføj credits");
            System.out.println("4. Se inventory");
            System.out.println("0. Afslut");
            System.out.print("Valg: ");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Skriv spillernavn: ");
                    String name = scanner.nextLine().trim();
                    try {
                        currentPlayer = findPlayer(name.toLowerCase().trim());
                        System.out.println("Valgt spiller: " + currentPlayer.getUsername());
                    } catch (PlayerNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "2":
                    if (currentPlayer == null) {
                        System.out.println("Vælg en spiller først.");
                        break;
                    }
                    System.out.println("Tilgængelige crates: " + crates.keySet());
                    System.out.print("Skriv crate id: ");
                    String crateId = scanner.nextLine().trim().toLowerCase();
                    LootCrate crate = crates.get(crateId);
                    if (crate == null) {
                        System.out.println("Crate findes ikke.");
                        break;
                    }
                    try {
                        String reward = crate.openFor(currentPlayer);
                        System.out.println("Fik: " + reward);
                    } catch (NotEnoughCreditsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "3":
                    if (currentPlayer == null) {
                        System.out.println("Vælg en spiller først.");
                        break;
                    }
                    System.out.print("Hvor mange credits vil du tilføje: ");
                    try {
                        int amount = Integer.parseInt(scanner.nextLine());
                        currentPlayer.addCredits(amount);
                        System.out.println("Ny saldo: " + currentPlayer.getCredits());
                    } catch (NegativeAmountException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "4":
                    if (currentPlayer == null) {
                        System.out.println("Vælg en spiller først.");
                        break;
                    }
                    System.out.println(currentPlayer.getUsername() + " inventory: " + currentPlayer.getInventory());
                    break;

                case "0":
                    System.out.println("Lukker program...");
                    break;

                default:
                    System.out.println("Ugyldigt valg.");
            }
        }
    }
}
