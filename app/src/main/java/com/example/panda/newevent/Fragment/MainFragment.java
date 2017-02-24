package com.example.panda.newevent.Fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panda.newevent.R;
import com.example.panda.newevent.adapter.MyListAdapter;
import com.example.panda.newevent.database.Info;
import com.example.panda.newevent.model.sListView;
import com.example.panda.newevent.tools.ACache;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static android.view.View.GONE;
import static android.view.View.getDefaultSize;
import static com.example.panda.newevent.R.id.calendarView;
import static com.example.panda.newevent.R.id.dateplus;
import static com.example.panda.newevent.R.id.time;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ListView listview;
    private MaterialCalendarView calenderView;
    private FragmentTransaction fragmentTransaction;
    private ImageView backButton;
    private TextView title;
    private ImageView editButton;
    private TextView detailText;
    private TextView datePlus;
    static MainFragment fragment;
    //数组声明
    private static ArrayList<String> listTime = new ArrayList<>();//时间
    private static ArrayList<String> listTitle = new ArrayList<>();//事项标题
    private static ArrayList<String> listContent = new ArrayList<>();//事项内容
    private static ArrayList<String> listEmergency = new ArrayList<>();//紧急程度
    private int listDrawable[] = {R.drawable.greeimage, R.drawable.yellowimage, R.drawable.redimage};//事项紧急程度划分
    private static ArrayList<String> listId = new ArrayList<>();
    private static int count=0;//来进行锁控制
    private static boolean TAG;
    private static boolean TAGofNull;
    private static boolean FromTag;
    List<CalendarDay> calendar = new ArrayList<>();
    DetailFragment detailFragment = DetailFragment.newInstance(new Bundle(), fragment);

    private OnFragmentInteractionListener mListener;
    private Bundle newBundle = new Bundle();
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(Bundle bundle) {

        Log.i(">>>bundle", ("bundle == null ? " + (bundle == null)));

        fragment = new MainFragment();
        fragment.setTargetFragment(fragment, 1);
        listTime = null;
        listTime = new ArrayList<>();
        listContent = null;
        listContent = new ArrayList<>();
        listTitle = null;
        listTitle = new ArrayList<>();
        listId = null;
        listId = new ArrayList<>();
        if (bundle == null) {
            TAGofNull=true;

        } else {
            String time = bundle.getString("time");
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            String emergency=bundle.getString("emergency");
            Log.i("qqq", time + title + content + emergency+"");
            if(bundle.getBoolean("isFromMainActivity")){

                listTime.add(time);
                listContent.add(content);
                listTitle.add(title);
                listEmergency.add(emergency);
            }
            if(bundle.getBoolean("isFromSaved")){
                FromTag=true;
            }
        }

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {

            AsyncTask asyncTask = null;
            asyncTask = new search();
            asyncTask.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.main, container, false);
        View parent = (LinearLayout) getActivity().findViewById(R.id.titlebar);
        title = (TextView) parent.findViewById(R.id.title);
        editButton = (ImageView) parent.findViewById(R.id.editButton);
        backButton = (ImageView) parent.findViewById(R.id.backButton);

        title.setText("待办");
        editButton.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.INVISIBLE);
        listview = (ListView) v.findViewById(R.id.mainlistView);
        calenderView = (MaterialCalendarView) v.findViewById(calendarView);
        detailText = (TextView) v.findViewById(R.id.dateplus);
        //DetailFragment detailFragment=new DetailFragment();
        calenderView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        calenderView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return false;
            }

            @Override
            public void decorate(DayViewFacade view) {
                try {
                    view.setBackgroundDrawable(Drawable.createFromPath("/sdcard/a.jpg"));
                } catch (Exception e) {
                    Log.i(">>>error", e.toString());
                }
            }
        });
        calenderView.setSelectedDate(CalendarDay.today());
        Log.i(">>>today", CalendarDay.today() + "");
        //List<CalendarDay> calenderDay = calenderView.getSelectedDates();

        calenderView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                //listTime.add(date.getDate());
                dialog(date);

            }
        });



        /*

        for (int i = 0; i < calenderDay.size(); i++) {
            //if(calenderDay.get(i)==calenderView.getCurrentDate()){}

            //else
            onDateSelected(calenderView, calenderDay.get(i), true);
        }
*/


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                newBundle.putString("time", listTime.get(i));
                newBundle.putString("objectId", listId.get(i));
                newBundle.putString("title", listTitle.get(i));
                newBundle.putString("content", listContent.get(i));
                newBundle.putBoolean("isFromList",true);
                DetailFragment detailFragment = DetailFragment.newInstance(newBundle, fragment);
                fragmentTransaction.add(R.id.detailContent, detailFragment);

                listview.setVisibility(View.GONE);
                calenderView.setVisibility(View.GONE);

                fragmentTransaction.show(detailFragment);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTransaction.hide(getTargetFragment());
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

    protected void dialog(final CalendarDay calendarDay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认添加日程安排？");

        builder.setTitle("提示");

        builder.setPositiveButton("添加日程", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String s1, s2;
                calenderView.setSelectedDate(calendarDay);

                Bundle bundle = new Bundle();
                if (calendarDay.getMonth() + 1 >= 10) {
                    s1 = "" + calendarDay.getMonth() + 1;
                } else {
                    s1 = "0" + (calendarDay.getMonth()+1);
                }
                if (calendarDay.getDay() >= 10) {
                    s2 = calendarDay.getDay() + "";
                } else {
                    s2 = "0" + calendarDay.getDay();
                }

                Log.i(">>>>", calendarDay.getYear() + "-" + calendarDay.getMonth() + "-" + calendarDay.getDay() + "");
                bundle.putString("time", calendarDay.getYear() + "" + s1 + "" + s2 + "");

/*                for(int i=0;i<listTime.size();i++){
                    if(bundle.getString("time").equals(listTime.get(i))){
                        bundle.putString("objectId",listId.get(i));
                        bundle.putString("title",listTitle.get(i));
                        bundle.putString("content",listContent.get(i));
                    }
                    else;
                }*/
                DetailFragment detailFragment = DetailFragment.newInstance(bundle, fragment);
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.detailContent, detailFragment);
                detailFragment.setTargetFragment(fragment, 100);//100表示
                fragmentTransaction.show(detailFragment);
                listview.setVisibility(View.GONE);
                calenderView.setVisibility(View.GONE);
                detailText.setVisibility(GONE);
                fragmentTransaction.commit();


            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //calenderView.setSelectedDate(CalendarDay.today());
            }
        });

        builder.create().show();
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


    class search extends AsyncTask<Object, Void, Boolean> {

        search() {

        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            int record=0;
            ACache mCache = ACache.get(getActivity());
            ACache userCache = ACache.get(getActivity(),"user");
            if (mCache!=null&&FromTag==false) {
                try {
                    record=1;
                    JSONArray titlejsonArray = mCache.getAsJSONArray("title");
                    JSONArray idjsonArray = mCache.getAsJSONArray("id");
                    JSONArray timejsonArray = mCache.getAsJSONArray("time");
                    JSONArray contentjsonArray = mCache.getAsJSONArray("content");
                    JSONArray emergencyjsonArray = mCache.getAsJSONArray("emergency");
                    for(int i=0;i<titlejsonArray.length();i++){
                        listTitle.add(titlejsonArray.get(i).toString());
                        listId.add(idjsonArray.get(i).toString());
                        listTime.add(timejsonArray.get(i).toString());
                        listContent.add(contentjsonArray.get(i).toString());
                        listEmergency.add(emergencyjsonArray.get(i).toString());
                    }
                    Log.i("listTitle",listTitle.toString());
                } catch (Exception ex) {
                    Log.e("exception", ex.getMessage());

                }
                finally {
                    TAG=true;
                }
            }else {

                BmobQuery<Info> query = new BmobQuery<Info>();
//查询playerName叫“比目”的数据
                query.addWhereEqualTo("User", userCache.getAsString("username"));
//返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(50);
//执行查询方法
                query.order("Time");
                try {

                    query.findObjects(new FindListener<Info>() {
                        @Override
                        public void done(List<Info> object, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "查询成功：共" + object.size() + "条数据。", Toast.LENGTH_LONG).show();
                                count = object.size();

                                for (Info info : object) {
                                    //获得playerName的信息
                                    listTitle.add(info.getTitle());
                                    //获得数据的objectId信息
                                    listId.add(info.getObjectId());
                                    //SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");  //格式化日期

                                    listTime.add(info.getTime());

                                    listContent.add(info.getContent());

                                    listEmergency.add(info.getEmergency());
                                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                                    ACache mCache = ACache.get(getActivity(),"Acache");
                                    mCache.clear();
                                    try{
                                    JSONArray titlejsonArray = new JSONArray(listTitle);
                                    JSONArray idjsonArray = new JSONArray(listId);
                                    JSONArray timejsonArray = new JSONArray(listTime);
                                    JSONArray contentjsonArray = new JSONArray(listContent);
                                    JSONArray emergencyjsonArray = new JSONArray(listEmergency);
                                    //Log.i("listTitle",titlejsonArray.toString());
                                    mCache.put("title", titlejsonArray);
                                    mCache.put("id", idjsonArray);//保存10秒，如果超过10秒去获取这个key，将为null
                                    //mCache.put("test_key3", "test value", 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null
                                    mCache.put("time", timejsonArray);
                                    mCache.put("content", contentjsonArray);
                                    mCache.put("emergency", emergencyjsonArray);
                                    }catch (Exception es){
                                        System.out.print(es.getMessage());
                                    }
                                }

                            } else {
                                ACache mCache = ACache.get(getActivity());
                                if (mCache != null) {
                                    try {

                                        JSONArray titlejsonArray = mCache.getAsJSONArray("title");
                                        JSONArray idjsonArray = mCache.getAsJSONArray("id");
                                        JSONArray timejsonArray = mCache.getAsJSONArray("time");
                                        JSONArray contentjsonArray = mCache.getAsJSONArray("content");
                                        JSONArray emergencyjsonArray = mCache.getAsJSONArray("emergency");
                                        for (int i = 0; i < titlejsonArray.length(); i++) {
                                            listTitle.add(titlejsonArray.get(i).toString());
                                            listId.add(idjsonArray.get(i).toString());
                                            listTime.add(timejsonArray.get(i).toString());
                                            listContent.add(contentjsonArray.get(i).toString());
                                        }
                                    } catch (Exception ex) {
                                        Log.e("exception", ex.getMessage());

                                    }
                                }

                                Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());


                            }
                            TAG = true;
                        }
                    });
                } catch (Exception e) {
                    Log.i(">>>error", e.toString());
                }
            }
            if(record==0){
            while(listContent.size()!=count||TAG==false){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }}
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.i(">>>", "Start");
            Log.i(">>>",listTime+"");
            try {
                for (int i = 0; i < listTime.size(); i++) {
                    Log.i(">>>", "" + i);
                    Log.i(">>>>>", "" + listTime.get(i));
                    int a = Integer.valueOf(listTime.get(i).substring(0, 4));
                    int b = Integer.valueOf(listTime.get(i).substring(4, 6));
                    int c = Integer.valueOf(listTime.get(i).substring(6, 8));
                    CalendarDay currentDay = CalendarDay.from(a, b-1 , c);
                    Log.i(">>>>>", "" + currentDay);
                    calenderView.setDateSelected(currentDay, true);
                    calendar.add(currentDay);
                    MyListAdapter myListAdapter = new MyListAdapter(listId, listTime, listTitle, listContent,listEmergency, getActivity());

                    listview.setAdapter(myListAdapter);
                    setListViewHeight(listview);
                }
            } catch (Exception e) {
                Log.i(">>>error", e.toString());
            }


            //super.onPostExecute(aBoolean);
        }
        private void adapter() {

        }
    }



    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

