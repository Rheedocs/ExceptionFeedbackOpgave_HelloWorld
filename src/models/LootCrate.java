package models;

import exceptions.NotEnoughCreditsException;

import java.util.List;
import java.util.Random;

public class LootCrate {

    // Crate id og pris
    private final String id;
    private final int price;

    // Loot table med mulige rewards
    private final List<String> lootTable;
    private final Random rng = new Random();

    public LootCrate(String id, int price, List<String> lootTable) {
        this.id = id;
        this.price = price;
        this.lootTable = lootTable;
    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    // Åbner en crate, kræver at spilleren har nok credits, ellers kastes fejl
    public String openFor(Player player) throws NotEnoughCreditsException {
        player.spendCredits(price);
        int idx = rng.nextInt(lootTable.size());
        String reward = lootTable.get(idx);

        // Læg reward i spillerens inventory
        player.addItem(reward);

        return reward;
    }

    @Override
    public String toString() {
        return "LootCrate: " + id + " | price: " + price;
    }
}
