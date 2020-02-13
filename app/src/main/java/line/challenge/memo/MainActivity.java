package line.challenge.memo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button attachFromAlbumBtn;
    Button attachNewPictureBtn;
    Button attachFromLinkBtn;
    ImageView imageView;
    View view;

    final int TAKE_A_PICTURE_REQUEST_CODE = 1000;
    final int GET_PICTURE_FROM_ALBUM_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachFromAlbumBtn = findViewById(R.id.album_button);
        attachNewPictureBtn = findViewById(R.id.new_button);
        attachFromLinkBtn = findViewById(R.id.link_button);
        imageView = findViewById(R.id.image_view);
        attachFromAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GET_PICTURE_FROM_ALBUM_REQUEST_CODE);
            }
        });
        attachNewPictureBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_A_PICTURE_REQUEST_CODE);});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_A_PICTURE_REQUEST_CODE:
                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    break;
                case GET_PICTURE_FROM_ALBUM_REQUEST_CODE:
                    String result = data.getDataString();
            }
        }
    }
}
