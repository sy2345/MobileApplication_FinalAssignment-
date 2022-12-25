package com.example.myapplication;

import static com.example.myapplication.db.DBManager.FindProvince;
import static com.example.myapplication.db.DBManager.getProvince;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.city_manager.CityManagerActivity;
import com.example.myapplication.city_manager.DeleteCityActivity;
import com.example.myapplication.city_manager.SearchCityActivity;
import com.example.myapplication.covid_19.Covid19Activity;
import com.example.myapplication.db.DBManager;

import java.util.Objects;


public class words extends AppCompatActivity {
    private int bgNum;
    RelativeLayout outLayout;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        outLayout = findViewById(R.id.main_out_layout);
        exchangeBg();

        ImageView backIv = findViewById(R.id.city_iv_back);
        //按钮进行监听
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView rightIv = findViewById(R.id.city_iv_right);
        //按钮进行监听
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(words.this, works1.class);
                startActivity(intent);
            }
        });
    }

    //        换壁纸的函数
    public void exchangeBg(){
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = pref.getInt("bg", 2);
        switch (bgNum) {
            case 0:
                outLayout.setBackgroundResource(R.mipmap.bg);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.bg2);
                break;
            case 2:
                outLayout.setBackgroundResource(R.mipmap.bg3);
                break;
            //自己设置图片的情况下
            case 99:
                //此时需要获取对应的path的路径
                String bg_path = pref.getString("path","default");
                try {
                    //尝试根据这个路径，用Drawable的方法，创建一个Drawable对象
                    Drawable drawable = Drawable.createFromPath(bg_path);
                    //RelativeLayout 有一个方法是把背景设置成drawable对象
                    outLayout.setBackground(drawable);
                    //设置完成
                    break;
                }
                catch (Exception e)
                {
                    //如果上面失败了，就设置默认的
                    e.printStackTrace();
                    Toast.makeText(this, "加载壁纸失败，源文件可能已经被删除，挪动。", Toast.LENGTH_SHORT).show();
                    outLayout.setBackgroundResource(R.mipmap.bg2);
                    break;
                }

        }

    }
}