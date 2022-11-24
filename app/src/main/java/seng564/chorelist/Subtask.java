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
        int event = xpp.next();
        while(event != XmlPullParser.END_TAG){
            switch(event){
                case XmlPullParser.START_TAG:
                    processTag(xpp);
                    break;
                //Add more cases as the XML document grows more complex
                default:
                    Log.d("XML.Subtask","Unknown event: "+event);
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
                xpp.next();
            //Add more cases as the XML doc grows more complex
            default:
                Log.d("XML.Subtask", "Unknown Tag: "+xpp.getName());
                xpp.next();
                break;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isComplete() {
        return this.isComplete;
    }

    public void markComplete() {
        this.isComplete = true;
        Log.d("Subtask", "Completed: "+this.title);
    }

    public void markActive(){
        this.isComplete = false;
    }

    public String toString(){
        return this.title;
    }

    public void writeXML(XmlSerializer xml) throws IOException {
        Log.d("XML.Subtask","Writing subtask: "+title);
        xml.startTag(null, "Subtask");
        xml.startTag(null, "Title");
        xml.text(title);
        xml.endTag(null,"Title");
        xml.endTag(null,"Subtask");
    }

}
