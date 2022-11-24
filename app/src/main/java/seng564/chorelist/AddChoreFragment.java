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


public class AddChoreFragment extends Fragment {

    private FragmentAddChoreBinding binding;
    private int numberOfSubs = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddChoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSubmit.setOnClickListener(view1 -> {
            addNewChore();
            NavHostFragment.findNavController(AddChoreFragment.this)
                    .navigate(R.id.action_AddChore_to_ChoreList);
        });

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

        addSubtaskInput();
        binding.buttonNewSubtask.setOnClickListener(view1 -> addSubtaskInput());
    }

    private void addNewChore(){
        Chore c = new Chore(binding.choreTitleInput.getText().toString());
        for(int i=1; i<=numberOfSubs; i++){
            EditText et = (EditText) binding.getRoot().findViewById(i);
            String subText = et.getText().toString();
            if(subText.isEmpty() || subText.trim() == "" || subText.matches("\\s*")){
                continue;
            }
            c.addSubtask(new Subtask(subText));
        }
        ChoreList.addChore(c);
    }

    private void addSubtaskInput(){
        LinearLayout ll = (LinearLayout) binding.getRoot().findViewById(R.id.addChoreLayout);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 10, 10, 0);

        EditText et = new EditText(getContext());
        et.setLayoutParams(editTextParams);
        et.setHint(R.string.subtask_title);
        et.setImeOptions(EditorInfo.IME_ACTION_SEND);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setId(++numberOfSubs);

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

        ll.addView(et);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}