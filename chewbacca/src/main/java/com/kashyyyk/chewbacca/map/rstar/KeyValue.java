package com.kashyyyk.chewbacca.map.rstar;

import java.util.HashMap;
import java.util.HashSet;

/** Class used to store information on keys and their corresponding value(s).*/
public class KeyValue {

    private HashMap<String, HashSet<String>> KeyValue = new HashMap<>();

    /** Creates a map containing all relevant key-value pairs
     * @param kv - Array with key-value pairs. Each row in the array should contain two elements, the first one being the key and the second one being the value
     */
    public KeyValue(String[][] kv){
        if(kv!=null){
            for(int i=0;i<kv.length;i++){
                if(kv[i].length!=2) continue;
                add(kv[i][0],kv[i][1]);
            }
        }
    }

    /** Used to add a key-val pair into the structure
     * @param key - the key that should be stored
     * @param value - the value connected to the key to be stored
     */
    public void add(String key, String value){
        if(!this.KeyValue.containsKey(key)) this.KeyValue.put(key,new HashSet<>());
        this.KeyValue.get(key).add(value);
    }

    /** Used to see if a specific key exist in the KeyVal instance
     * @param key - The key to be checked for inside the KeyVal
     * @return Boolean value true if the key exist in KeyVal instance, otherwise false
     */
    public boolean containsKey(String key){return this.KeyValue.containsKey(key);}

    /** Check if a specific value is associated with any key in the KeyVal.
     * @param value - The value to be searched for
     * @return Boolean value true if the value is associated to at least one key, otherwise false
     */
    public boolean containsValue(String value){
        for(String key:this.KeyValue.keySet()){
            if(this.KeyValue.get(key).contains(value)) {
                return true;
            }
        }
        return false;
    }

    /** Getter for obtaining all values associated with a single key
     * @param key - The key that the values should be associated to
     * @return A HashSet of all the values associated with the given key
     */
    public HashSet<String> getValues(String key){ return this.KeyValue.get(key);}

    /** Getter for obtaining all keys stored in KeyVal instance
     * @return A HashSet of all the keys inside KeyVal instance
     */
    public HashSet<String> getKeys(){return (HashSet<String>) this.KeyValue.keySet();}

    /** Used to obtain readable representation of the KeyVal instance
     * @return String with all key-value pairs in KeyVal instance
     */
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
