package example.videodemo.com.videodemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String LOG_TAG = "MainActivity";

    Context mContext;
    static Object[] recyclerviewlist = new Object[3];// Object put  List<VideoItem>
    LinearLayoutManager mLayoutManager_favorie, mLayoutManager_ch, mLayoutManager_en;
    RecyclerView[] mRecyclerView = new RecyclerView[recyclerviewlist.length];//{My favorite, Chinese song, English song}
    static RecyclerView.Adapter[] mAdapter = new RecyclerView.Adapter[recyclerviewlist.length];
    private FavoriteListNotifiedReceiver favoriteListNotifiedReceiver = new FavoriteListNotifiedReceiver();
    private IntentFilter filter = new IntentFilter(AppContent.ACTION_FAVORITE_LIST_CHANGED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerviewlist[0] = new ArrayList<VideoItem>(); // My favorite
        recyclerviewlist[1] = new ArrayList<VideoItem>(); //Chinese song
        recyclerviewlist[2] = new ArrayList<VideoItem>(); //English song
        loadData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(favoriteListNotifiedReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(favoriteListNotifiedReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.my_favorite_list:
                intent = new Intent(mContext, CategoryListActivity.class);
                intent.setAction(AppContent.ACTION_ACTIVATE_CATEGORY_LIST);
                intent.putExtra(AppContent.category, AppContent.CATEGORY_MY_FAVORITE);
                intent.putParcelableArrayListExtra(AppContent.video_list,
                        (ArrayList<VideoItem>)recyclerviewlist[AppContent.CATEGORY_MY_FAVORITE]);
                startActivity(intent);
                break;
            case R.id.english_song_list:
                intent = new Intent(mContext, CategoryListActivity.class);
                intent.setAction(AppContent.ACTION_ACTIVATE_CATEGORY_LIST);
                intent.putExtra(AppContent.category, AppContent.CATEGORY_ENGLISH_SONG);
                intent.putParcelableArrayListExtra(AppContent.video_list,
                        (ArrayList<VideoItem>)recyclerviewlist[AppContent.CATEGORY_ENGLISH_SONG]);
                startActivity(intent);
                break;
            case R.id.chinese_song_list:
                intent = new Intent(mContext, CategoryListActivity.class);
                intent.setAction(AppContent.ACTION_ACTIVATE_CATEGORY_LIST);
                intent.putExtra(AppContent.category, AppContent.CATEGORY_CHINESE_SONG);
                intent.putParcelableArrayListExtra(AppContent.video_list,
                        (ArrayList<VideoItem>)recyclerviewlist[AppContent.CATEGORY_CHINESE_SONG]);
                startActivity(intent);
                break;
            case R.id.korean_song_list:
                break;
            case R.id.nav_share:
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData(){
        VideoItem item;
        Object[][] data = new Object[recyclerviewlist.length][3];
        String[] titleArray, imgArray, videoArray;
        for(int i=0; i<recyclerviewlist.length; i++) {
            switch(i) {
                case AppContent.CATEGORY_MY_FAVORITE:
                    data[i][0] = getResources().getStringArray(R.array.video_title_list_favorite);
                    data[i][1] = getResources().getStringArray(R.array.image_url_list_favorite);
                    data[i][2] = getResources().getStringArray(R.array.video_url_list_favorite);
                    break;
                case AppContent.CATEGORY_CHINESE_SONG:
                    data[i][0] = getResources().getStringArray(R.array.video_title_list_ch);
                    data[i][1] = getResources().getStringArray(R.array.image_url_list_ch);
                    data[i][2] = getResources().getStringArray(R.array.video_url_list_ch);
                    break;
                case AppContent.CATEGORY_ENGLISH_SONG:
                    data[i][0] = getResources().getStringArray(R.array.video_title_list_en);
                    data[i][1] = getResources().getStringArray(R.array.image_url_list_en);
                    data[i][2] = getResources().getStringArray(R.array.video_url_list_en);
                    break;
                default:
                    data = null;
                    break;
            }
            if(data[i][0]==null ||data[i][1]==null || data[i][2]==null ) continue;
            else if(((String[])data[i][0]).length != ((String[])data[i][1]).length ||
                    ((String[])data[i][0]).length != ((String[])data[i][2]).length) continue;

            for (int j = 0; j < ((String[])data[i][0]).length; j++) {
                item = new VideoItem(((String[])data[i][0])[j], ((String[])data[i][1])[j], ((String[])data[i][2])[j],i);
                ((List<VideoItem>)recyclerviewlist[i]).add(item);
            }
        }
    }

    private void initRecyclerView() {
        for(int i=0; i<recyclerviewlist.length; i++){
            switch(i) {
                case AppContent.CATEGORY_MY_FAVORITE:
                    mLayoutManager_favorie = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView[i] = (RecyclerView) findViewById(R.id.myFavorite);
                    mRecyclerView[i].setLayoutManager(mLayoutManager_favorie);
                    break;
                case AppContent.CATEGORY_CHINESE_SONG:
                    mLayoutManager_ch = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView[i] = (RecyclerView) findViewById(R.id.chineseSong);
                    mRecyclerView[i].setLayoutManager(mLayoutManager_ch);
                    break;
                case AppContent.CATEGORY_ENGLISH_SONG:
                    mLayoutManager_en = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    mRecyclerView[i] = (RecyclerView) findViewById(R.id.englishSong);
                    mRecyclerView[i].setLayoutManager(mLayoutManager_en);
                    break;
                default:
                    mRecyclerView[i] = null;
                    break;
            }
            if(mRecyclerView[i]==null) continue;
            mRecyclerView[i].setHasFixedSize(true);
            mAdapter[i] = new MyRecyclerViewAdapter(mContext, (List<VideoItem>) recyclerviewlist[i]);
            mRecyclerView[i].setAdapter(mAdapter[i]);
        }
    }

    public static class FavoriteListNotifiedReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "FavListNotiReceiver";
        public FavoriteListNotifiedReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG, "onReceive.");
            if (intent.getAction() == AppContent.ACTION_FAVORITE_LIST_CHANGED) {
                List<VideoItem> newlist = new ArrayList<VideoItem>();
                newlist = intent.getParcelableArrayListExtra(AppContent.video_list);
                if(newlist !=null && newlist.size()>0){
                    if(((List<VideoItem>)recyclerviewlist[0]).size() == 0) ((List<VideoItem>)recyclerviewlist[0]).addAll(newlist);
                    Iterator it = newlist.iterator();
                    while(it.hasNext()){
                        VideoItem element = (VideoItem) it.next();
                        if(!((List<VideoItem>)recyclerviewlist[0]).contains(element)){
                            if(element.getFavorite()==1)
                                ((List<VideoItem>)recyclerviewlist[0]).add(element);
                        } else{
                            if(element.getFavorite()==0) ((List<VideoItem>)recyclerviewlist[0]).remove(element);
                        }
                    }
                    //TODO: [Jean] there's fail to load new video to myFavorite list.
                    mAdapter[AppContent.CATEGORY_MY_FAVORITE].notifyDataSetChanged();
                }
            }
        }
    }
}
