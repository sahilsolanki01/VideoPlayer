package com.solanki.sahil.test_player.ui.list;

import androidx.lifecycle.LiveData;

import com.solanki.sahil.test_player.data.model.Model_Items;

import java.util.ArrayList;

public interface HomeScreenListener {
    void onSuccessListener(LiveData<ArrayList<Model_Items>> response);
    void onFailureListener(String message);
}
