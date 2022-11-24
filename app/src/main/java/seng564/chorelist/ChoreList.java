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

public class ChoreList {

    private static ArrayList<Chore> chores;
    //private static boolean displayComplete = false;
    private static File backupFile;

    public ChoreList() {this.chores = new ArrayList<Chore>(); }

    public ChoreList(ArrayList<Chore> c){
        this.chores = c;
    }

    public ChoreList(File f){
        //TODO: Read XML file
        //Initialize variables
        this.backupFile = f;
        this.chores = new ArrayList<Chore>();
        readFile();
    }

    public static ArrayList<Chore> getAllChores() {
        return chores;
    }

    public static Chore getChore(String title){
        for (Chore c:chores){
            if(c.getTitle()==title){
                return c;
            }
        }
        return null;
    }

    public static Chore getChore(int t){
        return chores.get(t);
    }

    public static int size(){
        return chores.size();
    }

    public static ArrayList<Chore> getActiveChores() {
        ArrayList<Chore> activeTasks = new ArrayList<Chore>();
        for(Chore t: chores){
            if(t.isComplete()) {
                activeTasks.add(t);
            }
        }
        return activeTasks;
    }

    public static void addChore(Chore t){
        chores.add(t);
    }

    public static void updateChore(Chore oldChore, Chore newChore){
        int idx = chores.indexOf(oldChore);
        chores.remove(oldChore);
        chores.add(idx, newChore);
    }

    //TODO: Toggle display of inactive chores
/*    public static void toggleComplete(){
        if(displayComplete){
            displayComplete = false;
        } else {
            displayComplete = true;
        }
    }*/

    public static void removeChore(Chore t){
        chores.remove(t);
    }

    public static void setBackupFile(File f){
        backupFile = f;
    }

    public static void readFile(){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new FileReader(backupFile));
            int event = xpp.getEventType();
            while(event != XmlPullParser.END_DOCUMENT){
                switch(event){
                    case XmlPullParser.START_TAG:
                        processTag(xpp);
                        break;

                    case XmlPullParser.END_TAG:
                        if(!xpp.getName().equals("ChoreList")){
                            Log.d("XML.ChoreList","Unknown End Tag: "+xpp.getName());
                        }
                        xpp.next();
                        break;

                    //Add more as document grows
                    default:
                        Log.d("XML.ChoreList","Unknown event: "+event);
                        xpp.next();
                        break;
                }
            }
            if(chores.isEmpty()){
                Log.d("XML.ChoreList","Did not find and Chores");
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.d("readFile","Can't setup file reader");
        }

    }

    private static void processTag(XmlPullParser xpp) throws XmlPullParserException, IOException {
        switch(xpp.getName()){
            case "ChoreList":
                xpp.next();
                break;
            case "Chore":
                chores.add(new Chore(xpp)); //Ends on </Chore>
                xpp.next();
                break;
            //Add more as XML document grows
            default:
                Log.d("XML.ChoreList","Unknown Tag"+xpp.getName());
                xpp.next();
                break;
        }
    }

    public static void writeBackupFile() throws IOException {
        Log.d("XML.ChoreList","Writing list");

        FileWriter writer = new FileWriter(backupFile);
        XmlSerializer xml = Xml.newSerializer();
        xml.setOutput(writer);

        xml.startDocument("UTF-8", true);
        xml.startTag(null, "ChoreList");
        for(Chore c:chores){
            c.writeXML(xml);
        }
        xml.endTag(null, "ChoreList");
        xml.endDocument();
        xml.flush();
    }

}
