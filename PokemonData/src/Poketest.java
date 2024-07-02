package PokemonData.src;
import java.util.Scanner;

public class Poketest {
    public static void main(String[] args) {
        String testPokemon = "Bulbasaur";
        String testTypes = "\n\nGrass\nPoison";

        Pokemon p = new Pokemon(testPokemon, testTypes);
        System.out.println(p.getName() + " " + p.getType1() + " " + p.getType2());

        Pokemon[] plist = new Pokemon[20];
        

        Scanner s = new Scanner(System.in);
        System.out.println("press Enter to exit the program");
        s.nextLine();
    }
}
