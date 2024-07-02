package PokemonData.src;
import java.util.*;

public class Pokemon {
    private String name;
    private String type1;
    private String type2;
    
    public Pokemon(String primaryName, String types) {
        name=primaryName;
        StringTokenizer t = new StringTokenizer(types, "\n");
        boolean i = true;
        while(t.hasMoreElements()){
            if(t.toString().contains(name)) continue;  //eliminates parsing RatataAlolanForm by detecting Ratata
            else{  //else parse the types
                if(i) type1 += t.toString();
                else type2 += t.toString();
                i = !i; //alternate typing.
            }  //interesting edge case (and possible future bug) where pokemon can have several forms and be monotype
            t.nextToken();
        }
    }
    public String getName(){
        return name;
    }
    public String getType1(){
        return type1;
    }
    public String getType2(){
        return type2;
    }
}