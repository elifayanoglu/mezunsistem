package com.example.mezunsistem.activities;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mezunsistem.R;
import com.example.mezunsistem.databinding.ActivitySignInBinding;
import com.example.mezunsistem.databinding.ActivityUsersBinding;
import com.example.mezunsistem.utilities.Constants;
import com.example.mezunsistem.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    RoundedImageView photo;
    EditText nname, eemail, giris_yili, mezun_yili, telefon, ulke, sehir, firma, sosyal_medya_hesap;
    CheckBox chk1, chk2, chk3;
    Button buttonUpdate;
    ImageView back_btn;
    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    String currentUserId;
    String c;

   // private ActivitySignInBinding binding;
    //private ActivityUsersBinding binding;

    private PreferenceManager preferenceManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        preferenceManager = new PreferenceManager(getApplicationContext());


        photo = findViewById(R.id.imageProfileUpdate);
        nname = findViewById(R.id.inputName);
        eemail = findViewById(R.id.inputEmail);
        giris_yili = findViewById(R.id.girisYili);
        mezun_yili = findViewById(R.id.mezunYili);
        telefon = findViewById(R.id.telefon);
        ulke = findViewById(R.id.ülke);
        sehir = findViewById(R.id.sehir);
        firma = findViewById(R.id.firma);
        sosyal_medya_hesap = findViewById(R.id.sosyal_medya_hesap);
        chk1 = findViewById(R.id.chk1);

        chk2 = findViewById(R.id.chk2);
        chk3 = findViewById(R.id.chk3);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        back_btn = findViewById(R.id.imageBackMain);

        chk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk1.isChecked()){
                    //chk1.setText(c1);
                    c = "Lisans";


                }
            }
        });

        chk2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk2.isChecked()){
                    //chk2.setText(c2);
                    c = "Yüksek Lisans";


                }
            }
        });

        chk3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk3.isChecked()){
                    //chk3.setText(c3);
                    c = "Doktora";


                }
            }
        });





        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nname.getText().toString();
                String email = eemail.getText().toString();
                String girisyili = giris_yili.getText().toString();
                String mezunyili = mezun_yili.getText().toString();
                String telefonn = telefon.getText().toString();
                String ulkee = ulke.getText().toString();
                String sehirr = sehir.getText().toString();
                String firmaa = firma.getText().toString();
                String sosyal = sosyal_medya_hesap.getText().toString();
               // String cd = c.toString();

                /*nname.setText("");
                eemail.setText("");
                giris_yili.setText("");
                mezun_yili.setText("");
                telefon.setText("");
                ulke.setText("");
                sehir.setText("");
                firma.setText("");
                sosyal_medya_hesap.setText("");
                chk1.setText("");
                chk2.setText("");
                chk3.setText("");*/




                if (!chk1.isChecked() & !chk2.isChecked() & !chk3.isChecked()){
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    firestore.collection("users").document(currentUserId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String egitimDurumu = documentSnapshot.getString("egitim_durumu");
                                        //og.d(TAG, "egitimDurumu: " + egitimDurumu);
                                        // egitim_durumu alanını burada kullanabilirsiniz
                                        UpdateData(name,email,girisyili,mezunyili,telefonn,ulkee,sehirr,firmaa,sosyal,egitimDurumu);
                                    } else {

                                        Toast.makeText(EditProfile.this, "Belirtilen belge yok", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfile.this, "Hata oluştu:", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    UpdateData(name,email,girisyili,mezunyili,telefonn,ulkee,sehirr,firmaa,sosyal,c);
                }


            }
        });

        setListeners();




        // Güncelle butonuna tıklanınca veritabanına veriler kaydediliyor
         /* findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nname.getText().toString();
                String email = eemail.getText().toString();
                String girisYili = giris_yili.getText().toString();

                // Verilerin güncellemesi için kullanılacak HashMap
                HashMap<String, Object> updateData = new HashMap<>();
                updateData.put("name", name);
                updateData.put("email", email);
                updateData.put("girisYili", girisYili);

              mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();


                if(currentUser!= null) {
                    currentUserId = currentUser.getUid(); //Do what you need to do with the id
                    // Verileri veritabanına kaydet
                    //databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(updateData);
                    databaseReference.child(currentUserId).updateChildren(updateData);

                    Toast.makeText(EditProfile.this, "Profil güncellendi", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(EditProfile.this, "Profil güncellenemiyor", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void setListeners(){
        back_btn.setOnClickListener(v -> onBackPressed());
    }

    private void UpdateData(String name, String email, String girisyili, String mezunyili, String telefonn, String ulkee, String sehirr, String firmaa, String sosyal, String cb) {
        Map<String, Object> userDetail = new HashMap<>();
        if(!name.isEmpty()){
            userDetail.put("name",name);
        }
        if (!email.isEmpty()){
            userDetail.put("email",email);
        }
        if (!girisyili.isEmpty()){
            userDetail.put("giris_yili",girisyili);
        }
        if (!mezunyili.isEmpty()) {
            userDetail.put("mezun_yili",mezunyili);
        }
        if (!telefonn.isEmpty()){
            userDetail.put("telefon",telefonn);
        }
        if (!ulkee.isEmpty()) {
            userDetail.put("ulke",ulkee);
        }
        if (!sehirr.isEmpty()) {
            userDetail.put("sehir",sehirr);
        }
        if (!firmaa.isEmpty()) {
            userDetail.put("firma",firmaa);
        }
        if (!sosyal.isEmpty()) {
            userDetail.put("sosyal_medya_hesap",sosyal);
        }
        if (!cb.isEmpty()) {
            userDetail.put("egitim_durumu",cb);
        }



        /*FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful() && !task.getResult().isEmpty()){

                            DocumentSnapshot  documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            database.collection("users")
                                    .document(documentID)
                                    .update(userDetail)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(EditProfile.this, "Profil Güncellendi", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, "Hata oluştu", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }else{
                            Toast.makeText(EditProfile.this, "Hata oluştu2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {

        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        if (currentUserId != null) {
            database.collection("users")
                    .document(currentUserId)
                    .update(userDetail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfile.this, "Profil Güncellendi", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(EditProfile.this, "Kullanıcı kimliği bulunamadı", Toast.LENGTH_SHORT).show();
        }

    });
    }
}