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
import java.util.Arrays;

public class Subtask {

    //Title of Task
    private String title;
    //True = Complete; False = Active
    private Boolean isComplete;


    public Subtask(String title) {
        this.title = title;
        this.isComplete = false;
    }

    public Subtask(XmlPullParser xpp) throws XmlPullParserException, IOException {
        //Initialize Local vars
        this.title = "";
        this.isComplete = false;
        //We should only be here on START_TAG <Subtask>
        //Process until END_TAG </Subtask>
        while(xpp.getEventType() != XmlPullParser.END_TAG){
            switch(xpp.getEventType()){
                case XmlPullParser.START_TAG:
                    processTag(xpp);
                    xpp.next();
                    break;

                //Add more cases as the XML document grows more complex
                default:
                    Log.d("XML.Subtask","Unknown event: "+xpp.getEventType());
                    xpp.next();
                    break;
            }
        }
        if(title.equals("")){
            Log.d("XML.Subtask","Did not get Subtask title");
        }
        if(!xpp.getName().equals("Subtask")){
            Log.d("XML.Subtask","Did not end on </Subtask>");
        }
        //End on </Subtask>
    }

    private void processTag(XmlPullParser xpp) throws XmlPullParserException, IOException {
        switch (xpp.getName()) {
            case "Title":
                this.title = xpp.nextText(); //Ends on </Title>
                break;
            //Let's Gooooooooo!   (Mountaineers!)
            case "Subtask":
                break;
            //Add more cases as the XML doc grows more complex
            //Parent loop will move to next element
            default:
                Log.d("XML.Subtask", "Unknown Tag: "+xpp.getName()+" Event: "+xpp.getEventType());
                break;
        }
    }

    //Return title
    public String getTitle() {
        return title;
    }
    //Return boolean
    //True = Complete; False = Active
    public Boolean isComplete() {
        return this.isComplete;
    }
    //Set boolean true
    public void markComplete() {
        this.isComplete = true;
    }
    //Set boolean false
    public void markActive(){
        this.isComplete = false;
    }
    //Returns title for now
    public String toString(){
        return this.title;
    }
    //Write XML
    //XmlSerializer should already have output file set by ChoreList
    public void writeXML(XmlSerializer xml) throws IOException {
        //Start Subtask
        xml.startTag(null, "Subtask");
        //Title
        xml.startTag(null, "Title");
        xml.text(title);
        xml.endTag(null,"Title");
        //End Subtask
        xml.endTag(null,"Subtask");
    }

}
