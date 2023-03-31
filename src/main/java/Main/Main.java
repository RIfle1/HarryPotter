package Main;

import Classes.Potion;
import Classes.Spell;
import Classes.Wizard;
import Enums.Level;
import Functions.CharacterCreation;

import java.util.Arrays;

import static Classes.Color.ANSI_RED;
import static Classes.Color.returnColoredText;
import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
import static Functions.ConsoleFunctions.*;
import static Functions.GeneralFunctions.generateRandomString;
import static Functions.SaveFunctions.*;
import static java.util.Arrays.stream;

public class Main {
    public static void main(String[] args) {
        printMainScreen();
        checkSaves();
        chooseAction();
    }
}