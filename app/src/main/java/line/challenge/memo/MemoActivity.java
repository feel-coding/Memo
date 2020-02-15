package line.challenge.memo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    SQLiteDatabase db;
    MemoDBHelper dbHelper;
    Button attachFromAlbumBtn;
    Button attachNewPictureBtn;
    Button attachFromLinkBtn;
    ImageView imageView;
    EditText title;
    EditText memoText;
    List<String> imageUrl = new ArrayList<>();

    static final int TAKE_A_PICTURE_REQUEST_CODE = 1000;
    static final int GET_PICTURE_FROM_ALBUM_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        if (getSupportActionBar() != null) { //액션바 설정
            //getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        attachFromAlbumBtn = findViewById(R.id.album_button);
        attachNewPictureBtn = findViewById(R.id.new_button);
        attachFromLinkBtn = findViewById(R.id.link_button);
        imageView = findViewById(R.id.image_view);
        title = findViewById(R.id.memo_title);
        memoText = findViewById(R.id.memo_text);
        dbHelper = new MemoDBHelper(this);

        View.OnClickListener choosePictureListener = v -> { //클릭 이벤트 처리할 리스너 객체
            Log.d("testest", "리스너에 들어옴");
            Intent intent = new Intent();
            int requestCode = -1;
            switch (v.getId()) { //앨범에서 사진 선택해서 첨부할 때
                case R.id.album_button:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
                    requestCode = GET_PICTURE_FROM_ALBUM_REQUEST_CODE;
                    break;
                case R.id.new_button: //새로 촬영해서 첨부할 때
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    requestCode = TAKE_A_PICTURE_REQUEST_CODE;
                    break;
                case R.id.link_button: //외부 이미지 링크 첨부할 때
                    Log.d("testest", "pressed");
                    final EditText et = new EditText(this);

                    final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);

                    alt_bld.setTitle("URL로 외부 이미지 첨부")

                            .setMessage("변경할 닉네임을 입력하세요")

                            .setCancelable(false)

                            .setView(et)

                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int id) {

                                    String value = et.getText().toString();

                                    Log.d("linklink", value);

                                }

                            });

                    AlertDialog alert = alt_bld.create();

                    alert.show();
                    //String imgUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png";
                    //Glide.with(this).load(imgUrl).into(imageView);
            }
            if(v.getId() != R.id.link_button) startActivityForResult(intent, requestCode);
        };

        //버튼에 리스너 등록
        attachFromAlbumBtn.setOnClickListener(choosePictureListener);
        attachNewPictureBtn.setOnClickListener(choosePictureListener);
        attachFromLinkBtn.setOnClickListener(choosePictureListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_A_PICTURE_REQUEST_CODE:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    break;
                case GET_PICTURE_FROM_ALBUM_REQUEST_CODE:
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    imageUrl.add(uri.toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", title.getText().toString());
                values.put("memo", memoText.getText().toString());
                if(imageUrl.size() > 0) //첨부된 이미지가 있다면
                    values.put("hasImage", 1);
                else
                    values.put("hasImage", 0);
                long id = db.insert("memos", null, values);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
