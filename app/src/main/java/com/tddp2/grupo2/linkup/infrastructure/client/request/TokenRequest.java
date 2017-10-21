package com.tddp2.grupo2.linkup.infrastructure.client.request;

import java.io.Serializable;

public class TokenRequest implements Serializable {
    private TokenRequest.User user;

    public TokenRequest(String fbid, String token) {
        this.user = new TokenRequest.User(fbid, token);
    }

    public TokenRequest.User getUser() {
        return user;
    }

    public class User {
        private String fbid;
        private TokenRequest.Control control;

        public User(String fbid, String token) {
            this.fbid = fbid;
            this.control = new TokenRequest.Control(token);
        }

        public String getFbid() {
            return fbid;
        }

        public TokenRequest.Control getControl() {
            return control;
        }
    }

    public class Control {
        String token;

        public Control(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

}
