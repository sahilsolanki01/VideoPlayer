package com.solanki.sahil.test_player.ui.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.solanki.sahil.test_player.R;
import com.solanki.sahil.test_player.data.adapter.MyRecyclerViewAdapter;
import com.solanki.sahil.test_player.data.adapter.RecyclerViewClickListener;
import com.solanki.sahil.test_player.data.adapter.VerticalSpacingItemDecorator;
import com.solanki.sahil.test_player.data.model.Model_Items;
import com.solanki.sahil.test_player.data.network.Api;
import com.solanki.sahil.test_player.data.network.Network_Interceptor;
import com.solanki.sahil.test_player.data.network.RetrofitInstance;
import com.solanki.sahil.test_player.data.repository.Repository;
import com.solanki.sahil.test_player.databinding.ActivityHomescreenBinding;
import com.solanki.sahil.test_player.ui.player.PlayerActivity;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements HomeScreenListener, RecyclerViewClickListener {
    private RecyclerViewClickListener clickListener = this;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    private MyRecyclerViewAdapter mAdapter;
    private static final String TAG = "HomeScreen";
    private HomeScreenViewModel homeScreenViewModel;
    private HomeScreenListener homeScreenListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomescreenBinding bindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_homescreen);

        Network_Interceptor network_interceptor = new Network_Interceptor(this);
        Api api = new RetrofitInstance().getApi(network_interceptor);
        Repository userRepository = new Repository(api);
        HomeScreenViewModelFactory factory = new HomeScreenViewModelFactory(userRepository);

        homeScreenViewModel = ViewModelProviders.of(this, factory).get(HomeScreenViewModel.class);
        homeScreenViewModel.homeScreenListener = this;
        homeScreenViewModel.init(this);

        mRecyclerView = bindingUtil.recyclerView;
        mRecyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        mAdapter = new MyRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onSuccessListener(LiveData<ArrayList<Model_Items>> list) {
        list.observe(this, new Observer<ArrayList<Model_Items>>() {
            @Override
            public void onChanged(ArrayList<Model_Items> model_items) {
                mAdapter.setModelList(model_items, clickListener);

                Log.e(TAG, "onChanged: " + model_items.size());
            }
        });

    }

    @Override
    public void onFailureListener(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, "onFailureListener: " + message);

    }

    @Override
    public void onClick(View view, int position) {
        Log.e("HomeScreen", "onItemClick: " + position);
        Intent intent = new Intent(HomeScreen.this, PlayerActivity.class);
        intent.putExtra("url", homeScreenViewModel.getAllItems().getValue().get(position).getMedia_url());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerView != null)
            mRecyclerView = null;

        clickListener = null;
        homeScreenListener = null;
        super.onDestroy();
    }
}
