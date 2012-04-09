package edu.iit.cs.cs553.javaclient;

/**
 * A Simple Java client of App on Google App Engine.
 *
 */
public class App {
    static String baseUrl = "http://save-files.appspot.com";

    public static boolean testConnection() {
        AppInterface ai = new JavaDelegator(baseUrl);

        return ai.connect();
    }

    public static boolean testInsert() {
        AppInterface ai = new JavaDelegator(baseUrl);

        return ai.insert("foo", "bar");
    }

    public static boolean testCheck() {
        AppInterface ai = new JavaDelegator(baseUrl);

        boolean stage1 = ai.insert("foo", "bar") == ai.check("foo");
        
        boolean stage2 = ai.check("bar");
        
        if(stage1==true && stage2 == false)
            return true;
        else
            return false;
    }

    public static boolean testRemove() {
        AppInterface ai = new JavaDelegator(baseUrl);

        boolean stage1 = ai.insert("foo", "bar") == ai.check("foo");

        boolean stage2 = ai.remove("foo");

        boolean stage3 = ai.check("foo");

        if (stage1 == true && stage2 == true && stage3 == false) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Connection: " + ( testConnection() ? "OK" : "Failed" ));
        System.out.println("Insert: " + ( testInsert() ? "OK" : "Failed" ));
        System.out.println("Check: " + ( testCheck() ? "OK" : "Failed" ));
        System.out.println("Remove: " + ( testRemove() ? "OK" : "Failed" ));
    }
}
