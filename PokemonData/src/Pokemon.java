package PokemonData.src;
import java.util.*;

public class Pokemon {
    private String name;
    private String type1 = "";
    private String type2 = "";
    
    public Pokemon(String primaryName, String types) {
        setName(primaryName);
        setTypes(types);
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
    public String getTypes(){
        return type1 + " " + type2;
    }
    public void setName(String n){
        name = n.strip();
    }
    public void setType1(String t){
        type1 = t.strip();
    }
    public void setType2(String t){
        type2 = t.strip();
    }
    public void setTypes(String t){
        StringTokenizer tkn = new StringTokenizer(t, "\n");
        boolean i = true;
        while (tkn.hasMoreTokens()) {
            String type = tkn.nextToken();
            if(type.isEmpty()) continue;
            if (type.contains(name))
                continue; // eliminates parsing RatataAlolanForm by detecting Ratata
            else { // else parse the types
                if (i)
                    type1 += type.strip();
                else
                    type2 += type.strip();
                i = !i; // alternate typing.
            } // interesting edge case (and possible future bug) where pokemon can have
              // several forms and be monotype
        }
    }
}