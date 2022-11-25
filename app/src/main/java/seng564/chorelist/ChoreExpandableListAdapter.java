/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

//Custom ExpandableListAdapter
//For the most part, I followed this tutorial:
//https://www.digitalocean.com/community/tutorials/android-expandablelistview-example-tutorial
//
//For reference, "Group" is a Chore object
// and "Child" is a Subtask
// Related Layouts: list_group.xml & list_item.xml
//
//Most logic here is in the getGroupView() and getChildView() methods,
//which are used to display the Chores & Subtasks
public class ChoreExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;

    public ChoreExpandableListAdapter(Context context, Fragment parentFragment){
        this.context = context;
    }

    //Return total number of Chores
    @Override
    public int getGroupCount() {
        return ChoreList.size();
    }

    //Return number of Subtasks for a given Chore
    @Override
    public int getChildrenCount(int c) {
        return ChoreList.getChore(c).countSubtasks();
    }

    //Return Chore
    @Override
    public Object getGroup(int c) {
        return ChoreList.getChore(c);
    }

    //Return Subtask for given Chore
    @Override
    public Object getChild(int c, int s) {
        return ChoreList.getChore(c).getSubtask(s);
    }

    //Required, but not used
    @Override
    public long getGroupId(int c) {
        return c;
    }
    //Required, but not used
    @Override
    public long getChildId(int c, int s) {
        return s;
    }
    //Required, but not used
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //Return View used for displaying a Chore in Expandable List
    @Override
    public View getGroupView(int c, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //Get this Chore
        Chore chore = ChoreList.getChore(c);
        //Initialize View
        TextView choreTitleView;
        //If null, inflate parent from list_group.xml
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        //Get this chore text view and set text as title
        choreTitleView = (TextView) convertView.findViewById(R.id.listGroup);
        choreTitleView.setText(chore.toString());

        //Setup checkbox settings & listener
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        //If chore is complete, then check box and set font color to gray
        if(chore.isComplete()){
            checkBox.setChecked(chore.isComplete());
            choreTitleView.setTextColor(Color.LTGRAY);
        }
        //Add listener for checking/unchecking box
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){ //On Check, mark complete and change font color to gray
                chore.markComplete();
                choreTitleView.setTextColor(Color.LTGRAY);
            } else { //On uncheck, mark active and set font color back
                chore.markActive();
                choreTitleView.setTextColor(Color.BLACK);
            }
        });
        //Return view
        return convertView;
    }

    //Return View used for displaying a Subtask in Expandable List
    @Override
    public View getChildView(int c, int s, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        //Get subtask
        Subtask sub = (Subtask) getChild(c, s);
        //Initialize View
        TextView subtaskTitleView;
        //If null, inflate parent from list_item.xml
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        //Get this child text view and set title as text
        subtaskTitleView = (TextView) convertView.findViewById(R.id.listItem);
        subtaskTitleView.setText(sub.toString());
        //Setup checkbox & add listener
        CheckBox checkBox = convertView.findViewById(R.id.checkBoxSub);
        //If Subtask is completed, check box & change font to gray
        if(sub.isComplete()){
            checkBox.setChecked(sub.isComplete());
            subtaskTitleView.setTextColor(Color.LTGRAY);
        }
        //Add listener for checking/unchecking box
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){ //On Check, mark complete and change font color to gray
                sub.markComplete();
                subtaskTitleView.setTextColor(Color.LTGRAY);
            } else { //On uncheck, mark active and set font color back
                sub.markActive();
                subtaskTitleView.setTextColor(Color.BLACK);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int t, int s) {
        return false;
    }
}
