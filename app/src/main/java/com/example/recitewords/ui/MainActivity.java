package com.example.recitewords.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.recitewords.R;
import com.example.recitewords.roomdb.AppDatabase;
import com.example.recitewords.roomdb.Word;
import com.example.recitewords.roomdb.WordDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    WordDao wordDao;
    AppDatabase appDatabase;
    TextView tvAdd, tvStudy, tvDateStudy, tvWatchNewWords, tvWatchOldWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //通过room构建数据库
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "wordDatabase")
                .allowMainThreadQueries()
                .build();
        //拿到wordDao
        wordDao = appDatabase.wordDao();

        List<Word> wordList = wordDao.getAllWord();
        System.out.println(wordList);

        tvAdd = findViewById(R.id.tvAdd);
        tvStudy = findViewById(R.id.tvStudy);
        tvDateStudy = findViewById(R.id.tvDateStudy);
        tvWatchNewWords = findViewById(R.id.tvWatchNewWords);
        tvWatchOldWords = findViewById(R.id.tvWatchOldWords);
        //添加单词
        tvAdd.setOnClickListener(view -> {
            AddActivity.start(MainActivity.this);
            finish();
        });
        //学习和复习生词
        tvStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudyAndReviewActivity.start(MainActivity.this);
                finish();
            }
        });
        //查看生词
        tvWatchNewWords.setOnClickListener(view -> {
            NewWordsActivity.start(MainActivity.this);
            finish();
        });
        //查看熟词
        tvWatchOldWords.setOnClickListener(view -> {
            OldWordsActivity.start(MainActivity.this);
            finish();
        });
        //打卡学习
        tvDateStudy.setOnClickListener(view -> {
            ClockInStudyActivity.start(MainActivity.this);
            finish();
        });
    }

}