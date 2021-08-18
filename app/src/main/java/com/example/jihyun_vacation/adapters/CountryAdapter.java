package com.example.jihyun_vacation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jihyun_vacation.databinding.RvItemCountryBinding;
import com.example.jihyun_vacation.models.Country;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private final String TAG = "Country Adapter";
    private final Context context;
    private final ArrayList<Country> countryArrayList;
    private final OnCountryClickListener itemClickListener;

    public CountryAdapter(Context context, ArrayList<Country> countryArrayList, OnCountryClickListener itemClickListener) {
        this.context = context;
        this.countryArrayList = countryArrayList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public CountryAdapter.CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemCountryBinding.inflate(LayoutInflater.from(context), parent, false);

        return new CountryViewHolder(RvItemCountryBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.CountryViewHolder holder, int position) {
        final Country currentCountry = countryArrayList.get(position);
        holder.bind(context, currentCountry, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return this.countryArrayList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        RvItemCountryBinding binding;

        public CountryViewHolder(RvItemCountryBinding b) {
            super(b.getRoot());
            binding = b;
        }

        public void bind(Context context, final Country currentCountry, final OnCountryClickListener clickListener) {
            binding.name.setText(currentCountry.getName() + " (" + currentCountry.getNativeName() + ")");
            binding.capital.setText("Capital: " + currentCountry.getCapital());
            binding.subregion.setText("Region: " + currentCountry.getSubregion());

            Glide.with(context.getApplicationContext())
                    .load("https://www.countryflags.io/" + currentCountry.getCode() + "/flat/64.png")
                    .placeholder(ContextCompat.getDrawable(context.getApplicationContext(), android.R.drawable.ic_menu_mylocation))
                    .into(binding.flag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onCountryClicked(currentCountry);
                }
            });

        }

    }
}
