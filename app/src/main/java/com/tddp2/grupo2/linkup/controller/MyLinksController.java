package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.MyLinksFragment;
import com.tddp2.grupo2.linkup.model.MyLink;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agustin on 16/09/2017.
 */

public class MyLinksController {
    public MyLinksController(MyLinksFragment myLinksFragment) {
    }

    public List<MyLink> getMyLinks() {
        List<MyLink> myLinks = new ArrayList<>();
        myLinks.add(new MyLink("Emma Wilson", "23 years old", R.drawable.user));
        myLinks.add(new MyLink("Lavery Maiss", "25 years old", R.drawable.user));
        myLinks.add(new MyLink("Lillie Watts", "35 years old", R.drawable.user));
        myLinks.add(new MyLink("Emma Wilson", "23 years old", R.drawable.user));
        myLinks.add(new MyLink("Lavery Maiss", "25 years old", R.drawable.user));
        myLinks.add(new MyLink("Lillie Watts", "35 years old", R.drawable.user));
        myLinks.add(new MyLink("Emma Wilson", "23 years old", R.drawable.user));
        myLinks.add(new MyLink("Lavery Maiss", "25 years old", R.drawable.user));
        myLinks.add(new MyLink("Lillie Watts", "35 years old", R.drawable.user));
        myLinks.add(new MyLink("Emma Wilson", "23 years old", R.drawable.user));
        myLinks.add(new MyLink("Lavery Maiss", "25 years old", R.drawable.user));
        myLinks.add(new MyLink("Lillie Watts", "35 years old", R.drawable.user));
        myLinks.add(new MyLink("Emma Wilson", "23 years old", R.drawable.user));
        myLinks.add(new MyLink("Lavery Maiss", "25 years old", R.drawable.user));
        myLinks.add(new MyLink("Lillie Watts", "35 years old", R.drawable.user));
        return myLinks;
    }

}
