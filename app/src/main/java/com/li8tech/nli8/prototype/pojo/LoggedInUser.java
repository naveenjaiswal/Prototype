package com.li8tech.nli8.prototype.pojo;

/**
 * Created by FDUSER on 2/27/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoggedInUser implements Parcelable {
    public String FullName;
    public String Email;
    public String PhotoURL;

    protected LoggedInUser(Parcel in) {
        FullName = in.readString();
        Email = in.readString();
        PhotoURL = in.readString();
    }
public  LoggedInUser()
{

}
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(FullName);
        dest.writeString(Email);
        dest.writeString(PhotoURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoggedInUser> CREATOR = new Parcelable.Creator<LoggedInUser>() {
        @Override
        public LoggedInUser createFromParcel(Parcel in) {
            return new LoggedInUser(in);
        }

        @Override
        public LoggedInUser[] newArray(int size) {
            return new LoggedInUser[size];
        }
    };
}