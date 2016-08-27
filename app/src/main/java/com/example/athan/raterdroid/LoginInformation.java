package com.example.athan.raterdroid;

/**
 * Created by athan on 27.8.16.
 */
public class LoginInformation {
    private String _id;
    private String _skey;

    public LoginInformation(){

    }

    public LoginInformation(String _id, String _skey){
        this._id = _id;
        this._skey = _skey;
    }

    public String get_id(){
        return _id;
    }

    public String get_skey(){
        return _skey;
    }

}
