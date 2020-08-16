package com.example.lastgithubsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;




public class ResultsActivity extends AppCompatActivity {



    //widgets
    TextView resultsTextView;
    ProgressBar progressBar;
    String searchQuery;
    String searchResult;

    //instance of ViewModel
    ResultsViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //enable navigation to the parent by <- arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // TODO(12) - Make an instance of ViewModel in the activity (make sure to add the latest dependencies in the build.gradle file)
        //instance of repository
        NetworkRepository repository = NetworkRepository.getInstance(MyApp.executorService);
        //instance of ResultsViewModelFactory
        ResultsViewModelFactory factory = new ResultsViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ResultsViewModel.class);

        //init views
        resultsTextView = findViewById(R.id.tv_result);
        progressBar = findViewById(R.id.progressBar);

        //intent that started this activity
        //the intent could be null if the activity is started from a savedInstance state
        Intent intent = getIntent();

        //get the search query from the intent
        String data = null;


        if(intent != null) {
            //check if there is a search query attached
            if (intent.hasExtra("data")) {
                data = (String) intent.getCharSequenceExtra("data");
            }
            if(data != null){
                searchQuery = data;
                progressBar.setVisibility(View.VISIBLE);
                // TODO(13) - Observe the LiveData Object from the ViewModel
                viewModel.getResultString().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        //this method is called whenever the data is to be updated
                        searchResult = s;
                        onResultReturned(searchResult);
                    }
                });
                // TODO(14) - Make the Network Request
                viewModel.getSearchResult(searchQuery);
            }

        }
    }


    void onResultReturned(String result){
        progressBar.setVisibility(View.INVISIBLE);
        resultsTextView.setText(result);
    }



}