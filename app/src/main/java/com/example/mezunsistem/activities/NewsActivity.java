package com.example.mezunsistem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mezunsistem.R;
import com.example.mezunsistem.adapters.NewsAdapter;
import com.example.mezunsistem.adapters.UsersAdapter;
import com.example.mezunsistem.databinding.ActivityNewsBinding;
import com.example.mezunsistem.databinding.ActivityUsersBinding;
import com.example.mezunsistem.models.News;
import com.example.mezunsistem.models.User;
import com.example.mezunsistem.utilities.Constants;
import com.example.mezunsistem.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private ActivityNewsBinding binding;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_news);

        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getNews();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());

        binding.usersRecyclerView.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.imageAdd.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddNews.class)));

    }

    private void getNews() {
        loading(true);
        FirebaseFirestore database =  FirebaseFirestore.getInstance();
        database.collection("news")
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentNewsId = preferenceManager.getString("newsID");
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<News> news = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            //if (currentNewsId.equals(queryDocumentSnapshot.getId())){
                                News news1 = new News();
                                news1.newsID = queryDocumentSnapshot.getString("newsID");
                                news1.desc = queryDocumentSnapshot.getString("desc");
                                news1.title = queryDocumentSnapshot.getString("title");
                                news1.author = queryDocumentSnapshot.getString("author");


                                news.add(news1);
                           // }

                        }
                        if(news.size() > 0){
                            NewsAdapter newsAdapter = new NewsAdapter(news);
                            binding.usersRecyclerView.setAdapter(newsAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);


                        }else{
                            showErrorMessage();
                        }
                    }else {
                        showErrorMessage();
                    }
                });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s","kullanıcı yok"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }


}