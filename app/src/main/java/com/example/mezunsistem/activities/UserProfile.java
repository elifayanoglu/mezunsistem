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
import com.example.mezunsistem.databinding.ActivityUserProfileBinding;
import com.example.mezunsistem.models.User;
import com.example.mezunsistem.utilities.Constants;
import com.example.mezunsistem.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    private User receiverUser;
    String userEmail;

    private PreferenceManager preferenceManager;
    ImageView back_btn;
    RecyclerView usersRecyclerView2;

    //Bu kısmı daha sonra dene profil görüntülemek için layoutta her bir görüntüleme için textview oluştur.
    private TextView nameTextView, companyTextView, egitimTextView, emailTextView,girisTextView,mezunTextView;
    private TextView sehirTextView,ulkeTextView,telTextView,sosyalTextView ;
    private RoundedImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        //back_btn = findViewById(R.id.imageBackMain);
        loadReceiverDetails();

        //setContentView(R.layout.activity_user_profile);


        image = findViewById(R.id.imageProfileUpdate2);
        usersRecyclerView2 = findViewById(R.id.usersRecyclerView2);

        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();


        nameTextView = findViewById(R.id.nameTextView2);
        companyTextView = findViewById(R.id.companyTextView2);
        egitimTextView = findViewById(R.id.egitimTextView2);
        emailTextView = findViewById(R.id.emailTextView2);
        girisTextView = findViewById(R.id.girisTextView2);
        mezunTextView = findViewById(R.id.mezunTextView2);
        sehirTextView = findViewById(R.id.sehirTextView2);
        ulkeTextView = findViewById(R.id.ulkeTextView2);
        telTextView = findViewById(R.id.telTextView2);
        sosyalTextView = findViewById(R.id.sosyalTextView2);


    }


    private void setListeners(){
        binding.imageBackMain.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, UsersActivity.class);
        startActivity(i);
        finish();
    }

    private void loadReceiverDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        //binding.nameTextView2.setText(receiverUser.name);
        userEmail = receiverUser.email;


        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserEmail = preferenceManager.getString(Constants.KEY_EMAIL);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (userEmail.equals(queryDocumentSnapshot.getString("email"))) {


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
                        Toast.makeText(UserProfile.this, "Profil yüklenemedi", Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }






}