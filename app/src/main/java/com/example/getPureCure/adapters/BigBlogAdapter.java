package com.example.getPureCure.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getPureCure.R;
import com.example.getPureCure.objects.Blog;
import com.example.getPureCure.assets.Time;
import com.example.getPureCure.patientPart.ShowSingleBlogActivity;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;

public class BigBlogAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private Context context;

    private ArrayList<Blog> blogArray;

    public BigBlogAdapter(Context context, ArrayList<Blog> blogArray) {
        this.context = context;
        this.blogArray = blogArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_big_blog, parent, false);
        return new BigBlogAdapter.BlogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Blog blog = blogArray.get(position);

        BlogHolder blogHolder = (BlogHolder) holder;

        ImageView blogTitleImageView = blogHolder.blogTitleImageView;
        TextView blogTitleTextView = blogHolder.blogTitleTextView;
        TextView blogDateTextView = blogHolder.blogDateTextView;
        TextView blogLikeTextView = blogHolder.blogLikeTextView;
        TextView blogCommentTextView = blogHolder.blogCommentTextView;

        if(blog.getTitleImageUri() == null || blog.getTitleImageUri().isEmpty()){
            Picasso.get().load(R.drawable.default_blog_picture)
                    .centerCrop()
                    .fit()
                    .error(R.drawable.default_blog_picture)
                    .into(blogTitleImageView);
        }else {
            Picasso.get().load(blog.getTitleImageUri())
                    .centerCrop()
                    .fit()
                    .error(R.drawable.default_blog_picture)
                    .into(blogTitleImageView);
        }
        blogTitleTextView.setText(blog.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            blogDateTextView.setText(Time.getTimeAgo(Instant.parse(blog.getDate()).toEpochMilli()));
        } else {
            blogDateTextView.setText(Time.getTimeDate(blog.getDate()));
        }

        String likeCount = blog.getLikeCount();
        String commentCount = blog.getCommentCount();


        if (Integer.parseInt(likeCount) > 1) {
            blogLikeTextView.setText(likeCount + " Likes");
        } else {
            blogLikeTextView.setText(likeCount + " Like");
        }

        if (Integer.parseInt(commentCount) > 1) {
            blogCommentTextView.setText(commentCount + " Comments");
        } else {
            blogCommentTextView.setText(commentCount + " Comment");
        }

        blogHolder.blogId = blog.getId();
    }

    @Override
    public int getItemCount() {
        return blogArray.size();
    }

    private class BlogHolder extends RecyclerView.ViewHolder {
        private ImageView blogTitleImageView;
        private LinearLayout blogBodyLinearLayout;
        private TextView blogTitleTextView, blogDateTextView, blogLikeTextView, blogCommentTextView;

        private String blogId;

        BlogHolder(View itemView) {
            super(itemView);

            blogTitleImageView = itemView.findViewById(R.id.item_big_blog_blogTitle_ImageView);
            blogBodyLinearLayout = itemView.findViewById(R.id.item_big_blog_blogBody_LinearLayout);
            blogTitleTextView = itemView.findViewById(R.id.item_big_blog_blogTitle_TextView);
            blogDateTextView = itemView.findViewById(R.id.item_big_blog_blogDate_TextView);
            blogLikeTextView = itemView.findViewById(R.id.item_big_blog_blogLike_TextView);
            blogCommentTextView = itemView.findViewById(R.id.item_big_blog_blogComment_TextView);

            blogId = null;

            blogBodyLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowSingleBlogActivity.class);
                    intent.putExtra("blog_id", blogId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
