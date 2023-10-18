package com.example.iicpshuttle;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
public class ScanMeActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private TextView nameTextView, emailTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_attendance);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // The text you want to encode in the QR code
        String textToEncode = getStudentUid();

        // Generate the QR code
        Bitmap qrCodeBitmap = generateQRCode(textToEncode);

        // Set the QR code to the ImageView
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
    }

    private Bitmap generateQRCode(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 800, 800);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStudentUid(){
        String userUid = mUser.getUid();
        return userUid;
    }


}

