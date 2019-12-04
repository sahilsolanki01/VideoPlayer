package com.solanki.sahil.test_player.ui.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.solanki.sahil.test_player.data.repository.Repository;

public class HomeScreenViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Repository repository;


    public HomeScreenViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeScreenViewModel(repository);
    }
}
