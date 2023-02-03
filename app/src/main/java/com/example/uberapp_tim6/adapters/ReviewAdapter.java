package com.example.uberapp_tim6.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.DriverReviewListDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.Review;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.services.ServiceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends BaseAdapter {
    private Activity activity;
    private List<CreateReviewResponseDTO> reviews;


    public ReviewAdapter(Activity activity, List<CreateReviewResponseDTO> reviews) {
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
        final View vi= activity.getLayoutInflater().inflate(R.layout.review_item_list, null);;
        CreateReviewResponseDTO review = this.reviews.get(position);

        Call<UserInfoDTO> call2 = ServiceUtils.userService.getUserById(Integer.toString(review.getPassenger().getId()));
        call2.enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call2, Response<UserInfoDTO> response) {
                if (response.body() != null) {
                    TextView name = (TextView) vi.findViewById(R.id.namePlaceholder);
                    TextView comment = (TextView) vi.findViewById(R.id.comment);
                    RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.simpleRatingBar);

                    String nameText = response.body().getName() + response.body().getSurname();
                    name.setText(nameText);
                    ImageView image = vi.findViewById(R.id.profilePicture);
                    Glide.with(activity.getApplicationContext()).load(response.body().getProfilePicture()).into(image);

                    comment.setText(review.getComment());
                    ratingBar.setRating(review.getRating());
                }

            }

            @Override
            public void onFailure(Call<UserInfoDTO> call2, Throwable t) {
            }

        });

        return vi;
    }




}

