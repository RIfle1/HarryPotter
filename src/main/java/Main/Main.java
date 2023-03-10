package Main;

import AbstractClasses.AbstractCharacter;
import Classes.Enemy;
import Classes.Spell;

import javax.swing.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

import static Main.ConsoleLogic.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        gameCredits();
        checkSaves();
        characterCreation();
    }
}