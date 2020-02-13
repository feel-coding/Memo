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

    final int TAKE_A_PICTURE_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachFromAlbumBtn = findViewById(R.id.album_button);
        attachNewPictureBtn = findViewById(R.id.new_button);
        attachFromLinkBtn = findViewById(R.id.link_button);
        imageView = findViewById(R.id.image_view);
        attachNewPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_A_PICTURE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_A_PICTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}
