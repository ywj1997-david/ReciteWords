package com.example.recitewords.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recitewords.R;
import com.example.recitewords.roomdb.Word;

import java.util.List;


public class DateLearnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Word> mDatas;

    public OnItemClickListener listener;

    public DateLearnAdapter(Context context, List<Word> datas, OnItemClickListener listener) {
        mContext = context;
        mDatas = datas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_date_learn, parent, false);
        return new NormalHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NormalHolder normalHolder = (NormalHolder) holder;
        Word word = mDatas.get(position);
        normalHolder.etSentence.setText(word.getSentence());
        normalHolder.position.setText(String.valueOf(position + 1) + ".");
        if ("1".equals(word.getLearn())) {
            normalHolder.close.setChecked(true);
        } else {
            normalHolder.close.setChecked(false);
        }
        normalHolder.close.setOnClickListener(view -> listener.close(word));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        private TextView etSentence, position;
        private Switch close;

        public NormalHolder(View itemView) {
            super(itemView);
            etSentence = itemView.findViewById(R.id.etSentence);
            position = itemView.findViewById(R.id.position);
            close = itemView.findViewById(R.id.close);

        }
    }

    //定义OnItemClickListener接口
    public interface OnItemClickListener {

        void close(Word word);

    }
}