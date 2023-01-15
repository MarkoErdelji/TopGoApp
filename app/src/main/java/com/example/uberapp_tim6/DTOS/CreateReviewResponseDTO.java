package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.models.Review;

import java.util.ArrayList;
import java.util.List;

public class CreateReviewResponseDTO {
    int id;
    float rating;
    String comment;
    UserRef passenger;

    public CreateReviewResponseDTO() {
    }

    public CreateReviewResponseDTO(Review r){
        this.id = r.getId();
        this.rating = r.getRating();
        this.passenger = new UserRef(r.getPassenger());
        this.comment = r.getComment();
    }

    public static List<CreateReviewResponseDTO> convertToCreateReviewResponseDTO(List<Review> content) {
        List<CreateReviewResponseDTO> createReviewResponseDTOS = new ArrayList<CreateReviewResponseDTO>();
        for(Review r:content){
            createReviewResponseDTOS.add(new CreateReviewResponseDTO(r));
        }
        return createReviewResponseDTOS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserRef getPassenger() {
        return passenger;
    }

    public void setPassenger(UserRef passenger) {
        this.passenger = passenger;
    }
}
