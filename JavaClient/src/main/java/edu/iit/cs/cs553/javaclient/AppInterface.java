package edu.iit.cs.cs553.javaclient;

/**
 *
 * @author Bo Feng
 */
public interface AppInterface {
    
    Boolean connect();
    
    Boolean cache(boolean turn);

    Boolean insert(String key, String value);

    Boolean check(String key);
    
    String find(String key);
    
    Boolean remove(String key);
    
    String[] listing();
}
