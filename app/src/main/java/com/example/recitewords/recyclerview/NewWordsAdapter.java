package com.example.recitewords.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recitewords.R;
import com.example.recitewords.roomdb.Word;

import java.util.List;


public class NewWordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Word> mDatas;

    public OnItemClickListener listener;

    public NewWordsAdapter(Context context, List<Word> datas, OnItemClickListener listener) {
        mContext = context;
        mDatas = datas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_new_word, parent, false);
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
        normalHolder.etKind.setText(word.getKind());
        normalHolder.etPhrase.setText(word.getPhrase());
        normalHolder.etSentence.setText(word.getSentence());
        normalHolder.item.setOnClickListener(view -> listener.item(word));
        normalHolder.etSentence.setOnClickListener(view -> listener.sentence(word));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        private TextView etEnglish, etChinese, etKind, etPhrase, etSentence, position;
        private LinearLayout item;

        public NormalHolder(View itemView) {
            super(itemView);
            etEnglish = itemView.findViewById(R.id.etEnglish);
            etChinese = itemView.findViewById(R.id.etChinese);
            etKind = itemView.findViewById(R.id.etKind);
            etPhrase = itemView.findViewById(R.id.etPhrase);
            etSentence = itemView.findViewById(R.id.etSentence);
            item = itemView.findViewById(R.id.item);
            position = itemView.findViewById(R.id.position);
        }
    }

    //定义OnItemClickListener接口
    public interface OnItemClickListener {

        void item(Word word);

        void sentence(Word word);

    }
}