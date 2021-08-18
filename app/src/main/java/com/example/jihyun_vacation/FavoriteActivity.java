package com.example.jihyun_vacation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jihyun_vacation.adapters.CountryAdapter;
import com.example.jihyun_vacation.adapters.OnCountryClickListener;
import com.example.jihyun_vacation.databinding.ActivityFavoriteBinding;
import com.example.jihyun_vacation.databinding.ActivityMainBinding;
import com.example.jihyun_vacation.models.Country;
import com.example.jihyun_vacation.viewmodels.CountryViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements OnCountryClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    ActivityFavoriteBinding binding;
    private ArrayList<Country> allFav;
    private CountryAdapter adapter;
    private CountryViewModel countryViewModel;
    private Country tempCountry;
    private ItemTouchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.allFav = new ArrayList<>();
        this.adapter = new CountryAdapter(this, allFav, (OnCountryClickListener) this);
        this.binding.rvList.setAdapter(adapter);
        this.binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        this.binding.rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        this.countryViewModel = new ViewModelProvider(this).get(CountryViewModel.class);

        //this.countryViewModel = CountryViewModel.getInstance(this.getApplication());
        this.countryViewModel.allFavorites.observe(this, new Observer<List<Country>>() {
            @Override
            public void onChanged(List<Country> countries) {
                if (!countries.isEmpty()) {
                    Log.e(TAG, "onChanged: Favorite received from DB" + countries.toString());
                    //allFav.clear();
                    for (Country ctr : countries) {
                        allFav.add(ctr);
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Log.e(TAG, "onChanged: empty Favorite list");
                }
            }
        });
    }

    @Override
    public void onCountryClicked(Country country) {
        this.countryViewModel.removeFavorite(country.getId());
        Log.d(TAG, "deleteFavorite: Item removed" + country.toString());
        Toast.makeText(getApplicationContext(),
                " The Country " + country.getName() + " has been removed",
                Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();
    }
}