package com.example.uberapp_tim6.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.Review;
import com.example.uberapp_tim6.models.Ride;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    private Activity activity;
    private List<Review> reviews;

    public ReviewAdapter(Activity activity, List<Review> reviews) {
        this.activity = activity;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return this.reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return this.reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi=view;
        Review review = this.reviews.get(position);
        if(view==null)
            vi = activity.getLayoutInflater().inflate(R.layout.review_item_list, null);

        TextView name = (TextView)vi.findViewById(R.id.namePlaceholder);
        TextView comment = (TextView)vi.findViewById(R.id.comment);
        RatingBar ratingBar = (RatingBar)vi.findViewById(R.id.simpleRatingBar);

        String nameText = review.getPassenger().getFirstName() + review.getPassenger().getLastName();
        name.setText(nameText);

        comment.setText(review.getComment());

        ratingBar.setRating(review.getRating());

        return vi;
    }
}
