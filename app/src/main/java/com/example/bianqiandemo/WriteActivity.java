package com.example.bianqiandemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WriteActivity extends AppCompatActivity {
    private Button btback,btfinish;
    private EditText secondEdit;
    private TextView secondTime;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ActionBar actionBar=getSupportActionBar();//隐藏标题栏
        if(actionBar!=null){
            actionBar.hide();
        }

        btback=(Button) findViewById(R.id.second_bt1);//返回按钮
        btfinish=(Button) findViewById(R.id.second_bt2);//完成按钮
        secondEdit=(EditText) findViewById(R.id.second_edit);//编辑框

        secondTime=(TextView)findViewById(R.id.write_time);
        secondTime.setText(new Time().getTimer());//显示当前时间

        btback.setOnClickListener(new View.OnClickListener() {//返回按钮点击事件
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WriteActivity.this,MainActivity.class);//返回到主界面
                startActivity(intent);
                finish();//结束活动
            }
        });

        databaseHelper=new MyDatabaseHelper(this,"MessageStore.db",null,2);//建表
        Intent intent=getIntent();
        final String text1=intent.getStringExtra("text");//接收MainActivity传递的数据
        secondEdit.setText(text1);//将接收的文字显示出来

        btfinish.setOnClickListener(new View.OnClickListener() {//完成按钮的点击的事件
            @Override
            public void onClick(View v) {
                    String text=secondEdit.getText().toString().trim();//获取输入的文字
                    if (text.equals("")) {//判断输入是否为空
                        Toast.makeText(getApplicationContext(),"输入不能为空",Toast.LENGTH_SHORT).show();//短时间弹出显示
                        finish();
                    }else{
                        String time=new Time().getTimer();//获取当前时间
                        SQLiteDatabase db=databaseHelper.getReadableDatabase();
                        ContentValues values=new ContentValues();
                        //开始组装内容
                        values.put("text",text);
                        values.put("time",time);
                        db.insert("Message",null,values);//插入数据
                        values.clear();
                        Toast.makeText(WriteActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

            }
        });

    }
}
