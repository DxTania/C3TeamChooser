package org.ieee.c3.teamchooser.components;

import java.util.ArrayList;
import java.util.List;

/**
 * The class that represents a team of 3-4 people
 */
public class Team {
    private List<Person> people;
    private double avgExp;
    private double totalExp;

    /**
     * Creates a team with no people
     */
    public Team() {
        people = new ArrayList<Person>();
        avgExp = 0;
    }

    /**
     * Adds a person to the team and updates exp
     *
     * @param p The person to add
     */
    public void addPerson(Person p) {
        people.add(p);
        totalExp += p.getExp();
        avgExp = totalExp / people.size();
    }

    public List<Person> getPeople() {
        return people;
    }

    public double getAvgExp() {
        return avgExp;
    }

    @Override
    public String toString() {
        String text = "Avg Exp: " + String.format("%.2f", avgExp) + "\n";
        for (Person p : people) {
            text += "-" + p.getName() + ": " + String.valueOf(p.getExp()) + "\n";
        }
        return text;
    }
}
