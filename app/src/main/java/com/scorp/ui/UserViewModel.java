package com.scorp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scorp.data.DataSource;
import com.scorp.data.Person;

import java.util.ArrayList;
import java.util.List;


public class UserViewModel extends ViewModel {

    private DataSource mDataSource;

    public MutableLiveData<List<Person>> getPersonList(String next){

        if (mDataSource == null)
            mDataSource = new DataSource();

        final MutableLiveData<List<Person>> mutableLiveData = new MutableLiveData<>();

        mDataSource.fetch(next, (fetchResponse, fetchError) -> {

            if (fetchError != null){
                mutableLiveData.postValue(null);
            }else if (fetchResponse.getPeople().isEmpty())
                mutableLiveData.postValue(new ArrayList<>());
            else
                mutableLiveData.postValue(fetchResponse.getPeople());

            return null;
        });

        return mutableLiveData;
    }
}