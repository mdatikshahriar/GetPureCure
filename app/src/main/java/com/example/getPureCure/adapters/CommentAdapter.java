package com.example.getPureCure.adapters;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getPureCure.R;
import com.example.getPureCure.assets.Time;
import com.example.getPureCure.objects.Comment;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class CommentAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private ArrayList<Comment> commentArray;

    public CommentAdapter(ArrayList<Comment> commentArray) {
        this.commentArray = commentArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentArray.get(position);

        CommentHolder commentHolder = (CommentHolder) holder;

        ImageView authorProfilePhotoCircularImageView = commentHolder.authorProfilePhotoCircularImageView;
        TextView authorNameTextView = commentHolder.authorNameTextView;
        TextView contentTextView = commentHolder.contentTextView;
        TextView timeTextView = commentHolder.timeTextView;
        TextView replyTextView = commentHolder.replyTextView;

        if(comment.getAuthorImageUri() == null || comment.getAuthorImageUri().isEmpty()){
            Picasso.get().load(R.drawable.ic_account)
                    .centerCrop()
                    .fit()
                    .error(R.drawable.ic_account)
                    .into(authorProfilePhotoCircularImageView);
        }else {
            Picasso.get().load(comment.getAuthorImageUri())
                    .centerCrop()
                    .fit()
                    .error(R.drawable.ic_account)
                    .into(authorProfilePhotoCircularImageView);
        }

        authorNameTextView.setText(comment.getAuthorName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentTextView.setText(Html.fromHtml(comment.getContent(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            contentTextView.setText(Html.fromHtml(comment.getContent()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contentTextView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeTextView.setText(Time.getTimeAgo(Instant.parse(comment.getDate()).toEpochMilli()));
        } else {
            timeTextView.setText(Time.getTimeDate(comment.getDate()));
        }

        String replyCount = comment.getReplyCount();

        if (Integer.parseInt(replyCount) > 1) {
            replyTextView.setText(replyCount + " Replies");
        } else {
            replyTextView.setText(replyCount + " Reply");
        }
    }

    @Override
    public int getItemCount() {
        return commentArray.size();
    }

    private class CommentHolder extends RecyclerView.ViewHolder {
        ImageView authorProfilePhotoCircularImageView;
        TextView authorNameTextView, contentTextView, timeTextView, replyTextView;

        public CommentHolder(View itemView) {
            super(itemView);

            authorProfilePhotoCircularImageView = itemView.findViewById(R.id.item_comment_authorProfilePhoto_CircularImageView);
            authorNameTextView = itemView.findViewById(R.id.item_comment_authorName_TextView);
            contentTextView = itemView.findViewById(R.id.item_comment_content_TextView);
            timeTextView = itemView.findViewById(R.id.item_comment_time_TextView);
            replyTextView = itemView.findViewById(R.id.item_comment_reply_TextView);

            authorNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });

            replyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
    }
}
