package com.example.recitewords.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recitewords.R;
import com.example.recitewords.recyclerview.NewWordsAdapter;
import com.example.recitewords.roomdb.AppDatabase;
import com.example.recitewords.roomdb.Word;
import com.example.recitewords.roomdb.WordDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 生词
 */
public class NewWordsActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, NewWordsActivity.class);
        context.startActivity(starter);
    }

    //创建自带语音对象
    private TextToSpeech textToSpeech = null;
    WordDao wordDao;
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    NewWordsAdapter newWordsAdapter;
    List<Word> wordList = new ArrayList<>();
    ImageView ivBack;
    TextView tvTitle;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_words);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initTTS();
        recyclerView = findViewById(R.id.recyclerView);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        etSearch = findViewById(R.id.etSearch);
        tvTitle.setText("生词表");
        ivBack.setOnClickListener(view -> {
            MainActivity.start(NewWordsActivity.this);
            finish();
        });
        //通过room构建数据库
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "wordDatabase")
                .allowMainThreadQueries()
                .build();
        //拿到wordDao
        wordDao = appDatabase.wordDao();
        wordList = wordDao.getAllWordByKey("0");
        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewWordsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        newWordsAdapter = new NewWordsAdapter(NewWordsActivity.this, wordList, onClickListener);
        recyclerView.setAdapter(newWordsAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = charSequence.toString();
                Log.d("PlaceFragment", "query is " + content);
                if (content.isEmpty()) {
                    wordList.clear();
                    wordList = wordDao.getAllWordByKey("0");
                    recyclerView.setLayoutManager(linearLayoutManager);
                    newWordsAdapter = new NewWordsAdapter(NewWordsActivity.this, wordList, onClickListener);
                    recyclerView.setAdapter(newWordsAdapter);
                } else {
                    wordList.clear();
                    wordList = wordDao.getAllWordByEnglish(content, "0");
                    recyclerView.setLayoutManager(linearLayoutManager);
                    newWordsAdapter = new NewWordsAdapter(NewWordsActivity.this, wordList, onClickListener);
                    recyclerView.setAdapter(newWordsAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 初始化TextToSpeech对象
     */
    private void initTTS() {

        //实例化自带语音对象
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == 0) {

                    // Toast.makeText(MainActivity.this,"成功输出语音",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);
                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    Log.i("zhh_tts", "US支持否？--》" + a +
                            "\nzh-CN支持否》--》" + b);

                } else {
                    Toast.makeText(NewWordsActivity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public NewWordsAdapter.OnItemClickListener onClickListener = new NewWordsAdapter.OnItemClickListener() {
        @Override
        public void item(Word word) {
            //跳转至有道字典
            Uri uri = Uri.parse("https:///m.youdao.com/dict?le=eng&q=" + word.getEnglish());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }

        @Override
        public void sentence(Word word) {
            textToSpeech.setLanguage(Locale.CHINA);
            //设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.setPitch(4f);
            //设置语速
            textToSpeech.setSpeechRate(1f);
            //输入中文，若不支持的设备则不会读出来
            if (TextUtils.isEmpty(word.getSentence())) {
                Toast.makeText(NewWordsActivity.this, "请输入需要朗读的文字", Toast.LENGTH_SHORT).show();
                return;
            }
            textToSpeech.speak(word.getSentence(),
                    TextToSpeech.QUEUE_FLUSH, null);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
}