package com.esp.testvideo.backend;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.esp.testvideo.Utils.Config;
import com.esp.testvideo.Utils.Log;
import com.esp.testvideo.bean.ChannelBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 3/2/16.
 */
public class ChannelApi {
    private Context context;
    private HashMap<String, String> mParams;
    private Adapter mAdapter = null;
    private ResponseListener responseListener;
    private String API_SearchAddress;
    private String tokentotalpage;
    String tokenPage;

    public ChannelApi(Context contex, ResponseListener responseListener) {
        this.context = contex;
        this.mParams = new HashMap<String, String>();

        //UCyC4NHU8GeQX2nEEKcOgBTA
        this.API_SearchAddress = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyDqc4on_Q3XKR2TDuJSQN0SxDd3YDjC9Ko&channelId=UCRGlHHyMQC5fMZ9lDjKampQ&part=snippet,id&order=date&maxResults=10";

        System.out.println("----------ChannelApi----URL--------" + this.API_SearchAddress);

        Log.print(":::: SearchAddres ::::" + mParams);

        this.responseListener = responseListener;
    }

    public void execute() {
        this.mAdapter = new Adapter(this.context);
        this.mAdapter.doGet("TAG_ChannelId", API_SearchAddress, mParams,
                new APIResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        mParams = null;
                        System.out.println("----------Response of youtube-------" + response);
                        parse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private void parse(String response) {
        int code = 0;
        String mesg = "";
        ArrayList<ChannelBean> channelBeanArrayList = null;

        try {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(response);
            JSONObject totalPgObj = jsonObject.getJSONObject("pageInfo");
            String totalpage = totalPgObj.getString("resultsPerPage");

            if (jsonObject.has("nextPageToken")) {
                tokenPage = jsonObject.getString("nextPageToken");
            }

            tokentotalpage = tokenPage + ":" + totalpage;
            channelBeanArrayList = new ArrayList<ChannelBean>();
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ChannelBean channelBean = new ChannelBean();
                JSONObject jsonObject2 = jsonObject1.getJSONObject("id");
                if (jsonObject2.has("videoId"))
                    channelBean.videoId = jsonObject2.getString("videoId");
                JSONObject jsonObject3 = jsonObject1.getJSONObject("snippet");
                channelBean.titleofVideo = jsonObject3.getString("title");
                channelBean.publishedDate = new SimpleDateFormat("dd-MMM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'").parse(jsonObject3.getString("publishedAt"))).toString();
                JSONObject jsonObject4 = jsonObject3.getJSONObject("thumbnails");
                JSONObject jsonObject5 = jsonObject4.getJSONObject("default");
                channelBean.pictureUrl = jsonObject5.getString("url");
                channelBeanArrayList.add(channelBean);
            }
        } catch (Exception e) {
            code = -1;
            mesg = "Exception :: " + this.getClass() + " :: parse() :: "
                    + e.getLocalizedMessage();
            Log.error(this.getClass() + " :: Exception :: ", e);
            Log.print(this.getClass() + " :: Exception :: ", e);
            System.out.println("Exception of parsing" + e.toString());
        }
        doCallBack(code, mesg, channelBeanArrayList, tokentotalpage);
        /** release variables */
        response = null;
    }

    private void doCallBack(int code, String mesg, ArrayList<ChannelBean> channelBeanArrayList, String pages) {
        try {
            System.out.println("Codeeeee" + code);
            if (code == 0) {
                responseListener.onResponce("TAG_ChannelId", Config.API_SUCCESS, channelBeanArrayList, tokentotalpage);
            } else if (code > 0) {
                responseListener.onResponce("TAG_ChannelId", Config.API_FAIL, null);
            } else if (code < 0) {
                responseListener.onResponce("TAG_ChannelId", Config.API_FAIL, null);
            }
        } catch (Exception e) {
            Log.error(this.getClass() + " :: Exception :: ", e);
            Log.print(this.getClass() + " :: Exception :: ", e);
        }
    }

    /*
     * Cancel API Request
     */
    public void doCancel() {
        if (mAdapter != null) {
            mAdapter.doCancel("TAG_ChannelId");
        }
    }
}
