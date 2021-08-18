package com.example.jihyun_vacation.network;

import com.example.jihyun_vacation.models.Country;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    String BASE_URL = "https://restcountries.eu/rest/v2/";

    @GET("./all")
    Call<ArrayList<Country>> retrieveCountries();

}
