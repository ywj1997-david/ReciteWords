package com.example.recitewords.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recitewords.R;
import com.example.recitewords.recyclerview.StudyAndReviewWordsAdapter;
import com.example.recitewords.roomdb.AppDatabase;
import com.example.recitewords.roomdb.Word;
import com.example.recitewords.roomdb.WordDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 学习和复习单词
 */
public class StudyAndReviewActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, StudyAndReviewActivity.class);
        context.startActivity(starter);
    }

    WordDao wordDao;
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    StudyAndReviewWordsAdapter studyAndReviewWordsAdapter;
    List<Word> wordList = new ArrayList<>();
    ImageView ivBack;
    TextView tvTitle;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_and_review);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        recyclerView = findViewById(R.id.recyclerView);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        etSearch = findViewById(R.id.etSearch);
        tvTitle.setText("学习和复习");
        ivBack.setOnClickListener(view -> {
            MainActivity.start(StudyAndReviewActivity.this);
            finish();
        });
        //通过room构建数据库
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "wordDatabase")
                .allowMainThreadQueries()
                .build();
        //拿到wordDao
        wordDao = appDatabase.wordDao();
        wordList = wordDao.getAllWord();

        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StudyAndReviewActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        studyAndReviewWordsAdapter = new StudyAndReviewWordsAdapter(StudyAndReviewActivity.this, wordList, onClickListener);
        recyclerView.setAdapter(studyAndReviewWordsAdapter);
    }

    public StudyAndReviewWordsAdapter.OnItemClickListener onClickListener = new StudyAndReviewWordsAdapter.OnItemClickListener() {
        @Override
        public void closeChinese(Word word) {
            word.setCloseChinese(!word.getCloseChinese());
            recyclerView.setAdapter(studyAndReviewWordsAdapter);
        }

        @Override
        public void closeEnglish(Word word) {
            word.setCloseEnglish(!word.getCloseEnglish());
            recyclerView.setAdapter(studyAndReviewWordsAdapter);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
}