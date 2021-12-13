package com.example.recitewords.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recitewords.R;
import com.example.recitewords.roomdb.AppDatabase;
import com.example.recitewords.roomdb.Word;
import com.example.recitewords.roomdb.WordDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AddActivity.class);
        context.startActivity(starter);
    }

    WordDao wordDao;
    AppDatabase appDatabase;
    private TextView tvAdd, tvBack;
    private EditText etEnglish, etChinese, etKind, etPhrase, etSentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //通过room构建数据库
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "wordDatabase")
                .allowMainThreadQueries()
                .build();
        //拿到wordDao
        wordDao = appDatabase.wordDao();

        tvAdd = findViewById(R.id.tvAdd);
        tvBack = findViewById(R.id.tvBack);
        etEnglish = findViewById(R.id.etEnglish);
        etChinese = findViewById(R.id.etChinese);
        etKind = findViewById(R.id.etKind);
        etPhrase = findViewById(R.id.etPhrase);
        etSentence = findViewById(R.id.etSentence);

        tvBack.setOnClickListener(view -> {
            MainActivity.start(AddActivity.this);
            finish();
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etEnglish.getText()) ||
                        TextUtils.isEmpty(etChinese.getText()) ||
                        TextUtils.isEmpty(etKind.getText()) ||
                        TextUtils.isEmpty(etPhrase.getText()) ||
                        TextUtils.isEmpty(etSentence.getText())) {
                    Toast.makeText(AddActivity.this, "请全部输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                Word word = new Word(etEnglish.getText().toString(),
                        etChinese.getText().toString(), "0",
                        etSentence.getText().toString(),
                        etPhrase.getText().toString(),
                        etKind.getText().toString());
                wordDao.insertWord(word);
                Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                etEnglish.setText("");
                etChinese.setText("");
                etKind.setText("");
                etPhrase.setText("");
                etSentence.setText("");
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
}