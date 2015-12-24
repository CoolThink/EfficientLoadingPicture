package com.thinkcool.efficientloadingpicture.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.thinkcool.efficientloadingpicture.R;
import com.thinkcool.efficientloadingpicture.Utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mTakePhoneButton;
    private ImageView mPreviewImageView;
    public static final int TAKE_PHOTO = 0;
    private String photoPath = Environment.getExternalStorageDirectory() + "/outout_img.jpg";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        mTakePhoneButton.setOnClickListener(this);
        int w = mPreviewImageView.getWidth();
        int h = mPreviewImageView.getHeight();
    }

    private void init() {
        mTakePhoneButton = (Button) findViewById(R.id.btn_take_photo);
        mPreviewImageView = (ImageView) findViewById(R.id.img_preview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                File file = new File(photoPath);
                try {
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                0);
                    }

                    Bitmap bitmap = null;
                    int requestWidth = mPreviewImageView.getWidth();
                    int requestHeight = mPreviewImageView.getHeight();
                    //不处理直接加载
//                    bitmap = BitmapFactory.decodeFile(photoPath);
                    //缩放后加载:从file中加载
//                    bitmap = BitmapUtils.getFitSampleBitmap(photoPath,
//                            requestWidth, requestHeight);
                    //缩放后加载:模拟从网络的inputStream中加载,利用的是将其转化为file后在decode
                    try {
                        String tempPath = Environment.getExternalStorageDirectory() + "/temp.jpg";
                        bitmap = BitmapUtils.getFitSampleBitmap(getContentResolver().openInputStream(imageUri),
                                tempPath, requestWidth, requestHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                  缩放后加载:从resources中加载
//                    bitmap = BitmapUtils.getFitSampleBitmap(getResources(), R.drawable.res_img, requestWidth, requestHeight);


                    mPreviewImageView.setImageBitmap(bitmap);

                }
                break;
        }
    }
}

