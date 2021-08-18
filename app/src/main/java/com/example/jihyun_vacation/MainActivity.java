package com.example.jihyun_vacation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.jihyun_vacation.adapters.CountryAdapter;
import com.example.jihyun_vacation.adapters.OnCountryClickListener;
import com.example.jihyun_vacation.databinding.ActivityMainBinding;
import com.example.jihyun_vacation.models.Country;
import com.example.jihyun_vacation.network.RetrofitClient;
import com.example.jihyun_vacation.viewmodels.CountryViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnCountryClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    ActivityMainBinding binding;
    private ArrayList<Country> countries;
    private CountryAdapter adapter;
    private CountryViewModel countryViewModel;
    //private ItemTouchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.countries = new ArrayList<>();
        getCountries();

        this.adapter = new CountryAdapter(this, countries, (OnCountryClickListener) this);
        this.binding.rvCountries.setAdapter(adapter);
        this.binding.rvCountries.setLayoutManager(new LinearLayoutManager(this));
        this.binding.rvCountries.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //this.countryViewModel = new ViewModelProvider(this).get(CountryViewModel.class);
        this.countryViewModel = CountryViewModel.getInstance(this.getApplication());
    }

    private void getCountries() {
        Call<ArrayList<Country>> call = RetrofitClient.getInstance().getApi().retrieveCountries();

        try {
            call.enqueue(new Callback<ArrayList<Country>>() {
                @Override
                public void onResponse(Call<ArrayList<Country>> call, Response<ArrayList<Country>> response) {
                    if (response.code() == 200 && response.body() != null) {
                        List<Country> main_response = (List<Country>) response.body();
                        Log.e(TAG, main_response.size() + "objects received");
                        countries.clear();
                        countries.addAll(main_response);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Log.e(TAG, "OnResponse: No response received");
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<ArrayList<Country>> call, Throwable t) {
                    Log.e(TAG, "Error while fetching data -- " + t.getLocalizedMessage());
                }
            });
        }catch (Exception ex){
            Log.e(TAG, "Exception occurred : " + ex.getLocalizedMessage());
        }
    }


    public void goToFavorite() {
        Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
        startActivity(favoriteIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_favorite:{
                this.goToFavorite();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCountryClicked(Country country) {
        Log.e(TAG, "A country selected: " + country);
        this.countryViewModel.addFavorite(country);
        Log.d(TAG, "saveNewFav: Item inserted" + country.toString());
        //this.finish();
        Toast.makeText(getApplicationContext(),
                country.getName() + " has been added to Favorite",
                Toast.LENGTH_LONG).show();

    }

}