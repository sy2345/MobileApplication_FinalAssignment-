package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.user.User;
import com.example.myapplication.user.UserDBOpenHelper;

import java.util.ArrayList;

//implements View.OnClickListener 之后，
//就可以把onClick事件写到onCreate()方法之外
//这样，onCreate()方法中的代码就不会显得很冗余
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBOpenHelper mUserDBOpenHelper;     //声明UserDBOpenHelper对象，这玩意儿用来创建数据表
    private Button mBtLogin;
    private TextView register;
    private EditText mEtUsername;
    private EditText mEtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);    //把视图层 View 也就是 layout 的内容放到 Activity 中进行显示

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        initView();
        mUserDBOpenHelper = new UserDBOpenHelper(this);     //实例化DBOpenhelper，进行登录验证的时候要用来进行数据查询
        mUserDBOpenHelper.add("001","2222");
        mUserDBOpenHelper.add("南湖社区","12345");

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }



    private void initView(){
        //初始化视图中的控件对象
        mBtLogin= findViewById(R.id.bt_loginactivity_login);
        mBtLogin.setOnClickListener(this);      //设置点击事件监听器
        mEtUsername= findViewById(R.id.et_loginactivity_username);
        mEtPassword=findViewById(R.id.et_loginactivity_password);
    }

    public void onClick(View view){
        String name = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
            ArrayList<User> data = mUserDBOpenHelper.getAllData();
            boolean match = false;
            for (int i = 0; i < data.size(); i++) {
                User user = data.get(i);
                if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                    match = true;
                    break;
                } else {
                    match = false;
                }
            }
            if (match) {
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();//销毁此Activity
            } else {
                Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
        }
    }



}
