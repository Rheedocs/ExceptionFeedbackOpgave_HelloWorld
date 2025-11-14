package models;

import exceptions.NegativeAmountException;
import exceptions.NotEnoughCreditsException;

import java.util.ArrayList;
import java.util.List;

public class Player {

    // Spillernavn
    private final String username;

    // Spillernes mængde credits
    private int credits;

    // Inventory til de ting spilleren har fået fra crates
    private final List<String> inventory = new ArrayList<>();

    // Konstruktør, starter med et givet antal credits
    public Player(String username, int startingCredits) {
        this.username = username;
        this.credits = Math.max(0, startingCredits);
    }

    public String getUsername() {return username;}

    public int getCredits() {return credits;}

    // Unchecked exception, her må man ikke tilføje negative credits
    public void addCredits(int amount) {
        if (amount < 0) {
            throw new NegativeAmountException("Kan ikke tilføje negative credits: " + amount);
        }
        this.credits += amount;
    }

    // Checked exception, denne metode kastes videre, da en crate kan være for dyr
    public void spendCredits(int price) throws NotEnoughCreditsException {
        if (price < 0) {
            throw new NegativeAmountException("Pris kan ikke være negativ: " + price);
        }
        if (credits < price) {
            throw new NotEnoughCreditsException("Ikke nok credits, har " + credits + ", pris " + price);
        }
        this.credits -= price;
    }

    // Tilføj et item til inventory
    public void addItem(String item) {inventory.add(item);}

    // Mulighed for at se inventory udefra, fx i systemklassen
    public List<String> getInventory() {return inventory;}

    @Override
    public String toString() {
        return "Player: " + username + " | credits: " + credits;
    }
}
