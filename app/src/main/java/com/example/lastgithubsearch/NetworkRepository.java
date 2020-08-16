package com.example.lastgithubsearch;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

// TODO(2) - Create a Repository to handle network requests
public class NetworkRepository {
    /**
     *  The repository layer is in charge of moving the execution of the network request off
     *  the main thread and posting the result back to the main thread using a callback.
     *
     *  The repository layer is usually responsible for handling data among more than one source,
     *  e.g. database and network
     *
     *  in this app we are only handling network request so the network code will be handled here.
     */

    // TODO(3) - Make a callback interface to be called on receiving a response (or error) from the network request
    // this class will be implemented on object creation, when we are calling makeNetworkRequest.
    // the idea is to give a reference of an object, that can do something in the context of the ViewModel,
    // to the repository in order to have somewhere to return the results
    interface NetworkRequestCallback{
        void onCompleted(String result);
    }



    //constants
    static final String baseURI = "https://api.github.com/search/repositories";
    //instance of executor
    private final Executor executor;
    //singleton instance
    private static NetworkRepository INSTANCE;
    //lock
    private static final Object LOCK = new Object();

    MutableLiveData<String> string;

    // the constructor is passed an Executor instance instead of an Executor service because the
    // repository will only need to call execute() and will not perform any thread management.
    private NetworkRepository(Executor executor) {
        this.executor = executor;
    }

    //for singleton
    public static NetworkRepository getInstance(Executor executor){
        if(INSTANCE == null){
            synchronized (LOCK){
                if(INSTANCE == null){
                    INSTANCE = new NetworkRepository(executor);
                }
            }
        }
        return INSTANCE;
    }

    // TODO(7) - Code for runnning the network request Asynchronously
    /**
     * @param query - the string of the query to be run
     * @param callback - the callback object that will handle the result
     * @param resultHandler - the Handler for the thread that will run the callback
     */
    void makeNetworkRequest(final String query, final NetworkRequestCallback callback, final Handler resultHandler){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = getResponseFromNetwork(query);
                    notifyResult(result, callback, resultHandler);
                    //alternatively if we have a reference to a LiveData instance we can chose to run postResults() on it
                    //this removes the need for both the callback object and the notifyResult method
                }catch (Exception e){
                    notifyResult(e.getLocalizedMessage(), callback, resultHandler);
                }
            }
        });
    }


    //TODO(8) - Make a method to run the callback on the main thread using the mainThreadHandler
    void notifyResult(final String result, final NetworkRequestCallback callback, Handler resultHandler){
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onCompleted(result);
            }
        });
    }

    // TODO(6) - Code for the network request
    //method that builds a URL from a base URI and parameters, the result will be a URL formatted like:
    //"https://api.github.com/search/repositories?q=WHATEVER+language:assembly&sort=stars&order=desc"
    private URL buildUrl(String query) {
        Uri builtUri = Uri.parse(baseURI).buildUpon()
                .appendQueryParameter("q", query)
                .appendQueryParameter("sort", "stars")
                .appendQueryParameter("order", "desc")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    protected String getResponseFromNetwork(String query) {
        //result
        String result = null;

        URL url = buildUrl(query);

        // represents a communications link between the application and a URL and can be used both to read from and to write to the resource referenced by the URL.
        // https://developer.android.com/reference/java/net/HttpURLConnection
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Returns an input stream that reads from this open connection.
        // A SocketTimeoutException can be thrown when reading from the returned
        // input stream if the read timeout expires before data is available for read.
        InputStream input = null;
        try {
            input = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                result = scanner.next();
            }
        }catch (Exception x){
            x.printStackTrace();
        }finally {
            //Disconnecting releases the resources held by a connection so they may be closed or reused.
            httpURLConnection.disconnect();
        }

        return result;
    }


}
