package com.example.myapplication.city_manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.service.GetWeatherService;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView errorIv,rightIv;
    ListView deleteLv;
    List<String>mDatas;   //listview的数据源
    List<String>deleteCitys;  //表示存储了删除的城市信息
    private DeleteCityAdapter adapter;

    private int bgNum;
    LinearLayout outLayout;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorIv = findViewById(R.id.delete_iv_error);
        rightIv = findViewById(R.id.delete_iv_right);
        deleteLv = findViewById(R.id.delete_lv);
        outLayout = findViewById(R.id.main_out_layout);
        exchangeBg();

        mDatas = DBManager.queryAllCityName();
        deleteCitys = new ArrayList<>();

//        设置点击监听事件
        errorIv.setOnClickListener(this);
        rightIv.setOnClickListener(this);
//        适配器的设置
        adapter = new DeleteCityAdapter(this, mDatas, deleteCitys);
        deleteLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_iv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息").setMessage("您确定要舍弃更改么？")
                        .setPositiveButton("舍弃更改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    finish();   //关闭当前的activity
                            }
                        });
                builder.setNegativeButton("取消",null);
//                取消对话框，传入空
                builder.create().show();
                break;
            case R.id.delete_iv_right:
                for (int i = 0; i < deleteCitys.size(); i++) {
                    String city = deleteCitys.get(i);
//                    调用删除城市的函数
                    int i1 = DBManager.deleteInfoByCity(city);
                    //删除完毕后要提示小组件也更新一下
                        GetWeatherService.notifyDatabaseUpdate();
                }
//                删除成功返回上一级页面
                finish();
                break;
        }
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
