package org.ieee.c3.teamchooser.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rawrtan on 11/14/13.
 */
public class Team {
    private List<Person> people;
    private int avgExp;

    public Team() {
        people = new ArrayList<Person>();
        avgExp = 0;
    }

    public void addPerson(Person p) {
        people.add(p);
        avgExp = (avgExp + Integer.valueOf(p.getExp())) / people.size();
    }

    public List<Person> getPeople() {
        return people;
    }

    public int getAvgExp() {
        return avgExp;
    }
}
