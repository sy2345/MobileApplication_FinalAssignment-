package com.example.myapplication.city_manager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.db.DBManager;
import com.example.myapplication.db.DatabaseBean;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addIv,backIv,deleteIv;
    ListView cityLv;
    List<DatabaseBean> mDatas;  //显示列表数据源
    private CityManagerAdapter adapter;

    private int bgNum;
    RelativeLayout outLayout;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv = findViewById(R.id.city_iv_add);
        backIv = findViewById(R.id.city_iv_back);
        deleteIv = findViewById(R.id.city_iv_delete);
        cityLv = findViewById(R.id.city_lv);
        outLayout = findViewById(R.id.main_out_layout);
        exchangeBg();

        mDatas = new ArrayList<>();
//        添加点击监听事件
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
//        设置适配器
        adapter = new CityManagerAdapter(this, mDatas);
        cityLv.setAdapter(adapter);
    }
/*  获取数据库当中真实数据源，添加到原有数据源当中，提示适配器更新*/
//    重新获取焦点，重新设置数据源
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseBean> list = DBManager.queryAllInfo();
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_add:
                int cityCount = DBManager.getCityCount();
                if (cityCount<5) {
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"存储城市数量已达上限，请删除后再增加",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this, DeleteCityActivity.class);
                startActivity(intent1);
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
