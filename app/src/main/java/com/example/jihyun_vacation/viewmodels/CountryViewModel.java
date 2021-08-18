package com.example.jihyun_vacation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jihyun_vacation.models.Country;
import com.example.jihyun_vacation.repository.CountryRepository;

import java.util.List;

public class CountryViewModel extends AndroidViewModel {

    private final String TAG = this.getClass().getCanonicalName();
    private static CountryViewModel ourInstance;

    private final CountryRepository countryRepository = new CountryRepository();
    public LiveData<List<Country>> allFavorites;


    public static CountryViewModel getInstance(Application application) {
        if (ourInstance == null) {
            ourInstance = new CountryViewModel(application);
        }
        return ourInstance;
    }

    public CountryRepository getCountryRepository() { return countryRepository; }


    public CountryViewModel(Application application) {
        super(application);
        this.countryRepository.getAllFavorites();
        this.allFavorites = this.countryRepository.allFavorites;
    }

    public void addFavorite(Country newFavorite) {
        this.countryRepository.addFavorite(newFavorite);
    }

    public void removeFavorite(String docID) {
        this.countryRepository.removeFavorite(docID);
    }

}
