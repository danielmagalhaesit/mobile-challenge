package com.android.daniel.mobile_challenge.utils;

import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.PxApi;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.auth.OAuthAuthorization;
import com.fivehundredpx.api.auth.XAuthProvider;

import org.json.JSONObject;


public class Tasks500px {

    private static AccessToken mAccessToken;

    // Method to get the Access Token
    public void generateAccessToken() {

        if(mAccessToken == null){
            try {
                final OAuthAuthorization oauth = new OAuthAuthorization.Builder()
                        .consumerKey(Constants.CONSUMER_KEY)
                        .consumerSecret(Constants.CONSUMER_SECRET)
                        .build();
                mAccessToken = oauth.getAccessToken(
                        new XAuthProvider(Constants.USERNAME, Constants.PASSWORD));

            } catch (FiveHundredException e) {
                e.printStackTrace();
            }
        }
    }
    // Method to get the JSON object fro 500px
    public JSONObject getJSONfrom500px(String url){

        if(mAccessToken != null){
            try{
              /*params.add(new BasicNameValuePair("feature", "popular"));
                params.add(new BasicNameValuePair("exclude", "Nude"));
                params.add(new BasicNameValuePair("sorting", "rating"));
                params.add(new BasicNameValuePair("rpp", "20"));
                params.add(new BasicNameValuePair("include_store", "include_download"));
                params.add(new BasicNameValuePair("include_states", "voted"));*/

                PxApi pxApi = new PxApi(mAccessToken, Constants.CONSUMER_KEY
                , Constants.CONSUMER_SECRET);

                JSONObject jsonObject = pxApi.get(url);
                return jsonObject;

            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

    // It must and will be refactored
    public String getURL(int page){

        return "/photos?feature=popular&exclude=Nude&sort=rating&page="
                + page + "&rpp="
                + JSONToImage.itemsPerPage
                + "&image_size=4&include_store=store_download&include_states=voted";
    }

}
