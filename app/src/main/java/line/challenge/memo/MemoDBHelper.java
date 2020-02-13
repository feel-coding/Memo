package line.challenge.memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDBHelper extends SQLiteOpenHelper {
    public MemoDBHelper(Context context) {
        super(context, "memos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE memos(_id INT PRIMARY KEY AUTOINCREMENT, memo TEXT, hasImage INT NOT NULL, imageNumber INT)");
        db.execSQL("CREATE TABLE images(_id INT PRIMARY KEY AUTOINCREMENT,memoId INTEGER NOT NULL, uri TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
