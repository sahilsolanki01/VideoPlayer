package com.solanki.sahil.test_player.ui.list;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.solanki.sahil.test_player.data.model.Model_Items;
import com.solanki.sahil.test_player.data.repository.Repository;

import java.util.ArrayList;


public class HomeScreenViewModel extends ViewModel implements Repository.BookmarkCallback {
    private MutableLiveData<ArrayList<Model_Items>> mList;
    private Repository userRepository;
    public HomeScreenListener homeScreenListener;
    private Context context;
    private Repository.BookmarkCallback bookmarkCallback;


    public HomeScreenViewModel(Repository userRepository) {
        this.userRepository = userRepository;
    }


    public void init(Context context) {
        this.context = context;
        bookmarkCallback = this;

        if (mList != null)
            return;

        userRepository.init(bookmarkCallback);
    }

    public LiveData<ArrayList<Model_Items>> getAllItems() {
        return mList;
    }

    @Override
    public void onSuccess(MutableLiveData<ArrayList<Model_Items>> list) {
        mList = list;
        homeScreenListener.onSuccessListener(mList);

    }

    @Override
    public void onError(String error) {
        homeScreenListener.onFailureListener(error);
    }
}
