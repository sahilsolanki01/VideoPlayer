package com.solanki.sahil.test_player.data.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("test/api/")
    Call<ResponseBody> getResult();


}
