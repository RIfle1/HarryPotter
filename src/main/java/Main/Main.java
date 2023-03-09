package Main;

import Enums.Core;
import Enums.Pet;

public class Main {
    public static void main(String[] args) {
        for(String core:Core.getCoreList()) {
            System.out.println(core);
        }

        for(String pet:Pet.getPetList()) {
            System.out.println(pet);
        }
    }
}