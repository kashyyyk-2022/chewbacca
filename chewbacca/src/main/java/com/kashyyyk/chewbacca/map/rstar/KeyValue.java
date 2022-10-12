package com.kashyyyk.chewbacca.map.rstar;

import java.util.HashMap;
import java.util.HashSet;

public class KeyValue {

    private HashMap<String, HashSet<String>> KeyValue = new HashMap<>();

    /** Creates a map containing all relevant key-value pairs
     * @param kv - Array with key-value pairs. Each row in the array should contain two elements, the first one being the key and the second one being
     */
    public KeyValue(String[][] kv){
        if(kv!=null){
            for(int i=0;i<kv.length;i++){
                if(kv[i].length!=2) continue;
                add(kv[i][0],kv[i][1]);
            }
        }
    }

    public void add(String key, String value){
        if(!this.KeyValue.containsKey(key)) this.KeyValue.put(key,new HashSet<>());
        this.KeyValue.get(key).add(value);
    }

    public boolean containsKey(String key){return this.KeyValue.containsKey(key);}

    public boolean containsValue(String value){
        for(String key:this.KeyValue.keySet()){
            if(this.KeyValue.get(key).contains(value)) {
                return true;
            }
        }
        return false;
    }

    public HashSet<String> getValues(String key){ return this.KeyValue.get(key);}

    public HashSet<String> getKeys(){return (HashSet<String>) this.KeyValue.keySet();}

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String key:this.KeyValue.keySet()){
            for(String val:this.KeyValue.get(key)){
                sb.append("Key:"+key+" Val:"+val+"\n");
            }
        }
        return sb.toString();
    }
}
