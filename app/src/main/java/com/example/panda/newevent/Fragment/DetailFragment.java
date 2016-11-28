package com.example.panda.newevent.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.panda.newevent.R;
import com.example.panda.newevent.model.ListContent;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements TextWatcher {

    //View 声明
    private ImageButton backButton;
    private TextView title;
    private TextView editButton;
    private EditText editText;
    private EditText editTitle;
    //常量声明

    //变量声明
    private static String detailContent;
    Bundle bundle=new Bundle();
    //数组声明

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Bundle bundle) {
        detailContent=bundle.getString("content");

        DetailFragment fragment = new DetailFragment();
        fragment.setTargetFragment(fragment,1);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_detail, container, false);
        View parent=(LinearLayout)getActivity().findViewById(R.id.titlebar);
        title=(TextView)parent.findViewById(R.id.title);
        editButton=(TextView)parent.findViewById(R.id.editButton);
        backButton=(ImageButton)parent.findViewById(R.id.backButton) ;

        title.setText("详细事项");
        editButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        editText=(EditText) v.findViewById(R.id.detailTextContent);
        editTitle=(EditText)v.findViewById(R.id.titletext) ;
        editText.setSaveEnabled(true);
        if(detailContent!=null) {
            if (detailContent.equals("focus")) {
                editText.setFocusable(true);
                editText.setEditableFactory(Editable.Factory.getInstance());

            }

            editText.setText(detailContent);
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTitle.setFocusable(true);
                editTitle.setEditableFactory(Editable.Factory.getInstance());

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                MainFragment mainFragment=MainFragment.newInstance(bundle);
                fragmentTransaction.add(R.id.detailContent,mainFragment);
                fragmentTransaction.show(mainFragment);
                fragmentTransaction.hide(getTargetFragment());
                fragmentTransaction.commit();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String sTitle=editTitle.getText().toString();
        String s= editText.getText().toString();
        editText.setText(s);
        editTitle.setText(sTitle);
        bundle.putString("title",sTitle);
        bundle.putString("content",s);
        bundle.putInt("drawable",1);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
