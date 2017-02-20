package com.android.daniel.mobile_challenge.utils;


import com.android.daniel.mobile_challenge.models.Image500px;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONToImage {

    // It must and will be refactored
    public static int totalPages;
    public static int totalItems;
    public static int currentPage;
    public static final int itemsPerPage = 20;

    // Method to convert JSON in image500px
    public List<Image500px> parseJSON() {

        List<Image500px> images500px = new ArrayList<>();

        try {
            JSONObject jsonObject = new Tasks500px().getJSONfrom500px();
            JSONArray jsonArray = jsonObject.getJSONArray("photos");

            totalPages = Integer.parseInt(jsonObject.getString("total_pages"));
            totalItems = Integer.parseInt(jsonObject.getString("total_items"));
            currentPage = Integer.parseInt(jsonObject.getString("current_page"));

            for (int i = 0; i < jsonArray.length(); i++) {
                Image500px image = new Image500px();
                JSONObject obj = jsonArray.getJSONObject(i);
                image.setmImageURL(obj.getString("image_url"));
                image.setmDescription(obj.getString("description"));
                images500px.add(image);
            }
            return images500px;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
