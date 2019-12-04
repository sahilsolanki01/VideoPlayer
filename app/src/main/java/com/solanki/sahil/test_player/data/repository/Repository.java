package com.solanki.sahil.test_player.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.solanki.sahil.test_player.data.model.Model_Items;
import com.solanki.sahil.test_player.data.network.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    MutableLiveData<ArrayList<Model_Items>> mutableLiveData = new MutableLiveData<>();;
    ArrayList<Model_Items> arrayList = new ArrayList<>();
    private Api api;
    private static final String TAG = "Repository";

    public Repository(Api api) {
        this.api = api;
    }


    public void init(final BookmarkCallback callback) {

        Call<ResponseBody> call = api.getResult();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String url = jsonObject.getString("url");
                        String thumbnail = jsonObject.getString("thumbnail");
                        Model_Items items = new Model_Items(title, url, thumbnail);
                        arrayList.add(items);

                    }
                    //set value as called on main thread
                    mutableLiveData.setValue(arrayList);

                    callback.onSuccess(mutableLiveData);

                    Log.e(TAG, "getList: "+arrayList.size());


                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public interface BookmarkCallback{
        void onSuccess(MutableLiveData<ArrayList<Model_Items>> list);
        void onError(String error);
    }


}
