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

public class ChoreExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Fragment parentFragment;

    public ChoreExpandableListAdapter(Context context, Fragment parentFragment){
        this.context = context;
        this.parentFragment = parentFragment;
    }

    @Override
    public int getGroupCount() {
        return ChoreList.size();
    }

    @Override
    public int getChildrenCount(int c) {
        return ChoreList.getChore(c).countSubtasks();
    }

    @Override
    public Object getGroup(int c) {
        return ChoreList.getChore(c);
    }

    @Override
    public Object getChild(int c, int s) {
        return ChoreList.getChore(c).getSubtask(s);
    }

    @Override
    public long getGroupId(int c) {
        return c;
    }

    @Override
    public long getChildId(int c, int s) {
        return s;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int c, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Chore chore = ChoreList.getChore(c);
        TextView choreTitleView;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        choreTitleView = (TextView) convertView.findViewById(R.id.listGroup);
        choreTitleView.setText(chore.toString());

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        if(chore.isComplete()){
            checkBox.setChecked(chore.isComplete());
            choreTitleView.setTextColor(Color.LTGRAY);
        }
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                chore.markComplete();
                choreTitleView.setTextColor(Color.LTGRAY);
            } else {
                chore.markActive();
                choreTitleView.setTextColor(Color.BLACK);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int c, int s, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        Subtask sub = (Subtask) getChild(c, s);
        TextView subtaskTitleView;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        subtaskTitleView = (TextView) convertView.findViewById(R.id.listItem);
        subtaskTitleView.setText(sub.toString());
        CheckBox checkBox = convertView.findViewById(R.id.checkBoxSub);
        if(sub.isComplete()){
            checkBox.setChecked(sub.isComplete());
            subtaskTitleView.setTextColor(Color.LTGRAY);
        }
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                sub.markComplete();
                subtaskTitleView.setTextColor(Color.LTGRAY);
            } else {
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
