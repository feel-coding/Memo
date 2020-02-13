package line.challenge.memo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MemoActivity extends AppCompatActivity {

    SQLiteDatabase db;
    MemoDBHelper dbHelper;
    Button attachFromAlbumBtn;
    Button attachNewPictureBtn;
    Button attachFromLinkBtn;
    ImageView imageView;

    static final int TAKE_A_PICTURE_REQUEST_CODE = 1000;
    static final int GET_PICTURE_FROM_ALBUM_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        if (getSupportActionBar() != null) { //액션바 설정
            getSupportActionBar().setTitle("");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFD966));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        attachFromAlbumBtn = findViewById(R.id.album_button);
        attachNewPictureBtn = findViewById(R.id.new_button);
        attachFromLinkBtn = findViewById(R.id.link_button);
        imageView = findViewById(R.id.image_view);
        dbHelper = new MemoDBHelper(this);

        View.OnClickListener choosePictureListener = v -> { //클릭 이벤트 처리할 리스너 객체 생성
            Intent intent = new Intent();
            int requestCode = -1;
            switch (v.getId()) { //앨범에서 사진 선택해서 첨부
                case R.id.album_button:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
                    requestCode = GET_PICTURE_FROM_ALBUM_REQUEST_CODE;
                    break;
                case R.id.new_button: //새로 촬영해서 첨부
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    requestCode = TAKE_A_PICTURE_REQUEST_CODE;
                    break;
                case R.id.link_button: //외부 이미지 링크 첨부
            }
            startActivityForResult(intent, requestCode);
        };

        attachFromAlbumBtn.setOnClickListener(choosePictureListener);
        attachNewPictureBtn.setOnClickListener(choosePictureListener);

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
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}