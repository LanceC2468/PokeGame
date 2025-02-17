package PokemonData.src;
import java.util.*;

public class Pokemon {
    private String name;
    private String type1 = "";
    private String type2 = "";
    
    public Pokemon(String primaryName, String type1, String type2) {
        this.name = primaryName;
        this.type1 = type1;
        this.type2 = type2;
    }
    public Pokemon(String primaryName, String type1){
        this.name = primaryName;
        this.type1 = type1;
        type2 = "None";
    }
    public Pokemon(String primaryName){
        this.name = primaryName;
        type1 = "None";
        type2 = "None";
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
    public String getTypesPretty() {
        if(type2.equals("None")) return type1;
        else return getTypes();
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
                if (i){
                    if(type1.equals("None"))type1 = type.strip();
                    type1.concat(type.strip());
                }
                else{
                    if(type2.equals("None"))type2 = type.strip();
                    type2.concat(type.strip());
                }
                i = !i; // alternate typing.
            } // interesting edge case (and possible future bug) where pokemon can have
              // several forms and be monotype
        }
    }
    @Override
    public String toString(){
        return name + " " + getTypesPretty();
    }
    public boolean checkTypes(String t){
        if(t.equals(this.type1) || t.equals(this.type2)) return true;
        return false;
    }
    public char getLastLetter(){
        char LL = ' ';
        int i = 1;
        while (!Character.isLetter(name.charAt(name.length()-1))) {
           LL = name.charAt(name.length()-i);
           i++;
        }
        return LL;
    }
    
}