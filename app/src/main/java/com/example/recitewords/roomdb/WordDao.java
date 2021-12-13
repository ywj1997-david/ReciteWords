package com.example.recitewords.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @Description:
 * @Author: yangwj
 * @CreateDate: 2021/12/11 23:45
 * @UpdateUser:
 * @UpdateDate: 2021/12/11 23:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertWord(Word... word);

    @Query("SELECT * FROM word ORDER BY ID DESC")
    List<Word> getAllWord();

    //查找生词、熟词
    @Query("SELECT * FROM word WHERE new_word = :value")
    List<Word> getAllWordByKey(String value);

    //根据英文查找单词
    @Query("SELECT * FROM word WHERE english like '%' || :value || '%'  and new_word = :newWord")
    List<Word> getAllWordByEnglish(String value, String newWord);

    @Update
    public void updateWord(Word... words);
}
