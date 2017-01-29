package example.videodemo.com.videodemo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class VideoPlayedActivity extends AppCompatActivity {
    private String LOG_TAG = "VideoPlayedActivity";

    private Intent intent;
    private Uri videoUri;
    private MediaController mediaController;
    private VideoView videoView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    Uri uriYouTube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_played);
        intent = getIntent();
        videoView=(VideoView)findViewById(R.id.videoContainer);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        String path = intent.getStringExtra(AppContent.string_video_url);
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    void startPlaying(String url) {
        uriYouTube = Uri.parse(url);
        videoView.setVideoURI(uriYouTube);
        videoView.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Loc", videoView.getCurrentPosition());
        outState.putString("url", uriYouTube.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
