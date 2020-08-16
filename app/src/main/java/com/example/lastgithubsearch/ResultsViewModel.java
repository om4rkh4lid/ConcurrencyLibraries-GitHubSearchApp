package com.example.lastgithubsearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// TODO(4) - Create a ViewModel class for the activity/fragment that will display the results.
/**
 * from https://developer.android.com/jetpack/docs/guide
 * A ViewModel object provides the data for a specific UI component,
 * such as a fragment or activity, and contains data-handling business logic to communicate with the model.
 * For example, the ViewModel can call other components to load the data,
 * and it can forward user requests to modify the data. The ViewModel doesn't know about UI components,
 * so it isn't affected by configuration changes, such as recreating an activity when rotating the device.
 */
public class ResultsViewModel extends ViewModel {

    //Instance of Repository
    private final NetworkRepository networkRepository;

    //TODO(5) - Create an instance of MutableLiveData to update the UI
    /**
     * LiveData is an observable data holder class.
     * Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components,
     * such as activities, fragments, or services. This awareness ensures LiveData only updates app component observers that are in an active lifecycle state.
     *
     * LiveData has no publicly available methods to update the stored data, (e.g. Room generates all the necessary code to update the LiveData object when a database is updated.
     * The generated code runs the query asynchronously on a background thread when needed.)
     *
     * The MutableLiveData class exposes the setValue(T) and postValue(T) methods publicly and you must use these if you need to edit the value stored in a LiveData object yourself.
     * Usually MutableLiveData is used in the ViewModel and then the ViewModel only exposes immutable LiveData objects to the observers.
     */
    MutableLiveData<String> resultString;


    ResultsViewModel(NetworkRepository repository){
        networkRepository = repository;
    }

    // TODO(9) - Make a method that calls the repository's makeNetworkRequest and implements the callback to update the MutableLiveData instance
    void getSearchResult(String query){
        networkRepository.makeNetworkRequest(
                query,
                new NetworkRepository.NetworkRequestCallback() {
                    @Override
                    public void onCompleted(String result) {
                        //we are calling setValue() because this method will be executed on the main thread
                        //if we were to update the MutableLiveData object from a worker thread we would use postValue()
                        resultString.setValue(result);
                    }
                },
                MyApp.mainThreadHandler);
    }

    // TODO(10) - Make a getter method that returns an IMMUTABLE LiveData instance
    LiveData<String> getResultString(){
        return resultString == null? resultString = new MutableLiveData<String>() : resultString;
    }




}
