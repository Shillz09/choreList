package seng564.chorelist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import seng564.chorelist.databinding.FragmentChoreListBinding;

public class ChoreListFragment extends Fragment {

    private ExpandableListView listView;
    //private ChoreList choreList = new ChoreList();
    private ArrayList<String> listTitle;
    private ChoreExpandableListAdapter listAdapter;

    private FragmentChoreListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChoreListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView();
        binding.buttonAddChore.setOnClickListener(view1 -> NavHostFragment.findNavController(ChoreListFragment.this)
                .navigate(R.id.action_ChoreList_to_AddChore));
    }

    @Override
    public void onResume(){
        super.onResume();
        loadListView();
    }

    private void loadListView() {
        listView = binding.expandableListView.findViewById(R.id.expandableListView);
        listAdapter = new ChoreExpandableListAdapter(getActivity(), this);
        listView.setAdapter(listAdapter);
        addListeners();
    }

    private void addListeners() {
 /*       listView.setOnGroupExpandListener(i -> Toast.makeText(getActivity(),
                listAdapter.getGroup(i).toString() + " Expanded",
                Toast.LENGTH_SHORT).show());

        listView.setOnGroupCollapseListener(i -> Toast.makeText(getActivity(),
                listAdapter.getGroup(i).toString() + " List Collapsed.",
                Toast.LENGTH_SHORT).show());*/

/*        listView.setOnGroupClickListener((expandableListView, view, i, l) -> {
            expandableListView.expandGroup(i);
            Toast.makeText(getActivity(),
                    listAdapter.getGroup(i).toString() + "Clicked.",
                    Toast.LENGTH_SHORT).show();
            return false;
        });*/

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            if(ExpandableListView.getPackedPositionType(l) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
                int groupPosition = ExpandableListView.getPackedPositionGroup(l);
                Bundle choreBundle = new Bundle();
                choreBundle.putInt("choreInt", groupPosition);
                NavHostFragment.findNavController(ChoreListFragment.this)
                        .navigate(R.id.action_ChoreList_to_EditChore, choreBundle);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}