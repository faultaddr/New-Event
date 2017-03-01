package com.example.panda.newevent.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panda.newevent.R;
import com.example.panda.newevent.database.Info;
import com.example.panda.newevent.model.ListContent;
import com.example.panda.newevent.tools.ACache;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private ImageView backButton;
    private TextView title;
    private ImageView editButton;
    private EditText editText;
    private EditText editTitle;
    private CheckBox checkBox1,checkBox2,checkBox3;
    //常量声明

    //变量声明
    private static String detailContent;
    private static String detailTime;
    private static String objectId;
    private static MainFragment mainFragment1;
    //Bundle bundle=new Bundle();
    String s;
    static String sTitle;
    static String tempText;
    static String tempTitle;
    boolean saved;
    int TAG;
    //数组声明

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Bundle bundle, MainFragment mainFragment) {
        detailTime = bundle.getString("time");
        if(bundle.getBoolean("isFromList")==true){
        detailContent = bundle.getString("content");
        objectId = bundle.getString("objectId");
        tempTitle=bundle.getString("title");
        tempText=bundle.getString("content");
        }
        //mainFragment1=mainFragment;
        Log.i("time", detailTime + "");
        Log.i("title",tempTitle+"");
        DetailFragment fragment = new DetailFragment();
        fragment.setTargetFragment(fragment, 1);
        return fragment;
    }
    CheckBox.OnCheckedChangeListener checkboxlister = new CheckBox.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if(checkBox1.isChecked()){
                TAG=1;
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
            }
            if(checkBox2.isChecked()){
                TAG=2;
                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
            }
            if(checkBox3.isChecked()){
                TAG=3;
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
            }
            switch (TAG){
                case 1:

            }
        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View main=getActivity().findViewById(R.id.activity_main);
        main.setSystemUiVisibility(View.INVISIBLE);
        View appbar=getActivity().findViewById(R.id.toolbar);
        appbar.setVisibility(View.GONE);
        View mainFragment1=getActivity().findViewById(R.id.content_navigation);
        main.postInvalidate();
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        View parent = (LinearLayout)v.findViewById(R.id.titlebar);
        //parent.setVisibility(View.VISIBLE);
        title = (TextView) parent.findViewById(R.id.title);
        editButton = (ImageView) parent.findViewById(R.id.editButton);
        backButton = (ImageView) parent.findViewById(R.id.backButton);
        checkBox1=(CheckBox)v.findViewById(R.id.rankFirst);
        checkBox2=(CheckBox)v.findViewById(R.id.rankSecond);
        checkBox3=(CheckBox)v.findViewById(R.id.rankThird);
        checkBox1.setOnCheckedChangeListener(checkboxlister);
        checkBox2.setOnCheckedChangeListener(checkboxlister);
        checkBox3.setOnCheckedChangeListener(checkboxlister);

        title.setText("详细事项");
        editButton.setVisibility(View.VISIBLE);

        backButton.setVisibility(View.VISIBLE);
        editText = (EditText) v.findViewById(R.id.detailTextContent);
        editTitle = (EditText) v.findViewById(R.id.titletext);
        editText.setSaveEnabled(true);
        editText.setText(tempText);
        editTitle.setText(tempTitle);

/*        if (detailContent != null) {
            if (detailContent.equals("focus")) {
                editText.setFocusable(true);
                editText.setEditableFactory(Editable.Factory.getInstance());

            }

            editText.setText(detailContent);
        }*/
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusable(false);
                editTitle.setFocusable(false);
                editButton.setFocusable(true);
                sTitle = editTitle.getText().toString();
                s = editText.getText().toString();
//                editText.setText(s);
//                editTitle.setText(sTitle);
                Info p2 = new Info();
                p2.setTitle(sTitle);
                p2.setContent(s);
                p2.setTime(detailTime);
                p2.setEmergency(TAG+"");
                ACache userCache = ACache.get(getActivity().getApplication(),"User");
                p2.setUser(userCache.getAsString("username"));
                //TODO-LIST 1.读取已经存在的备忘详细内容
                //TODO-LIST 2.在有详细内容时再进行修改而不是直接存为一个新的备忘事项
                Log.i(">>>",""+tempText+">>>"+tempTitle);

                if (!(s.equals(tempText) || !sTitle.equals(tempTitle))) {

                    p2.setValue("Title", sTitle);
                    p2.setValue("Content", s);
                    p2.setValue("emergency",TAG+"");
                    p2.update(objectId, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "更新成功");
                                Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_LONG).show();
                                saved = true;
                            } else {
                                Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }

                    else{
                    p2.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                Log.i("done", "done");
                                Toast.makeText(getActivity().getApplication(),"保存成功",Toast.LENGTH_LONG).show();
                                saved = true;
                            } else {
                                Log.i("fail", "fail" + e);
                                Toast.makeText(getActivity().getApplication(),"保存失败",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        //appbar.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                if (saved == true) {
                    bundle.putString("time", detailTime);
                    bundle.putString("title", sTitle);
                    bundle.putString("content", s);
                    bundle.putString("emergency",TAG+"");
                    bundle.putBoolean("isFromSaved",true);
                }
                View main=getActivity().findViewById(R.id.activity_main);
                main.setSystemUiVisibility(View.VISIBLE);
                View appbar=getActivity().findViewById(R.id.toolbar);
                appbar.setVisibility(View.VISIBLE);
                MainFragment mainFragment = MainFragment.newInstance(bundle);
                fragmentTransaction.replace(R.id.detailContent, mainFragment);
                fragmentTransaction.show(mainFragment);
                fragmentTransaction.hide(getTargetFragment());

                fragmentTransaction.commit();
            }
        });

        return v;
    }

    private void setSupportActionBar(Toolbar toolbar) {
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
        Log.i(">>>", "before");
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i(">>>", "on");
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.i(">>>", "after");
        editText.setFocusable(false);
        editButton.setFocusable(true);
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
