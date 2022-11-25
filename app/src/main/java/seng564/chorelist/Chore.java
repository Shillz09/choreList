/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

//Chore Object
public class Chore {

    //Title of Chore
    private String title;
    //True = Complete; False = Active
    private boolean isComplete;
    //ArrayList of Subtasks
    private ArrayList<Subtask> subtasks;

    //TODO: Implement Occurrence
    public Chore(String title){
        this.title = title;
        this.isComplete = false;
        this.subtasks = new ArrayList<Subtask>();
    }

    //Constructor when reading XML file
    public Chore(XmlPullParser xpp) throws XmlPullParserException, IOException{
        //Initialize local variables
        this.title = "";
        this.isComplete = false;
        this.subtasks = new ArrayList<Subtask>();
        //We should only be here if the current pointer is at START_TAG <Chore>
        while(xpp.getEventType() != XmlPullParser.END_TAG){
            switch(xpp.getEventType()){
                case XmlPullParser.START_TAG:
                    processTag(xpp);
                    xpp.next();
                    break;
                //Add more cases as the XML document grows more complex
                default:
                    Log.d("XML.Chore","Unknown event: "+xpp.getEventType());
                    xpp.next();
                    break;
            }
        }
        //Debugging IF blocks
        if(title.equals("")){
            Log.d("XML.Chore","Did not get Chore title");
        }
        if(!xpp.getName().equals("Chore")){
            Log.d("XML.Chore","Did not end on </Chore>");
        }
        //End on </Chore>
    }

    //Process the name of this XML Tag
    private void processTag(XmlPullParser xpp) throws XmlPullParserException, IOException {
        switch (xpp.getName()) {
            //Set title
            case "Title":
                this.title = xpp.nextText(); //Ends on </Title>
                break;
            //Let's get this party started!
            case "Chore":
                break;
            //Add Subtask
            case "Subtask":
                this.subtasks.add(new Subtask(xpp)); //Ends on </Subtask>
                break;

            //Add more cases as the XML doc grows more complex
            default:
                Log.d("XML.Chore", "Unknown Tag: "+xpp.getName());
                break;
        }
    }

    //Retrun title
    public String getTitle() {
        return title;
    }

    //Return boolean
    //True = Complete; False = Active
    public boolean isComplete() {
        return this.isComplete;
    }

    //Mark Chore & all subtasks as complete
    public void markComplete() {
        this.isComplete = true;
        for(Subtask s:this.subtasks){s.markComplete();}
    }

    //Mark as active
    //NOTE: This does NOT re-activate subtasks
    public void markActive(){
        this.isComplete = false;
    }

    //Get a subtask by position (Used by List Adapter)
    public Subtask getSubtask(int i) {
        return subtasks.get(i);
    }

    //Return all Subtasks
    public ArrayList<Subtask> getSubtaskArray() {
        return this.subtasks;
    };

    //Add a Subtask by object
    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    //Add a Subtask by title
    public void addSubtask(String subtitle){
        subtasks.add(new Subtask(subtitle));
    }

    //Return count of subtasks
    public int countSubtasks() {
        return subtasks.size();
    }

    //Same as getTitle(), for now
    public String toString(){
        return this.title;
    }

    //Write this object to XML
    //File object should already be added to XmlSerializer by ChoreList
    public void writeXML(XmlSerializer xml) throws IOException {
        //Start Chore.
        xml.startTag(null, "Chore");
        //Title
        xml.startTag(null, "Title");
        xml.text(title);
        xml.endTag(null, "Title");
        //Let each Subtask write itself
        for(Subtask s:subtasks){
            s.writeXML(xml);
        }
        //Close Chore
        xml.endTag(null, "Chore");
    }

} //End Class
