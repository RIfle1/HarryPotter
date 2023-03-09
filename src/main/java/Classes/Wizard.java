package Classes;

import Enums.Pet;

import java.util.List;

public class Wizard {
    Pet pet;
    Wand wand;
    House house;
    List<Spells> knownSpells;
    List<Potions> potions;

    public Wizard(Pet pet, Wand wand, House house, List<Spells> knownSpells, List<Potions> potions) {
        this.pet = pet;
        this.wand = wand;
        this.house = house;
        this.knownSpells = knownSpells;
        this.potions = potions;
    }

    
}
