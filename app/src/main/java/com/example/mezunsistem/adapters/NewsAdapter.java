package com.example.mezunsistem.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mezunsistem.databinding.ActivityNewsBinding;
import com.example.mezunsistem.databinding.ItemContainerUserBinding;
import com.example.mezunsistem.databinding.ItemNewsBinding;
import com.example.mezunsistem.listeners.UserListener;
import com.example.mezunsistem.models.News;
import com.example.mezunsistem.models.User;

import java.util.List;

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<News> news;

    public NewsAdapter(List<News> news) {
        this.news = news;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /*ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new NewsAdapter.NewsViewHolder(itemContainerUserBinding);*/

        ItemNewsBinding itemNewsBinding = ItemNewsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new NewsAdapter.NewsViewHolder(itemNewsBinding);

        /*ActivityNewsBinding activityNewsBinding = ActivityNewsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new NewsAdapter.NewsViewHolder(activityNewsBinding);*/
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        holder.setNewsData(news.get(position));

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
       //ItemContainerUserBinding binding;
       ItemNewsBinding binding2;


      // NewsViewHolder(ItemContainerUserBinding itemContainerUserBinding) {

          // super(itemContainerUserBinding.getRoot());
          // binding = itemContainerUserBinding;
          NewsViewHolder(ItemNewsBinding itemNewsBinding) {
              super(itemNewsBinding.getRoot());
              binding2 = itemNewsBinding;

        }

        void setNewsData(News news1){
              binding2.titleTextView.setText(news1.title);
              binding2.descriptionTextView.setText(news1.desc);
              binding2.authorTextView.setText("Yayımlayan: " + news1.author);

           // binding.textName.setText(news1.newsID);
            //binding.textEmail.setText(news1.title);
            //binding.imageProfile.setImageBitmap(getUserImage(user.image));
           // binding.textName.setText(news1.desc);
            //binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
