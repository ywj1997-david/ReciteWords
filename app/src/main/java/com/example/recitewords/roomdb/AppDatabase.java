package com.example.recitewords.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @Description:
 * @Author: yangwj
 * @CreateDate: 2021/12/11 23:52
 * @UpdateUser:
 * @UpdateDate: 2021/12/11 23:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Database(entities = {Word.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
}
