package com.example.lastgithubsearch;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// TODO(11) - Create a Factory Class for the ViewModel 
public class ResultsViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    /**
     * the sole purpose of this class is to be able to add a network repository
     * instance to the ResultsViewModel constructor as the default method only uses a zero argument constructor
     */

    private final NetworkRepository repository;

    ResultsViewModelFactory(NetworkRepository repo){
        repository = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResultsViewModel(repository);
    }
}
