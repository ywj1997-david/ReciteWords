package com.example.recitewords.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word")
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "english")
    private String english;

    @ColumnInfo(name = "chinese")
    private String chinese;

    //0代表生词，1代表不需要补习
    @ColumnInfo(name = "new_word")
    private String newWord;
    //例句
    @ColumnInfo(name = "sentence")
    private String sentence;
    //词组
    @ColumnInfo(name = "phrase")
    private String phrase;
    //词性
    @ColumnInfo(name = "kind")
    private String kind;

    //0代表为学习。1代表已学习
    @ColumnInfo(name = "learn")
    private String learn = "0";

    private Boolean closeEnglish = false;
    private Boolean closeChinese = false;
    //是否学会


    public Word(String english, String chinese, String newWord, String sentence, String phrase, String kind) {
        this.english = english;
        this.chinese = chinese;
        this.newWord = newWord;
        this.sentence = sentence;
        this.phrase = phrase;
        this.kind = kind;
    }

    public String getLearn() {
        return learn;
    }

    public void setLearn(String learn) {
        this.learn = learn;
    }

    public Boolean getCloseEnglish() {
        return closeEnglish;
    }

    public void setCloseEnglish(Boolean closeEnglish) {
        this.closeEnglish = closeEnglish;
    }

    public Boolean getCloseChinese() {
        return closeChinese;
    }

    public void setCloseChinese(Boolean closeChinese) {
        this.closeChinese = closeChinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getNewWord() {
        return newWord;
    }

    public void setNewWord(String newWord) {
        this.newWord = newWord;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
