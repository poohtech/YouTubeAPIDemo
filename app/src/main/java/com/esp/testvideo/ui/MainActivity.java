package com.esp.testvideo.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.esp.testvideo.R;
import com.esp.testvideo.Utils.UtilsFunc;
import com.esp.testvideo.adapter.VideoListAdapter;
import com.esp.testvideo.backend.ChannelApi;
import com.esp.testvideo.backend.MoreVideoApi;
import com.esp.testvideo.backend.ResponseListener;
import com.esp.testvideo.bean.ChannelBean;

import java.util.ArrayList;

/**
 * Created by admin on 6/2/16.
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private ChannelApi channelApi;
    private ArrayList<ChannelBean> channelBeansList;
    private ListView listView;
    private VideoListAdapter videoListAdap;
    Button btnLoadMore;
    String pageNumber, tokenPage;
    MoreVideoApi moreVideoApi;
    ProgressDialog progressDialog;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);

        channelBeansList = new ArrayList<ChannelBean>();
        listView = (ListView) findViewById(R.id.list_item);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        if (UtilsFunc.isOnline(MainActivity.this)) {
            if (progressDialog == null) {
                progressDialog = UtilsFunc.createProgressDialog(MainActivity.this);
                progressDialog.show();
            } else {
                progressDialog.show();
            }
            try {
                channelApi = new ChannelApi(MainActivity.this.getApplication(), responseListener);
                channelApi.execute();
            } catch (Exception e) {
                System.out.println("Exception of api" + e.toString());
            }
        }
        btnLoadMore = new Button(MainActivity.this);
        btnLoadMore.setText("Load More");


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastitme = firstVisibleItem + visibleItemCount;
                if (lastitme == totalItemCount) {
                    if (UtilsFunc.isOnline(MainActivity.this.getApplication())) {
                        //UCyC4NHU8GeQX2nEEKcOgBTA
                        String url = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyDqc4on_Q3XKR2TDuJSQN0SxDd3YDjC9Ko&channelId=UCRGlHHyMQC5fMZ9lDjKampQ&pageToken=" + tokenPage + "&part=snippet,id&order=date&maxResults=10";
                        moreVideoApi = new MoreVideoApi(MainActivity.this.getApplication(), responseListener, url);
                        moreVideoApi.execute();
                    }

                }
            }
        });
    }


    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponce(String tag, int result, Object obj) {

        }

        @Override
        public void onResponce(String tag, int result, Object obj, Object obj2) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
            ArrayList<ChannelBean> temp = new ArrayList<ChannelBean>();
            if (tag == "TAG_ChannelId") {
                temp = (ArrayList) obj;
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                String pageToken = (String) obj2;
                String[] token = pageToken.split(":");
                pageNumber = token[1];
                tokenPage = token[0];
                for (int j = 0; j < temp.size(); j++) {
                    channelBeansList.add(temp.get(j));
                }
                setList(channelBeansList);
            } else if (tag == "TAG_MoreVideos") {
                temp = (ArrayList) obj;
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                String pageToken = (String) obj2;
                String[] token = pageToken.split(":");
                pageNumber = token[1];
                tokenPage = token[0];
                for (int j = 0; j < temp.size(); j++) {
                    channelBeansList.add(temp.get(j));
                }
                setList(channelBeansList);
            }
        }

        @Override
        public void onResponce(String tag, int result, Object obj, Object obj2, Object obj3) {

        }
    };

    public void setList(ArrayList<ChannelBean> channelBeansList) {
        System.out.println("========= NEW LIST SIZE =========" + channelBeansList.size());
        videoListAdap = new VideoListAdapter(MainActivity.this, R.layout.row_videolist, channelBeansList);
        listView.setAdapter(videoListAdap);
        listView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Videos Ids in main" + channelBeansList.get(position).videoId);
        Intent intent = new Intent(MainActivity.this, PlayingVideo.class);
        intent.putExtra("videoId", channelBeansList.get(position).videoId);
        startActivity(intent);
    }


}
