/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import seng564.chorelist.databinding.FragmentEditChoreBinding;

//Fragment for Editing Chore
//Related Layout:  fragment_edit_chore.xml
public class EditChoreFragment extends Fragment {

    private FragmentEditChoreBinding binding;
    private Chore chore;
    private int numberOfSubs = 0;

    //Inflate binding
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditChoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Get the chore that we are editting from the list
    //Set the Input Fields to existing Chore/Subtask text
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Chore item to Edit
        chore = ChoreList.getChore(getArguments().getInt("choreInt"));
        binding.choreTitleInput.setText(chore.getTitle());
        for(Subtask s:chore.getSubtaskArray()){
            addSubtaskInput(s.getTitle());
        }

        //Add listeners to buttons
        addButtonListeners();
    }

    //Add listeners to buttons
    private void addButtonListeners() {
        //Listener for new Subtask Input field
        binding.buttonNewSubtask.setOnClickListener(view1 -> addSubtaskInput());
        //Listener for Saving Chore
        binding.buttonSave.setOnClickListener(view1 -> {
            saveChore();
            NavHostFragment.findNavController(EditChoreFragment.this)
                    .navigate(R.id.action_editChore_to_ChoreList);
        });
        //Listener for Delete
        binding.buttonDelete.setOnClickListener(view -> {
            ChoreList.removeChore(chore);
            NavHostFragment.findNavController(EditChoreFragment.this)
                    .navigate(R.id.action_editChore_to_ChoreList);
        });
    }

    //Technically, we replace the existing chore with a new chore
    //Simiar to addChore() from AddChoreFragment
    //TODO: Move logic to static helper class
    private void saveChore(){
        Chore newChore = new Chore(binding.choreTitleInput.getText().toString());
        for(int i=1; i<=numberOfSubs; i++){
            EditText et = (EditText) binding.getRoot().findViewById(i);
            String subText = et.getText().toString();
            //Ignore Subtask Inputs that are blank or all whitespace
            if(subText.isEmpty() || subText.matches("\\s*")){
                continue;
            }
            newChore.addSubtask(new Subtask(subText));
        }
        ChoreList.updateChore(chore, newChore);
    }

    //Add an empty Subtask input
    //I needed a method that accepted a String for pre-existing subtasks,
    // but also a method that accepted no input for new Subtasks
    // Instead of two methods that do the same thing, I just call
    // the actual method with a blank String input
    private void addSubtaskInput(){
        addSubtaskInput("");
    }

    //Add another input field for another subtask
    //Duplicate method found in EditChoreFragment.
    //TODO: Move to static helper class for dynamic layout
    private void addSubtaskInput(String title){
        //Get Layout & Parameters
        LinearLayout ll = (LinearLayout) binding.getRoot().findViewById(R.id.editChoreLayout);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 0);
        //Create new EditText input field
        EditText et = new EditText(getContext());
        et.setLayoutParams(editTextParams);
        et.setHint(R.string.subtask_title);
        et.setText(title);
        et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setId(++numberOfSubs);
        //Add to layout
        ll.addView(et);
    }

}