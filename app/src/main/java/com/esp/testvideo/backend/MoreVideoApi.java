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
 * Created by admin on 9/2/16.
 */
public class MoreVideoApi {
    private Context context;
    private HashMap<String, String> mParams;
    private Adapter mAdapter = null;
    private ResponseListener responseListener;
    private String API_SearchAddress;
    private String tokentotalpage;

    public MoreVideoApi(Context contex, ResponseListener responseListener, String MoreVideo) {
        this.context = contex;
        this.mParams = new HashMap<String, String>();

        this.API_SearchAddress = MoreVideo;

        System.out.println("----------MoreVideoApi-------URL-----" + this.API_SearchAddress);

        Log.print(":::: SearchAddres ::::" + mParams);

        this.responseListener = responseListener;
    }

    public void execute() {
        this.mAdapter = new Adapter(this.context);
        this.mAdapter.doGet("TAG_MoreVideos", API_SearchAddress, mParams,
                new APIResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        super.onResponse(response);
                        mParams = null;
                        System.out.println("Response of youtube-------" + response);
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
            System.out.println("Total Pages" + totalpage);
            String tokenPage = jsonObject.getString("nextPageToken");

            tokentotalpage = tokenPage + ":" + totalpage;
            // pageToken = jsonObject.getString("nextPageToken");
            System.out.println("Token in More Vedio Api" + tokentotalpage);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            channelBeanArrayList = new ArrayList<ChannelBean>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ChannelBean channelBean = new ChannelBean();
                JSONObject jsonObject2 = jsonObject1.getJSONObject("id");
                if (jsonObject2.has("videoId"))
                    channelBean.videoId = jsonObject2.getString("videoId");
                System.out.println("channelBean.videoId" + channelBean.videoId);
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
                responseListener.onResponce("TAG_MoreVideos", Config.API_SUCCESS, channelBeanArrayList, tokentotalpage);


            } else if (code > 0) {

                responseListener.onResponce("TAG_MoreVideos", Config.API_FAIL, null);

            } else if (code < 0) {
                responseListener.onResponce("TAG_MoreVideos", Config.API_FAIL, null);

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
            mAdapter.doCancel("TAG_MoreVideos");
        }
    }
}
