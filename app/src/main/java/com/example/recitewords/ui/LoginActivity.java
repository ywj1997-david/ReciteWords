package com.example.recitewords.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recitewords.R;
import com.example.recitewords.db.Constants;
import com.example.recitewords.db.DateBaseHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase sqLiteDatabase;
    DateBaseHelper helper;
    TextView tvRegister;
    TextView tvLogin;
    EditText account, psw;
    String mAccount;
    String mPassword;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        helper = new DateBaseHelper(this);
        sqLiteDatabase = helper.getWritableDatabase();
        tvLogin = findViewById(R.id.tvLogin);
        tvRegister = findViewById(R.id.tvRegister);
        account = findViewById(R.id.et_input);
        psw = findViewById(R.id.et_input2);
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //注册按钮功能
            case R.id.tvRegister:
                //注册相关的弹窗设定，如果确定提交，则完成注册，数据存入数据库
                //AlerDialog：对话框控件
                new AlertDialog.Builder(LoginActivity.this).setTitle("tip")
                        .setMessage("确定提交吗？")
                        //为确定按钮配置监听
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // 获取EditView中的内容，并将其转化成字符串型后存入变量中
                                mAccount = account.getText().toString();
                                mPassword = psw.getText().toString();

                                //给数据表设置游标，Cursor是一个游标，以name字段为依据，query是一种根据条件获取数据的方法
                                Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String[]{"name"}, "name=?", new String[]{mAccount}, null, null, null);
                                //如果游标找到了所需要的name，则返回已注册，否则就利用之前写的insert方法插入数据
                                if (cursor.getCount() != 0) {
                                    Toast.makeText(LoginActivity.this, "该用户已经被注册", Toast.LENGTH_SHORT).show();
                                } else {
                                    helper.insert(sqLiteDatabase, mAccount, mPassword);
                                    Toast.makeText(LoginActivity.this, "注册成功，请登录 !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                //这个是点击返回后的操作，因为不需要，所以不管他直接跳出就好。

                            }
                        }).show();
                break;

            //登录按钮功能，上面解释过的已省略
            case R.id.tvLogin:
                String user_str = account.getText().toString();
                String psw_str = psw.getText().toString();
                //账号或密码为空时
                if (user_str.equals("")) {
                    Toast.makeText(this, "账号或者密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME, new String[]{"password"}, "name=?", new String[]{user_str}, null, null, null);
                    //游标的遍历，寻找name对应的password的值
                    if (cursor.moveToNext()) {
                        String psw_query = cursor.getString(cursor.getColumnIndex("password"));
                        //用户名对应的密码与输入的密码相同时
                        if (psw_str.equals(psw_query)) {
                            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "密码错误 !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "账号不存在，请先注册 ！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}