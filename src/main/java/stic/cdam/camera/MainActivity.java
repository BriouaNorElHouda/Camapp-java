package stic.cdam.camera;

import org.json.JSONObject;
import com.android.volley.Request;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private static final int requestCode  = 1;
    String  filePath="res/drawable/file_path.xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//camera
//
        ImageView imageoff = findViewById(R.id.imageoff);
        imageoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsDialog();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{

                    Manifest.permission.CAMERA}, 99);

        } else { // la permission est autorisée déjà... }


         //   Picasso.with(this).load("[URL]").into(imageoff);
        }




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[]

            permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 99 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission autorisée ...
        }
    }
    private boolean checkCameraHardware(Context ctx) {

        if(ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
// Cet appareil possède une caméra

            return true;
        } else { // Cet appareil ne possède pas une caméra

            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
        if (requestCode == 99 && resultCode == RESULT_OK) {
            Bundle b = i.getExtras();
            Bitmap photo = (Bitmap) b.get("data");
            ImageView imageView = findViewById(R.id.imageoff);
            imageView.setImageBitmap(photo);
                } else if (i != null && i.getData() != null) {
                    //njibou l img mn l gallery lazemna path
                    Uri selectedImageUri = i.getData();
                    try {
                        Bitmap selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        ImageView imageView = findViewById(R.id.imageoff);
                        imageView.setImageBitmap(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

    private void showOptionsDialog() {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setMessage("Do you want to upload an image or take a new one?")
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Launch gallery to choose an image
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, requestCode );
                    }
                })
                .setNegativeButton("Take Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //thers camera then aft7
                        if (checkCameraHardware(MainActivity.this)) {
                            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                             if (camIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(camIntent, 99); // 99 = requestCode
                            }
                        } else {
                           //makanch yaaa
                            Toast.makeText(MainActivity.this, "No camera available", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builder.create().show();
    }





}