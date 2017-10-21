package com.tddp2.grupo2.linkup.infrastructure.client.request;


import java.io.Serializable;

public class UpgradeAccountRequest implements Serializable {
    private User user;
    public UpgradeAccountRequest(String fbid) {
        this.user = new User(fbid);
    }

    public User getUser() {
        return user;
    }

    public class User {
        private String fbid;
        private Control control = new Control();

        public User(String fbid) {
            this.fbid = fbid;
            this.control = new Control();
        }

        public String getFbid() {
            return fbid;
        }

        public Control getControl() {
            return control;
        }
    }

    public class Control {
        boolean isPremium;

        public Control() {
            this.isPremium = true;
        }

        public boolean getIsPremium() {
            return isPremium;
        }
    }
}
