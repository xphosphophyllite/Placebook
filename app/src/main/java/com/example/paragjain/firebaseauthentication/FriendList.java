package com.example.paragjain.firebaseauthentication;

/**
 * Created by rahul on 11/11/17.
 */

public class FriendList {
    String listID;
    String listName;
    public String taskOne;
    public String taskTwo;
    public String taskThree;

    public FriendList(String listID, String listName)
    {
        this.listID = listID;
        this.listName = listName;
        taskOne = "";
        taskTwo = "";
        taskThree = "";
    }
}
