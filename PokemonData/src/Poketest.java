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

            String name_string = "";
            String types_string;
            while(dex.hasNext()){  
                String data = dex.next();  //temporary data

                if(data.contains("#"))continue;  //throw away Ndex #
                if(data.isBlank())continue;  //throw away newline characters
                if(name_string.isEmpty()) { //first string is name entry
                    name_string = data;
                    dex.useDelimiter("#");  //rest of string is types
                    types_string = dex.next();
                    dex.useDelimiter("\n");

                    Pokemon pkmn = new Pokemon(name_string);
                    pkmn.setTypes(types_string.strip());
                    plist.add(pkmn);
                    name_string = "";
                    types_string = "";
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        
        for(int j = 0; i < 55; i++){
            System.out.println(plist.get(i).getName() + "\t\t" + plist.get(i).getTypesPretty());
        }

        Scanner s = new Scanner(System.in);
        System.out.println("press Enter to exit the program");
        s.nextLine();
    }
}
