package example.videodemo.com.videodemo;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Jean on 2017/1/23.
 */

public class VideoItem implements Parcelable{

    private String videoTitle;
    private String imageUrl;
    private String videoUril;
    int category = -1;
    int favorite =0;

    public VideoItem(String title, String img, String video, int category){
        this.videoTitle = title;
        this.imageUrl = img;
        this.videoUril = video;
        this.category = category;
    }

    public VideoItem(String title, String img, String video,int category, int favorite){
        this.videoTitle = title;
        this.imageUrl = img;
        this.videoUril = video;
        this.category = category;
        this.favorite = favorite;
    }

    public String getTitle(){return  this.videoTitle;}
    public String getImgUri(){return this.imageUrl;}
    public String getVideoUri(){return this.videoUril;}
    public void setFavorite(int value){ this.favorite = value;}
    public int getFavorite(){return  this.favorite;}

    @Override
    public boolean equals(Object obj){
        if (obj instanceof VideoItem){
            if(((VideoItem) obj).getTitle().equals(this.videoTitle) &&
                    ((VideoItem) obj).getImgUri().equals(this.imageUrl) &&
                    ((VideoItem) obj).getVideoUri() == this.videoUril) return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoTitle);
        dest.writeString(imageUrl);
        dest.writeString(videoUril);
        dest.writeInt(favorite);
    }

    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel source) {
            return new VideoItem(source.readString(), source.readString(), source.readString(),
                    source.readInt());
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
}
