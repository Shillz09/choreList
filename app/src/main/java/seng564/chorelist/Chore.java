package seng564.chorelist;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;

public class Chore {

    //Title of Chore
    private String title;
    //True = Complete; False = Active
    private boolean isComplete;

    private ArrayList<Subtask> subtasks;

    //TODO: Implement Occurrence
    //private Occurrence occurrence

    public Chore(String title){
        this.title = title;
        this.isComplete = false;
        this.subtasks = new ArrayList<Subtask>();
    }

    public Chore(XmlPullParser xpp) throws XmlPullParserException, IOException{
        //Initialize local variables
        this.title = "";
        this.isComplete = false;
        this.subtasks = new ArrayList<Subtask>();
        //We should only be here if the current pointer is at START_TAG <Chore>
        int event = xpp.next();
        while(event != XmlPullParser.END_TAG){
            switch(event){
                case XmlPullParser.START_TAG:
                    processTag(xpp);
                    break;
                //Add more cases as the XML document grows more complex
                default:
                    Log.d("XML.Chore","Unknown event: "+event);
                    xpp.next();
                    break;
            }
        }
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
            case "Title":
                this.title = xpp.nextText(); //Ends on </Title>
                xpp.next();
                break;
            case "Subtask":
                this.subtasks.add(new Subtask(xpp)); //Ends on </Subtask>
                xpp.next();
                break;
            //Add more cases as the XML doc grows more complex
            default:
                Log.d("XML.Chore", "Unknown Tag: "+xpp.getName());
                xpp.next();
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public void markComplete() {
        this.isComplete = true;
        for(Subtask s:this.subtasks){s.markComplete();}
    }

    public void markActive(){
        this.isComplete = false;
    }

    public Subtask getSubtask(int i) {
        return subtasks.get(i);
    }

    public ArrayList<Subtask> getSubtaskArray() {
        return this.subtasks;
    };

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void addSubtask(String subtitle){
        subtasks.add(new Subtask(subtitle));
    }

    public int countSubtasks() {
        return subtasks.size();
    }

    public String toString(){
        return this.title;
    }

    public void writeXML(XmlSerializer xml) throws IOException {
        Log.d("XML.Chore","Writing chore: "+title);

        xml.startTag(null, "Chore");
        xml.startTag(null, "Title");
        xml.text(title);
        xml.endTag(null, "Title");
        for(Subtask s:subtasks){
            s.writeXML(xml);
        }
        xml.endTag(null, "Chore");
    }

}
