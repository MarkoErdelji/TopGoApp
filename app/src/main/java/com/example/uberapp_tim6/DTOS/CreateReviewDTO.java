package com.example.uberapp_tim6.DTOS;

public class CreateReviewDTO {
    Float rating;
    String comment;

    public CreateReviewDTO() {
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
