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

public class EditChoreFragment extends Fragment {

    private FragmentEditChoreBinding binding;
    private Chore chore;
    private int numberOfSubs = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditChoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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

    private void addButtonListeners() {
        //Listener for new Subtask Input field
        binding.buttonNewSubtask.setOnClickListener(view1 -> addSubtaskInput());
        //Listener for Replace
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

    private void saveChore(){
        Chore newChore = new Chore(binding.choreTitleInput.getText().toString());
        for(int i=1; i<=numberOfSubs; i++){
            EditText et = (EditText) binding.getRoot().findViewById(i);
            String subText = et.getText().toString();
            if(subText.isEmpty() || subText.trim() == "" || subText.matches("\\s*")){
                continue;
            }
            newChore.addSubtask(new Subtask(subText));
        }
        ChoreList.updateChore(chore, newChore);
    }

    private void addSubtaskInput(){
        addSubtaskInput("");
    }

    private void addSubtaskInput(String title){
        LinearLayout ll = (LinearLayout) binding.getRoot().findViewById(R.id.editChoreLayout);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 0);

        EditText et = new EditText(getContext());
        et.setLayoutParams(editTextParams);
        et.setHint(R.string.subtask_title);
        et.setText(title);
        et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setId(++numberOfSubs);

        ll.addView(et);
    }

}