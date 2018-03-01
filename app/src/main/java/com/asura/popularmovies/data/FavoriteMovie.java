package com.asura.popularmovies.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteMovie extends Movie implements Parcelable{
    private Bitmap imagePath;

    public FavoriteMovie(){

    }

    protected FavoriteMovie(Parcel in) {
        super(in);
        imagePath = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(imagePath, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavoriteMovie> CREATOR = new Creator<FavoriteMovie>() {
        @Override
        public FavoriteMovie createFromParcel(Parcel in) {
            return new FavoriteMovie(in);
        }

        @Override
        public FavoriteMovie[] newArray(int size) {
            return new FavoriteMovie[size];
        }
    };

    public Bitmap getImagePath() {
        return imagePath;
    }

    public void setImagePath(Bitmap imagePath) {
        this.imagePath = imagePath;
    }
}
