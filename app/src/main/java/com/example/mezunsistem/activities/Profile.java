package com.example.mezunsistem.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mezunsistem.R;
import com.example.mezunsistem.models.User;
import com.example.mezunsistem.utilities.Constants;
import com.example.mezunsistem.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    ImageView back_btn;
    RecyclerView usersRecyclerView2;

    //Bu kısmı daha sonra dene profil görüntülemek için layoutta her bir görüntüleme için textview oluştur.
    private TextView nameTextView, companyTextView, egitimTextView, emailTextView,girisTextView,mezunTextView;
    private TextView sehirTextView,ulkeTextView,telTextView,sosyalTextView ;
    private RoundedImageView image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back_btn = findViewById(R.id.imageBackMain);
        image = findViewById(R.id.imageProfileUpdate);
        usersRecyclerView2 = findViewById(R.id.usersRecyclerView2);

        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getMyProfile();

        nameTextView = findViewById(R.id.nameTextView);
        companyTextView = findViewById(R.id.companyTextView);
        egitimTextView = findViewById(R.id.egitimTextView);
        emailTextView = findViewById(R.id.emailTextView);
        girisTextView = findViewById(R.id.girisTextView);
        mezunTextView = findViewById(R.id.mezunTextView);
        sehirTextView = findViewById(R.id.sehirTextView);
        ulkeTextView = findViewById(R.id.ulkeTextView);
        telTextView = findViewById(R.id.telTextView);
        sosyalTextView = findViewById(R.id.sosyalTextView);


    }



    private void setListeners(){
        back_btn.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


   private void getMyProfile() {
        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {


                                String name = queryDocumentSnapshot.getString("name");
                                String company = queryDocumentSnapshot.getString("firma");
                                String egitim = queryDocumentSnapshot.getString("egitim_durumu");
                                String email = queryDocumentSnapshot.getString("email");
                                String giris = queryDocumentSnapshot.getString("giris_yili");
                                String mezun = queryDocumentSnapshot.getString("mezun_yili");
                                String sehir = queryDocumentSnapshot.getString("sehir");
                                String ulke = queryDocumentSnapshot.getString("ulke");
                                String tel = queryDocumentSnapshot.getString("telefon");
                                String sosyal = queryDocumentSnapshot.getString("sosyal_medya_hesap");
                                String encodedImage = queryDocumentSnapshot.getString("image");
                                image.setImageBitmap(getUserImage(encodedImage));


                                nameTextView.setText("İsim: " + name);
                                companyTextView.setText("Şirket: "+ company);
                                egitimTextView.setText("Eğitim Durumu: "+egitim);
                                emailTextView.setText("Email: "+email);
                                girisTextView.setText("Giriş Yılı: "+giris);
                                mezunTextView.setText("Mezun Yılı: "+mezun);
                                sehirTextView.setText("Şehir: "+sehir);
                                ulkeTextView.setText("Ülke: "+ulke);
                                telTextView.setText("Telefon: "+tel);
                                sosyalTextView.setText("Sosyal Medya Hesap Linki: \n"+sosyal);



                            }

                        }
                    }else {
                        Toast.makeText(Profile.this, "Profil yüklenemedi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }




}