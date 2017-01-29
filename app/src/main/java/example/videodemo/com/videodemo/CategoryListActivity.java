package example.videodemo.com.videodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {
    List<VideoItem> videoList = new ArrayList<VideoItem>();
    Intent intent;
    int listCategory;
    List<VideoItem> changedFavoriteList = new ArrayList<VideoItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        GridView gridView = (GridView)findViewById(R.id.category_list_grideview);
        intent = getIntent();
        listCategory = intent.getIntExtra(AppContent.category, -1);
        videoList = intent.getParcelableArrayListExtra(AppContent.video_list);
        final CustomedGridAdapter adapter = new CustomedGridAdapter(CategoryListActivity.this, videoList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });


    }

    private void notifyMyFovoriteListChanged(List<VideoItem> list){
        Intent intent = new Intent(AppContent.ACTION_FAVORITE_LIST_CHANGED);
        intent.setClass(CategoryListActivity.this,MainActivity.class);
        intent.putParcelableArrayListExtra(AppContent.video_list, (ArrayList<VideoItem>) videoList);
        sendStickyBroadcast(intent);
    }
}
