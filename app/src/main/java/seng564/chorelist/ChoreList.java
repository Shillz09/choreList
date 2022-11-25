/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

//ChoreList object
//Represents global/static ArrayList of Chores
public class ChoreList {

    private static ArrayList<Chore> chores;
    private static File backupFile;

    //Empty constructor
    public ChoreList() {this.chores = new ArrayList<Chore>(); }
    //Contructor from pre-existing ArrayList
    public ChoreList(ArrayList<Chore> c){
        this.chores = c;
    }
    //Constructor from XML file
    public ChoreList(File f){
        //Initialize variables
        this.backupFile = f;
        this.chores = new ArrayList<Chore>();
        readFile();
    }
    //Return chore by position
    public static Chore getChore(int t){
        return chores.get(t);
    }
    //Return # of chores
    public static int size(){
        return chores.size();
    }
    //Add new chore
    public static void addChore(Chore t){
        chores.add(t);
    }
    //Update existing chore (Used by Edit Chore screen)
    public static void updateChore(Chore oldChore, Chore newChore){
        int idx = chores.indexOf(oldChore);
        chores.remove(oldChore);
        chores.add(idx, newChore);
    }
    //Delete Chore
    public static void removeChore(Chore t){
        chores.remove(t);
    }
    //Set backup file
    public static void setBackupFile(File f){
        backupFile = f;
    }
    //Read XML file and build list
    public static void readFile(){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new FileReader(backupFile));
            //Move past Start Document
            xpp.next();
            //Loop until end of document
            while(xpp.getEventType() != XmlPullParser.END_DOCUMENT){
                switch(xpp.getEventType()){
                    //On START_TAG, process element
                    case XmlPullParser.START_TAG:
                        processTag(xpp);
                        xpp.next();
                        break;
                    //All <Chore> and <Subtask> elements should be processed
                    // by the respective class.
                    case XmlPullParser.END_TAG:
                        if(!xpp.getName().equals("ChoreList")){
                            Log.d("XML.ChoreList","Unknown End Tag: "+xpp.getName());
                        }
                        xpp.next();
                        break;
                    //Add more as document grows
                    //Log unknown event and move on so we aren't stuck in a loop
                    default:
                        Log.d("XML.ChoreList","Unknown event: "+xpp.getEventType());
                        xpp.next();
                        break;
                }
            }
            //Uh oh
            if(chores.isEmpty()){
                Log.d("XML.ChoreList","Did not find and Chores");
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.d("readFile","Can't setup file reader");
        }

    }

    //Process element
    private static void processTag(XmlPullParser xpp) throws XmlPullParserException, IOException {
        switch(xpp.getName()){
            //Call Chore constructor to process new Chore
            //Chore object should process all children and
            //leave us at respective </Chore> tag
            case "Chore":
                chores.add(new Chore(xpp)); //Ends on </Chore>
                break;
            //Let's get this show on the road!
            case "ChoreList":
                break;
            //Add more as XML document grows
            //Log unknown tag. Parent loop will move us forward so we aren't stuck
            default:
                Log.d("XML.ChoreList","Unknown Tag"+xpp.getName());
                break;
        }
    }

    //Write List to backup file
    public static void writeBackupFile() throws IOException {
        //Log.d("XML.ChoreList","Writing list");
        //Initialize File and XML Writer
        FileWriter writer = new FileWriter(backupFile);
        XmlSerializer xml = Xml.newSerializer();
        xml.setOutput(writer);

        //Start Document. Start ChoreList element
        xml.startDocument("UTF-8", true);
        xml.startTag(null, "ChoreList");
        //Let each Chore take care of itself & all Subtasks
        for(Chore c:chores){
            c.writeXML(xml);
        }
        //End ChoreList. End Document.
        xml.endTag(null, "ChoreList");
        xml.endDocument();
        //Flush & Close
        xml.flush();
        writer.close();
    }

}
