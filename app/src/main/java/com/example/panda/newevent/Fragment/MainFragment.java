package com.example.panda.newevent.Fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.panda.newevent.R;
import com.example.panda.newevent.adapter.MyListAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnDateSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView listview;
    private MaterialCalendarView calenderView;
    private FragmentTransaction fragmentTransaction;
    //数组声明

    private ArrayList<String> listTitle = new ArrayList<>();//事项标题
    private ArrayList<String> listContent = new ArrayList<>();//事项内容
    private int listDrawable[] = {R.drawable.greeimage, R.drawable.yellowimage, R.drawable.redimage};//事项紧急程度划分
    DetailFragment detailFragment = DetailFragment.newInstance(new Bundle());

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(Bundle bundle) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        listTitle.add("周一开会");
        listTitle.add("周一开会");
        listTitle.add("周一开会");

        listContent.add("214");
        listContent.add("214");
        listContent.add("214");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.main,container,false);
        listview = (ListView)v.findViewById(R.id.mainlistView);
        calenderView = (MaterialCalendarView)v.findViewById(R.id.calendarView);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //DetailFragment detailFragment=new DetailFragment();
        calenderView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        final DetailFragment detailFragment = DetailFragment.newInstance(new Bundle());

        //List<CalendarDay> calenderDay = calenderView.getSelectedDates();
        CalendarDay calendarDay = calenderView.getCurrentDate();

        onDateSelected(calenderView,calendarDay,true);
        /*

        for (int i = 0; i < calenderDay.size(); i++) {
            //if(calenderDay.get(i)==calenderView.getCurrentDate()){}

            //else
            onDateSelected(calenderView, calenderDay.get(i), true);
        }
*/

        MyListAdapter myListAdapter = new MyListAdapter(listTitle, listContent, listDrawable, getActivity());


        listview.setAdapter(myListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                fragmentTransaction.add(R.id.detailContent, detailFragment);
                fragmentTransaction.commitAllowingStateLoss();
                listview.setVisibility(View.GONE);
                calenderView.setVisibility(View.GONE);
                setupActionBar();
                fragmentTransaction.show(detailFragment);

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

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认添加日程安排？");

        builder.setTitle("提示");

        builder.setPositiveButton("添加日程", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("content", "focus");
                DetailFragment detailFragment = DetailFragment.newInstance(bundle);
                fragmentTransaction.add(R.id.detailContent, detailFragment);
                listview.setVisibility(View.GONE);
                calenderView.setVisibility(View.GONE);
                setupActionBar();
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTransaction.show(detailFragment);

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                calenderView.setSelectedDate(calenderView.getCurrentDate());
            }
        });

        builder.create().show();
    }
    private void setupActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        dialog();

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
