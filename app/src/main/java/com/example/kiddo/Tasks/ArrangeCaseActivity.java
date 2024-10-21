package com.example.kiddo.Tasks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kiddo.Controller.PointsManager;
import com.example.kiddo.R;

public class ArrangeCaseActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 101;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageCase;
    private PointsManager pointsManager; // تعريف PointsManager
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_case);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // إخفاء AppBar
        }
        ImageButton buttonBack = findViewById(R.id.caseBack);
        imageCase = findViewById(R.id.imageCase);

        Button buttonOpenCamera = findViewById(R.id.caseOpenCamera);
        pointsManager = new PointsManager(this); // تهيئة PointsManager

        buttonBack.setOnClickListener(v -> finish()); // العودة للصفحة السابقة

        buttonOpenCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras(); // الحصول على رابط الصورة
            imageBitmap = (Bitmap) extras.get("data");
            imageCase.setImageBitmap(imageBitmap); // تعيين الصورة الملتقطة في ImageView
            if (imageBitmap != null) {

                // firebaseManager.storeCaseMakingInfo(this, imageBitmap);
                pointsManager.showCompletionDialogWithImage(this,"ترتيب الدولاب- " ,imageBitmap);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied to access camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}