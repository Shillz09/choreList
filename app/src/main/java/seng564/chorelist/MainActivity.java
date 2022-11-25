/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import seng564.chorelist.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;

//Main -- START HERE
//Related layout:  activity_main.xml
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize ChoreList
        initializeChores();
        //Inflate binding & set as view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Set toolbar
        setSupportActionBar(binding.toolbar);
        //Initiate NavController (Fragments = screens)
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    //Read from file if it exists already.
    //Otherwise start with sample chores
    private void initializeChores(){
        File choreFile = new File(getApplicationContext().getFilesDir(), "backup.xml");
        if(choreFile.exists()){
            //Load from backup
            Log.d("main","reading from file");
            new ChoreList(choreFile);
            //ChoreList.setBackupFile(choreFile);
        } else {
            //Load samples
            Log.d("main", "Backup File not found. Loading Samples");
            new ChoreList(SampleChores.addChores());
            ChoreList.setBackupFile(choreFile);
        }

    }

    //Add Menu to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Menu items - "Save" is the only Menu item for now.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Write ChoreList to XML file
        if (id == R.id.save) {
            boolean bool = false;
            try {
                ChoreList.writeBackupFile();
                bool = true;
            } catch (IOException e) {
                Log.d("Main","Error writing XML file");
                e.printStackTrace();
            }
            return bool;
        }

        return super.onOptionsItemSelected(item);
    }

    //Navigate for "back" button
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}