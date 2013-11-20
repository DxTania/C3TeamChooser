package org.ieee.c3.teamchooser.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class which represents a single person
 */
public class Person {
    public static int UID = 0;
    public static int NAME = 1;
    public static int EMAIL = 2;
    public static int EXP = 3;

    private String exp;
    private String uid;
    private String name;
    private String email;
    private List<String> preferences;


    public Person(String p[]) {
        this.uid = p[UID];
        this.name = p[NAME];
        this.email = p[EMAIL];
        this.exp = p[EXP];
        this.preferences = new ArrayList<String>();
    }

    public Person(String personString) {
        String p[] = personString.split(",");
        this.uid = p[UID];
        this.name = p[NAME];
        this.email = p[EMAIL];
        this.exp = p[EXP];
        if (p.length > 4)
            this.preferences = Arrays.asList(p[4].split("/"));
        else
            this.preferences = new ArrayList<String>();
    }

    public void addPreference(String p) {
        if (p.equalsIgnoreCase(getName())) {
            return;
        }
        preferences.add(p);
    }

    /**
     * Creates a string delimited by ,
     *
     * @return The string representing the current person
     */
    @Override
    public String toString() {
        String prefs = "";
        if (preferences.size() > 0) {
            prefs += preferences.get(0);
            if (preferences.size() > 1) {
                prefs += "/" + preferences.get(1);
            }
        }
        return uid + "," + name + "," + email + "," + exp + "," + prefs;
    }

    public double getExp() {
        return Double.valueOf(exp);
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAbbrName() {
        int lastName = 1;
        // Index of last name initial
        while (name.charAt(lastName - 1) != ' ' && lastName < name.length()) {
            lastName++;
        }
        return name.substring(0, ++lastName) + ".";
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPreferences() {
        return preferences;
    }
}
