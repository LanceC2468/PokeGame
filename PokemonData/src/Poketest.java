package PokemonData.src;
import java.io.*;
import java.util.*;

public class Poketest {
    public static void main(String[] args) {
        String testPokemon = "Bulbasaur";
        String testTypes = "\n\nGrass\nPoison";
        Pokemon p = new Pokemon(testPokemon, testTypes);
        System.out.println(p.getName() + " " + p.getType1() + " " + p.getType2());

        
        ArrayList<Pokemon> plist = new ArrayList<Pokemon>();
        File names_types_txt = new File("C:\\Desktop\\PokeGame\\PokemonData\\name_types.txt");
        //File names_types_txt = new File("../../../name_types.txt");  //see file for data format
        int i = 0;
        try{  //this is broken!  fix this!
            Scanner dex = new Scanner(names_types_txt);
            dex.useDelimiter("\n");
            while(dex.hasNext()){  
                //0.)Throw away Ndex #
                String data = dex.next();  //temporary data
                String name_string;
                String types_string;

                if(data.isBlank()) continue;
                data = dex.next();  //throw away Ndex #
                // 1.)Extract Name
                name_string = dex.next();

                //2.)Extract Type
                dex.useDelimiter("#");
                types_string = dex.next();

                //3.)Revert any Scanner changes
                dex.useDelimiter("\n");
                dex.next();

                //4.) Add pokemon to the list
                Pokemon pkmn = new Pokemon(name_string, types_string);
                plist.add(pkmn);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        

        Scanner s = new Scanner(System.in);
        System.out.println("press Enter to exit the program");
        s.nextLine();
    }
}
