package line.challenge.memo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.graphics.ImageDecoder.createSource;

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
    File filePath;

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
            Intent intent;
            switch (v.getId()) { //앨범에서 사진 선택해서 첨부할 때
                case R.id.album_button:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent,GET_PICTURE_FROM_ALBUM_REQUEST_CODE);
                    break;
                case R.id.new_button: //새로 촬영해서 첨부할 때
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_A_PICTURE_REQUEST_CODE);
//                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myMemoApp";
//                    File dir = new File(dirPath);
//                    if(!dir.exists())
//                        dir.mkdir();
//                    try {
//                        filePath = File.createTempFile("IMG", ".jpg", dir);
//                        if (!filePath.exists())
//                            filePath.createNewFile();
//                        Uri photoUri = FileProvider.getUriForFile(MemoActivity.this, BuildConfig.APPLICATION_ID + ".provider", filePath);
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                        startActivityForResult(intent, TAKE_A_PICTURE_REQUEST_CODE);
//                    }catch (Exception e) {
//
//                    }
                    break;
                case R.id.link_button: //외부 이미지 링크 첨부할 때

                    Log.d("testest", "pressed");
                    final EditText et = new EditText(this);

                    final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

                    alt_bld.setTitle("URL로 외부 이미지 첨부")
                            .setMessage("url을 입력하세요")
                            .setCancelable(false)
                            .setView(et)
                            .setPositiveButton("확인", (dialog, id) -> {
                                String value = et.getText().toString();
                                /*try {
                                    Bitmap bitmap;
                                    HttpURLConnection connection = (HttpURLConnection) new URL(value).openConnection();
                                    connection.connect();
                                    InputStream is = connection.getInputStream();
                                    bitmap = BitmapFactory.decodeStream(is);
                                    String text = memoText.getText().toString() + "\nimg\n";
                                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                                    int start = text.indexOf("img");
                                    if (start > -1) {
                                        int end = start + "img".length();
                                        Drawable dr = new BitmapDrawable(getResources(), bitmap);
                                        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                                        ImageSpan span = new ImageSpan(dr);
                                        builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    memoText.setText(builder);
                                } catch (MalformedURLException e) {
                                    Toast.makeText(MemoActivity.this, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    System.out.println("통신 도중 오류");
                                }*/
                                Glide.with(MemoActivity.this).load(value).into(imageView);
//                                String text = memoText.getText().toString() + "\nimg\n";
//                                SpannableStringBuilder builder = new SpannableStringBuilder(text);
//                                int start = text.indexOf("img");
//                                if (start > -1) {
//                                    int end = start + "img".length();
//                                    Drawable dr = imageView.getDrawable();
//                        if(Build.VERSION.SDK_INT >= 28) {
//                            try {
//                                dr = ImageDecoder.decodeDrawable(createSource(getContentResolver(), uri));
//                            } catch (IOException e) {
//                                System.out.println("drawable로 decode 하는 도중 IOException");
//                            }
//                        }
//                                    dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
//                                    ImageSpan span = new ImageSpan(dr);
//                                    builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                }
//                                memoText.setText(builder);
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                    AlertDialog alert = alt_bld.create();

                    alert.show();
            }
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
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
//                    if(filePath != null) {
//
//                    }
                    /*String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myMemoApp";
                    File dir = new File(dirPath);
                    if(!dir.exists())
                        dir.mkdir();
                    try {
                        File filePath = File.createTempFile("IMG", ".jpg", dir);
                        if (!filePath.exists())
                            filePath.createNewFile();
                        Uri photoUri = FileProvider.getUriForFile(MemoActivity.this, BuildConfig.APPLICATION_ID + ".provider", filePath);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, TAKE_A_PICTURE_REQUEST_CODE);
                    }catch (Exception e) {

                    }*/


                    String text = memoText.getText().toString() + "\nimg\n";
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    int start = text.indexOf("img");
                    if (start > -1) {
                        int end = start + "img".length();
                        Drawable dr = new BitmapDrawable(getResources(), bitmap);
                        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                        ImageSpan span = new ImageSpan(dr);
                        builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    memoText.setText(builder);
                    break;
                case GET_PICTURE_FROM_ALBUM_REQUEST_CODE:
                    Uri uri = data.getData();
                    text = memoText.getText().toString() + "\nimg" + imageUrl.size() + "\n";
                    imageUrl.add(uri.toString());
                    builder = new SpannableStringBuilder(text);
                    start = text.indexOf("img" + (imageUrl.size() - 1));
                    if (start > -1) {
                        int end = start + ("img" + (imageUrl.size() - 1)).length();
                        Drawable dr = Drawable.createFromPath(uri.getEncodedPath());
                        if(Build.VERSION.SDK_INT >= 28) {
                            try {
                                dr = ImageDecoder.decodeDrawable(createSource(getContentResolver(), uri));
                            } catch (IOException e) {
                                System.out.println("drawable로 decode 하는 도중 IOException");
                            }
                        }
                        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                        ImageSpan span = new ImageSpan(dr);
                        builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    memoText.setText(builder);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: //저장 버튼 눌렸을 때
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", title.getText().toString());
                values.put("memo", memoText.getText().toString());
                if (imageUrl.size() > 0) //첨부된 이미지가 있다면
                    values.put("hasImage", 1);
                else
                    values.put("hasImage", 0);
                long id = db.insert("memos", null, values);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
