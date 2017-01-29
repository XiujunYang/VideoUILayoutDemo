package example.videodemo.com.videodemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

/**
 * Created by Jean on 2017/1/23.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements View.OnLongClickListener {

    private static final String LOG_TAG = "MyAdapter";

    private List<VideoItem> list;
    private static Context mContext;

    public MyRecyclerViewAdapter(Context context, List<VideoItem> myDataset) {
        list = myDataset;
        mContext = context;
        // Avoid performing network operations on the main thread.
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_componet, parent, false);

        ViewHolder holder = new ViewHolder(v);
        holder.videoTitle.setOnLongClickListener(MyRecyclerViewAdapter.this);
        holder.videoTitle.setTag(holder);
        holder.videoImg.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position;
        holder.videoTitle.setText(list.get(position).getTitle());
        if(isNetworkAvailable()) {
            holder.videoImg.setImageBitmap(getBitmapFromURL(list.get(position).getImgUri()));
        }else {
            Toast.makeText(mContext, "There's no network access.", Toast.LENGTH_SHORT).show();
            holder.videoImg.setImageResource(R.drawable.default_images);
        }
        holder.videoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,VideoPlayedActivity.class);
                intent.putExtra(AppContent.string_video_url,
                        list.get(index).getVideoUri());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        if(list.get(position).getFavorite()==1)
            holder.imageBtn.setImageResource(R.drawable.full_star);
        else holder.imageBtn.setImageResource(R.drawable.empty_star);
        holder.imageBtn.setBackgroundColor(Color.TRANSPARENT);

        holder.imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.get(index).getFavorite()==1){
                    list.get(index).setFavorite(0);
                }else{
                    list.get(index).setFavorite(1);
                }
                notifyDataSetChanged();
                Intent intent = new Intent(AppContent.ACTION_FAVORITE_LIST_CHANGED);
                intent.putParcelableArrayListExtra(AppContent.video_list, (ArrayList<VideoItem>) list);
                mContext.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public boolean onLongClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (view.getId() == holder.videoTitle.getId()) {
            list.remove(holder.getPosition());
            notifyDataSetChanged();

            Toast.makeText(mContext, "Item " + holder.videoTitle.getText() + " has been removed from list",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView videoTitle;
        public ImageView videoImg;
        public ImageButton imageBtn;

        public ViewHolder(View v) {
            super(v);
            videoTitle = (TextView) v.findViewById(R.id.videotitle);
            videoImg = (ImageView) v.findViewById(R.id.imageviewforvideo);
            imageBtn = (ImageButton) v.findViewById(R.id.favorite);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.getMessage());
            return null;
        }
    }

    public static boolean isNetworkAvailable() {
        if(mContext == null) return false;
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
