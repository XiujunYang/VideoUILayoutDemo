package example.videodemo.com.videodemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jean on 2017/1/29.
 */

public class CustomedGridAdapter extends BaseAdapter{
    private static final String LOG_TAG = "CustomedGridAdapter";

    private Context mContext;
    List<VideoItem> mVideoList;
    ViewHolder holder = null;
    LayoutInflater myInflater;


    public CustomedGridAdapter(Context context, List<VideoItem> videoList) {
        this.mContext = context;
        this.myInflater = LayoutInflater.from(mContext);
        this.mVideoList = videoList;
    }

    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < mVideoList.size()) return mVideoList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mVideoList.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.video_componet, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (CustomedGridAdapter.ViewHolder) convertView.getTag();
        }
        VideoItem video = (VideoItem) getItem(position);
        if (video == null) return convertView;
        holder.videoTitle.setText(video.getTitle());
        if(MyRecyclerViewAdapter.isNetworkAvailable()) {
            holder.imageView.setImageBitmap(MyRecyclerViewAdapter.getBitmapFromURL(
                    mVideoList.get(position).getImgUri()));
        }else {
            Toast.makeText(mContext, "There's no network access.", Toast.LENGTH_SHORT).show();
            holder.imageView.setImageResource(R.drawable.default_images);
        }
		
        if(mVideoList.get(position).getFavorite()==1)
                holder.imageButton.setImageResource(R.drawable.full_star);
        else holder.imageButton.setImageResource(R.drawable.empty_star);
        holder.imageButton.setBackgroundColor(Color.TRANSPARENT);

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVideoList.get(position).getFavorite()==1){
                    mVideoList.get(position).setFavorite(0);
                }else{
                    mVideoList.get(position).setFavorite(1);
                }
                notifyDataSetChanged();
                Intent intent = new Intent(AppContent.ACTION_FAVORITE_LIST_CHANGED);
                intent.putParcelableArrayListExtra(AppContent.video_list, (ArrayList<VideoItem>) mVideoList);
                mContext.sendBroadcast(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder{
        TextView videoTitle;
        ImageView imageView;
        ImageButton imageButton;

        public ViewHolder(View view) {
            videoTitle = (TextView) view.findViewById(R.id.videotitle);
            imageView = (ImageView) view.findViewById(R.id.imageviewforvideo);
            imageButton = (ImageButton) view.findViewById(R.id.favorite);
        }
    }
}
