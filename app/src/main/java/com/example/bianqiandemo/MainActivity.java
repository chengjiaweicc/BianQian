package com.example.bianqiandemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private TextView textView;
    private Button write;
    private ListView listView;
    private SQLiteDatabase db;
    private MessageAdapter adapter;
    private List<Message> messageList=new ArrayList<>();
    private MyDatabaseHelper databaseHelper;
    private SearchView searchView;
    private MessageAdapter findAdapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.requestWindowFeature(Window.FEATURE_NO_TITLE); //取消标题栏
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();//隐藏标题栏
        if(actionBar!=null){
            actionBar.hide();
        }

        textView=(TextView)findViewById(R.id.first_tv);//备忘录

        write=(Button)findViewById(R.id.button_write);//编辑按钮
        write.setOnClickListener(new View.OnClickListener() {//点击事件
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,WriteActivity.class);//跳转到编辑界面
                startActivity(intent);//启动活动
            }
        });

        initMessages();//初始化数据,显示所有信息
        adapter=new MessageAdapter(MainActivity.this,R.layout.message_item,messageList);
        listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        searchView=(SearchView) findViewById(R.id.find_et);//搜索框
        listView.setTextFilterEnabled(true);//设置listView可以被过滤
        searchView.setIconifiedByDefault(false);//设置该SearchView默认是否自动缩小为图标
        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setQueryHint("搜索");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//搜索框的监听事件
            List<Message> findList=new ArrayList<>();
            @Override
            public boolean onQueryTextSubmit(String query) {//搜索按钮的点击事件,在本方法内执行实际查询
                if (TextUtils.isEmpty(query)) {//判断查询是否为空
                    Toast.makeText(MainActivity.this,"请输入查找内容",Toast.LENGTH_SHORT).show();
                    listView.setAdapter(adapter);
                }else{
                    findList.clear();
                    for (int i=0;i<messageList.size();i++) {
                       Message  message =messageList.get(i);
                       if (message.getText().equals(query)) {
                           findList.add(message);
                           break;
                       }
                    }
                    if (findList.size() == 0) {
                        Toast.makeText(MainActivity.this,"没有该条消息",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"查找成功",Toast.LENGTH_SHORT).show();
                        findAdapter=new MessageAdapter(MainActivity.this,R.layout.message_item,findList);
                        listView.setAdapter(findAdapter);
                    }
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {//当输入框内容改变的时候回调
                if (TextUtils.isEmpty(newText)) {
                    listView.setAdapter(adapter);
                }else{
                    findList.clear();
                    for (int i=0;i<messageList.size();i++){
                        Message message =messageList.get(i);
                        if (message.getText().contains(newText)) {
                            findList.add(message);
                    }
                }
                    findAdapter=new MessageAdapter(MainActivity.this,R.layout.message_item,findList);
                    findAdapter.notifyDataSetChanged();
                    listView.setAdapter(findAdapter);
            }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//listView的点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message=messageList.get(position);//获取当前内容
                Intent intent=new Intent(MainActivity.this,WriteActivity.class);
                intent.putExtra("text",message.getText());//传递数据到WriteActivity
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//listView的长按事件
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Message message=messageList.get(position);//获取当前实例
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("确定要删除该信息吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text1=message.getText();
                        messageList.remove(message);
                        db.delete("Message","text=?",new String[]{text1});
                        listView.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
    }

    //初始化数据，对数据库进行查询
    private void initMessages() {
        databaseHelper=new MyDatabaseHelper(this,"MessageStore.db",null,2);
        db=databaseHelper.getWritableDatabase();
        //查询Message表中所有的数据
        Cursor cursor=db.query("Message",null,null,null,null,null,null);
        //将数据的指针移到第一行的位置
        if (cursor.moveToFirst()) {
            do {//遍历Cursor对象
                String text=cursor.getString(cursor.getColumnIndex("text"));
                String time=cursor.getString(cursor.getColumnIndex("time"));
                Message m=new Message(text,time);
                messageList.add(m);
            }while (cursor.moveToNext());
        }cursor.close();
    }

}
