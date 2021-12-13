package com.example.recitewords.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recitewords.R;
import com.example.recitewords.recyclerview.DateLearnAdapter;
import com.example.recitewords.roomdb.AppDatabase;
import com.example.recitewords.roomdb.Word;
import com.example.recitewords.roomdb.WordDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 打卡学习
 */
public class ClockInStudyActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ClockInStudyActivity.class);
        context.startActivity(starter);
    }

    ImageView ivBack;
    TextView tvTitle, tvTime, tvTest;
    WordDao wordDao;
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    DateLearnAdapter dateLearnAdapter;
    List<Word> wordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in_study);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //通过room构建数据库
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "wordDatabase")
                .allowMainThreadQueries()
                .build();
        //拿到wordDao
        wordDao = appDatabase.wordDao();
        wordList = wordDao.getAllWord();
        recyclerView = findViewById(R.id.recyclerView);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTime = findViewById(R.id.tvTime);
        tvTest = findViewById(R.id.tvTest);
        tvTitle.setText("学习生词");
        ivBack.setOnClickListener(view -> {
            MainActivity.start(ClockInStudyActivity.this);
            finish();
        });

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启倒计时
                timer.start();
            }
        });

        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ClockInStudyActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dateLearnAdapter = new DateLearnAdapter(ClockInStudyActivity.this, wordList, onClickListener);
        recyclerView.setAdapter(dateLearnAdapter);
    }

    public DateLearnAdapter.OnItemClickListener onClickListener = new DateLearnAdapter.OnItemClickListener() {
        @Override
        public void close(Word word) {
            if ("1".equals(word.getLearn())) {
                word.setLearn("0");
            } else {
                word.setLearn("1");
            }
            if ("1".equals(word.getNewWord())) {
                word.setNewWord("0");
            } else {
                word.setNewWord("1");
            }
            wordDao.updateWord(word);
            recyclerView.setAdapter(dateLearnAdapter);
        }
    };

    CountDownTimer timer = new CountDownTimer(10000, 1000) {
        @SuppressLint("SetTextI18n")
        public void onTick(long millisUntilFinished) {
            tvTime.setText(millisUntilFinished / 1000 + "秒");
        }

        public void onFinish() {
            tvTime.setText("0秒");
            Toast.makeText(ClockInStudyActivity.this, "时间到", Toast.LENGTH_SHORT).show();
        }
    };
}