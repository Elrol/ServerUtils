package dev.elrol.serverutilities.api.enums;


import java.util.ArrayList;
import java.util.List;

public enum ClaimFlagKeys {
    allow_entry, allow_switch, allow_pickup;

    public static boolean contains(String s){
        for(ClaimFlagKeys key : values()){
            if(key.name().equals(s)) return true;
        }
        return false;
    }

    public static List<String> list(){
        List<String> flags = new ArrayList<>();
        for(ClaimFlagKeys flag : ClaimFlagKeys.values())
            flags.add(flag.name());
        return flags;
    }
}
