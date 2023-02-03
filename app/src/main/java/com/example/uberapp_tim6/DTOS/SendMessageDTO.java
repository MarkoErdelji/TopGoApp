package com.example.uberapp_tim6.DTOS;

public class SendMessageDTO {

        private String message;

        private MessageType type;

        private Integer rideId;

        public SendMessageDTO() {
        }

        public String getMessage() {
                return message;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        public MessageType getType() {
                return type;
        }

        public void setType(MessageType type) {
                this.type = type;
        }

        public Integer getRideId() {
                return rideId;
        }

        public void setRideId(Integer rideId) {
                this.rideId = rideId;
        }
}
