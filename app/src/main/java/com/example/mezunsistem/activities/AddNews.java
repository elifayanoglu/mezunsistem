package com.example.mezunsistem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mezunsistem.R;
import com.example.mezunsistem.databinding.ActivityAddNewsBinding;
import com.example.mezunsistem.databinding.ActivityMainBinding;
import com.example.mezunsistem.databinding.ActivitySignUpBinding;
import com.example.mezunsistem.models.User;
import com.example.mezunsistem.utilities.Constants;
import com.example.mezunsistem.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddNews extends AppCompatActivity {

    private ActivityAddNewsBinding binding;

    private PreferenceManager preferenceManager;
    EditText title2, desc2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        title2 = findViewById(R.id.title);
        desc2 = findViewById(R.id.description);


        binding.buttonAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title2.getText().toString();
                String desc = desc2.getText().toString();

                title2.setText("");
                desc2.setText("");

                AddNewsData(title,desc);
            }
        });
        setListeners();
    }

    private void setListeners(){
        binding.imageBackMain.setOnClickListener(v -> onBackPressed());

    }

    private void AddNewsData(String title, String desc){
        String authorName = preferenceManager.getString(Constants.KEY_NAME);
        Map<String, Object> newsDetail = new HashMap<>();
        newsDetail.put("title",title);
        newsDetail.put("desc",desc);
        newsDetail.put("author",authorName);


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("news")
                .add(newsDetail)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putString("newsID",documentReference.getId());
                    preferenceManager.putString("title",binding.title.getText().toString());
                    preferenceManager.putString("desc",binding.description.getText().toString());


                    Intent intent = new Intent(getApplicationContext(),NewsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(AddNews.this, "Hata oluştu", Toast.LENGTH_SHORT).show();
                });



        /*FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("news")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful() && !task.getResult().isEmpty()){

                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();

                            database.collection("news")
                                    .document(documentID)
                                    .update(newsDetail)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(AddNews.this, "Duyuru Eklendi", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddNews.this, "Hata oluştu", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }else{
                            Toast.makeText(AddNews.this, "Hata oluştu2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
    }


}