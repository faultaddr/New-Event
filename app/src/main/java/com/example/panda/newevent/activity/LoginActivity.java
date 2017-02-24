package com.example.panda.newevent.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.panda.newevent.Fragment.MainFragment;
import com.example.panda.newevent.Fragment.RegisterFragment;
import com.example.panda.newevent.MainActivity;
import com.example.panda.newevent.R;
import com.example.panda.newevent.tools.ACache;
import com.example.panda.newevent.tools.JellyInterpolator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends AppCompatActivity implements OnClickListener {
    //TODO-LIST: 增加注册页面
    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    private EditText userId;

    private EditText passWord;

    private TextView signUp;

    private RelativeLayout mainToAll;

    private final Lock lock = new ReentrantLock();

    private Condition notComplete = lock.newCondition();
    private Condition notEmpty = lock.newCondition() ;

    private String nameString,psString;

    private static int TAG=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "1780a91ce4f5760f8553c5eeab00ebd4");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
        ACache mCache=ACache.get(getApplication(),"User");
        if(mCache.getAsString("username")!=null){
            Message msg=new Message();
            msg.what=1;
            handler.sendMessage(msg);
        }
    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        signUp=(TextView)findViewById(R.id.signup);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        userId=(EditText) findViewById(R.id.userId);
        passWord=(EditText)findViewById(R.id.passWord);
        mainToAll=(RelativeLayout)findViewById(R.id.mainToAll);
        mBtnLogin.setOnClickListener(this);

        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainToAll.setVisibility(View.INVISIBLE);
                RegisterFragment registerFragment=RegisterFragment.newInstance();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.registerFrame,registerFragment);
                fragmentTransaction.commit();
                fragmentTransaction.show(registerFragment);
            }
        });
    }

    @Override
    public void onClick(View v) {

        mWidth = mBtnLogin.getMeasuredWidth();
        mHeight = mBtnLogin.getMeasuredHeight();
        nameString=userId.getText().toString();
        psString=passWord.getText().toString();
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);

        inputAnimator(mInputLayout, mWidth, mHeight);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==101){
            nameString=data.getStringExtra("userId");
            psString=data.getStringExtra("passWord");
            userId.setText(nameString);
            passWord.setText(psString);
        }
    }
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(View.VISIBLE);
                try {
                    progressAnimator(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mInputLayout.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void progressAnimator(final View view) throws InterruptedException {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();


        boolean endFlag=login(nameString,psString);



    }
    public android.os.Handler handler=new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {

            if(msg.what==1){
                ACache mCache=ACache.get(getApplication(),"User");
                String username = (String) BmobUser.getObjectByKey("username");

                mCache.put("username",username);

                Intent intent=new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
            if(msg.what==0){
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
            super.handleMessage(msg);
        }

    };
    private boolean login(String nameString, String psString) throws InterruptedException {
        final String name=nameString;
        final String ps=psString;


                    System.out.println(Thread.currentThread().getName()+"=》进入");
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    ACache mCache=ACache.get(getApplication(),"User");
                    if(bmobUser != null||(mCache.getAsString("username")!=null)){
                        // 允许用户使用应用
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
                    }else{
                        //缓存用户对象为空时， 可打开用户注册界面…
                        //缓存用户对象为空时，可以使用当前的用户名密码登录
                        BmobUser bu2 = new BmobUser();
                        bu2.setUsername(name);
                        bu2.setPassword(ps);

                        bu2.login(new SaveListener<BmobUser>() {

                            @Override
                            public void done(BmobUser bmobUser, BmobException e) {
                                if(e==null){
                                    Toast.makeText(getApplication(),"登录成功:",Toast.LENGTH_SHORT).show();
                                    TAG=1;

                                    System.out.println(Thread.currentThread().getName()+"休息结束");

                                    Log.i("ture","fdsa");
                                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                                }else{
                                    Toast.makeText(getApplication(),"登录失败请检查用户名和密码:",Toast.LENGTH_LONG).show();
                                    TAG=0;

                                }
                                Message msg=new Message();
                                msg.what=TAG;
                                handler.sendMessage(msg);
                                //
                                Log.i("com","aaa");
                            }

                        });


                    }


        Thread.sleep(1000);
        Log.i("TAG",TAG+"");
        return TAG==1;
    }
}
