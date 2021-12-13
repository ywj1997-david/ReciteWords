package com.example.recitewords.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recitewords.R;
import com.example.recitewords.roomdb.Word;

import java.util.List;


public class StudyAndReviewWordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Word> mDatas;

    public OnItemClickListener listener;

    public StudyAndReviewWordsAdapter(Context context, List<Word> datas, OnItemClickListener listener) {
        mContext = context;
        mDatas = datas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_study_review_word, parent, false);
        return new NormalHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        Word word = mDatas.get(position);
        normalHolder.etEnglish.setText(word.getEnglish());
        normalHolder.position.setText(String.valueOf(position + 1) + ".");
        normalHolder.etChinese.setText(word.getChinese());
        normalHolder.closeChinese.setChecked(word.getCloseChinese());
        normalHolder.closeEnglish.setChecked(word.getCloseEnglish());
        if (!word.getCloseChinese()) {
            normalHolder.etChinese.setVisibility(View.VISIBLE);
        } else {
            normalHolder.etChinese.setVisibility(View.GONE);
        }

        if (!word.getCloseEnglish()) {
            normalHolder.etEnglish.setVisibility(View.VISIBLE);
        } else {
            normalHolder.etEnglish.setVisibility(View.GONE);
        }
        normalHolder.closeChinese.setOnClickListener(view -> listener.closeChinese(word));
        normalHolder.closeEnglish.setOnClickListener(view -> listener.closeEnglish(word));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        private TextView etEnglish, etChinese, position;
        private Switch closeChinese, closeEnglish;

        public NormalHolder(View itemView) {
            super(itemView);
            etEnglish = itemView.findViewById(R.id.etEnglish);
            etChinese = itemView.findViewById(R.id.etChinese);
            position = itemView.findViewById(R.id.position);
            closeChinese = itemView.findViewById(R.id.closeChinese);
            closeEnglish = itemView.findViewById(R.id.closeEnglish);
        }
    }

    //定义OnItemClickListener接口
    public interface OnItemClickListener {

        void closeChinese(Word word);

        void closeEnglish(Word word);

    }
}