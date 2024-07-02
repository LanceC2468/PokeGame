package PokemonData.src;

public class Poketest {
    public static void main(String[] args) {
        String testPokemon = "Bulbasaur";
        String testTypes = "\nGrass\nPoison";

        Pokemon p = new Pokemon(testPokemon, testTypes);
        System.out.println(p.getName() + " " + p.getType1() + " " + p.getType2());
    }
}
