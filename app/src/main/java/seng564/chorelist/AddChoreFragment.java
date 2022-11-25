/*
Anthony Shillingburg
SENG564 - Fall 2022
Individual Application
 */

package seng564.chorelist;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import seng564.chorelist.databinding.FragmentAddChoreBinding;

//Fragment (screen) for adding a new chore
public class AddChoreFragment extends Fragment {

    private FragmentAddChoreBinding binding;
    private int numberOfSubs = 0;

    //Inflate binding
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddChoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Setup buttons
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //On "Submit" - Add chore to list & navigate back to main
        binding.buttonSubmit.setOnClickListener(view1 -> {
            addNewChore();
            NavHostFragment.findNavController(AddChoreFragment.this)
                    .navigate(R.id.action_AddChore_to_ChoreList);
        });

        //Listener for hitting "send" on the keyboard while in the Chore Title Input item
        //Add chore to list & navigate back to main
        binding.choreTitleInput.setOnEditorActionListener((textView, actionID, keyEvent) -> {
            boolean isBool = false;
            if(actionID == EditorInfo.IME_ACTION_SEND){
                addNewChore();
                NavHostFragment.findNavController(AddChoreFragment.this)
                        .navigate(R.id.action_AddChore_to_ChoreList);
                isBool = true;
            }
            return isBool;
        });

        //Add initial Subtask input field
        addSubtaskInput();
        //Listener to add new subtask inputs on button click
        binding.buttonNewSubtask.setOnClickListener(view1 -> addSubtaskInput());
    }

    //Create chore, add all the subtasks, then add to ChoreList.
    //Similar to saveChore() method from EditChoreFragment.
    //TODO: Move to static helper class for dynamic layout
    private void addNewChore(){
        Chore c = new Chore(binding.choreTitleInput.getText().toString());
        for(int i=1; i<=numberOfSubs; i++){
            EditText et = (EditText) binding.getRoot().findViewById(i);
            String subText = et.getText().toString();
            //Ignore empty or blank subtask input fields
            if(subText.isEmpty() || subText.matches("\\s*")){
                continue;
            }
            c.addSubtask(new Subtask(subText));
        }
        ChoreList.addChore(c);
    }

    //Add another input field for another subtask
    //Duplicate method found in EditChoreFragment.
    //TODO: Move to static helper class for dynamic layout
    private void addSubtaskInput(){
        //Get Layout & editText parameters
        LinearLayout ll = (LinearLayout) binding.getRoot().findViewById(R.id.addChoreLayout);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 0);

        //Create EditText item
        EditText et = new EditText(getContext());
        et.setLayoutParams(editTextParams);
        et.setHint(R.string.subtask_title);
        et.setImeOptions(EditorInfo.IME_ACTION_SEND);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setId(++numberOfSubs);

        //Add the listener for "send" on the keyboard
        //Add chore & navigate back to main screen
        et.setOnEditorActionListener((textView, actionID, keyEvent) -> {
            boolean isSend = false;
            if(actionID == EditorInfo.IME_ACTION_SEND){
                addNewChore();
                NavHostFragment.findNavController(AddChoreFragment.this)
                        .navigate(R.id.action_AddChore_to_ChoreList);
                isSend = true;
            }
            return isSend;
        });

        //Add input field to layout
        ll.addView(et);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}