/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import java.util.ArrayList;

//Used to build sample chore list if no backup file
public class SampleChores {

    public static ArrayList<Chore> addChores() {
        ArrayList<Chore> choreList = new ArrayList<Chore>();

        Chore cats = new Chore("Feed Cats");
        cats.addSubtask("Breakfast");
        cats.addSubtask("Dinner");
        cats.addSubtask("Litter Box");
        choreList.add(cats);

        Chore homework = new Chore("Homework");
        homework.addSubtask("Finish App");
        homework.addSubtask("Presentation");
        homework.addSubtask("Case Study 3");
        homework.addSubtask("Final exam questions");
        choreList.add(homework);

        Chore newLights = new Chore("New Light Fixtures");
        newLights.addSubtask("Hallway");
        newLights.addSubtask("Aidin's Closet");
        newLights.addSubtask("Office");
        newLights.addSubtask("Basement");
        choreList.add(newLights);

        Chore flooring = new Chore("Flooring");
        flooring.addSubtask("Laundry Room");
        flooring.addSubtask("Powder Room");
        flooring.addSubtask("Master Bath");
        flooring.addSubtask("Basement bedroom");
        choreList.add(flooring);

        Chore garage = new Chore("Garage");
        garage.addSubtask("New door");
        garage.addSubtask("Add outlets");
        garage.addSubtask("Back workbench");
        garage.addSubtask("Side workbench");
        choreList.add(garage);

        return choreList;
    }
}
