package com.example.alperkaya.myruns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CODE_START = 766;

    private Spinner inputSpinner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        Button mStartBtn = (Button) v.findViewById(R.id.btnStart);
        inputSpinner = (Spinner) v.findViewById(R.id.inputSpinner);

        mStartBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                String inputTypes[] = getResources().getStringArray(R.array.input_type_items);
                String selectedSpinnerItem = inputSpinner.getSelectedItem().toString();
                if (inputTypes[0].equals(selectedSpinnerItem)) {
                    Log.d("alper", "0");
                    Intent mManualEntryIntent = new Intent(getContext(), ManualEntryActivity.class);
                    startActivity(mManualEntryIntent);
                } else if (inputTypes[1].equals(selectedSpinnerItem)) {
                    Log.d("alper", "1");
                    Intent mGPSIntent = new Intent(getContext(), GPSActivity.class);
                    startActivity(mGPSIntent);
                } else if (inputTypes[2].equals(selectedSpinnerItem)) {
                    Log.d("alper", "2");
                    Intent mAutoIntent = new Intent(getContext(), AutomaticActivity.class);
                    startActivity(mAutoIntent);
                }


                break;
        }
    }

}
