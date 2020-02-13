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
        //memos 테이블에서 title은 메모 제목, memo는 메모 내용, hasImage는 첨부한 사진 있으면 1, 없으면 0
        db.execSQL("CREATE TABLE memos(_id INT PRIMARY KEY AUTOINCREMENT, title TEXT, memo TEXT, hasImage INT NOT NULL)");
        //images 테이블에서 memoId는 이미지가 어느 메모에 첨부된 메모의 id, uri는 이미지의 uri
        db.execSQL("CREATE TABLE images(_id INT PRIMARY KEY AUTOINCREMENT,memoId INT NOT NULL, uri TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
