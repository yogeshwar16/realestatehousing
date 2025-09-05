package com.propertyapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.propertyapp.android.adapter.PropertyAdapter;
import com.propertyapp.android.api.ApiClient;
import com.propertyapp.android.api.ApiResponse;
import com.propertyapp.android.api.ApiService;
import com.propertyapp.android.model.Property;
import com.propertyapp.android.model.PropertyType;
import com.propertyapp.android.model.User;
import com.propertyapp.android.model.UserType;
import com.propertyapp.android.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements PropertyAdapter.OnPropertyClickListener {

    private TextView tvWelcome, tvUserType;
    private TextInputEditText etSearch;
    private Chip chipAll, chipLand, chipFlat, chipRowHouse, chipBungalow;
    private RecyclerView rvProperties;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabAddProperty;
    private ProgressBar progressBar;

    private PropertyAdapter propertyAdapter;
    private List<Property> allProperties = new ArrayList<>();
    private List<Property> filteredProperties = new ArrayList<>();
    private ApiService apiService;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupUser();
        setupRecyclerView();
        setupClickListeners();
        setupSearch();
        
        apiService = ApiClient.getApiService();
        loadProperties();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserType = findViewById(R.id.tvUserType);
        etSearch = findViewById(R.id.etSearch);
        chipAll = findViewById(R.id.chipAll);
        chipLand = findViewById(R.id.chipLand);
        chipFlat = findViewById(R.id.chipFlat);
        chipRowHouse = findViewById(R.id.chipRowHouse);
        chipBungalow = findViewById(R.id.chipBungalow);
        rvProperties = findViewById(R.id.rvProperties);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        fabAddProperty = findViewById(R.id.fabAddProperty);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupUser() {
        currentUser = SharedPrefManager.getInstance(this).getUser();
        if (currentUser != null) {
            tvWelcome.setText("Welcome, " + currentUser.getFullName().split(" ")[0] + "!");
            tvUserType.setText(currentUser.getUserType().toString());
            
            // Show FAB only for sellers
            if (currentUser.getUserType() == UserType.SELLER) {
                fabAddProperty.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupRecyclerView() {
        propertyAdapter = new PropertyAdapter(this, filteredProperties);
        propertyAdapter.setOnPropertyClickListener(this);
        rvProperties.setLayoutManager(new LinearLayoutManager(this));
        rvProperties.setAdapter(propertyAdapter);
    }

    private void setupClickListeners() {
        // Profile click
        findViewById(R.id.ivProfile).setOnClickListener(v -> {
            // TODO: Open profile/settings screen
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });

        // Filter click
        findViewById(R.id.ivFilter).setOnClickListener(v -> {
            // TODO: Open filter dialog
            Toast.makeText(this, "Filter clicked", Toast.LENGTH_SHORT).show();
        });

        // Property type chips
        chipAll.setOnClickListener(v -> filterByType(null));
        chipLand.setOnClickListener(v -> filterByType(PropertyType.LAND));
        chipFlat.setOnClickListener(v -> filterByType(PropertyType.FLAT));
        chipRowHouse.setOnClickListener(v -> filterByType(PropertyType.ROW_HOUSE));
        chipBungalow.setOnClickListener(v -> filterByType(PropertyType.BUNGALOW));

        // Add property FAB
        fabAddProperty.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPropertyActivity.class));
        });

        // Swipe refresh
        swipeRefresh.setOnRefreshListener(this::loadProperties);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProperties(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadProperties() {
        showLoading(true);
        
        Call<ApiResponse<List<Property>>> call = apiService.getAllProperties();
        call.enqueue(new Callback<ApiResponse<List<Property>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Property>>> call, Response<ApiResponse<List<Property>>> response) {
                showLoading(false);
                swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Property>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        allProperties.clear();
                        allProperties.addAll(apiResponse.getData());
                        filteredProperties.clear();
                        filteredProperties.addAll(allProperties);
                        propertyAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(DashboardActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to load properties", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Property>>> call, Throwable t) {
                showLoading(false);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(DashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByType(PropertyType propertyType) {
        // Update chip selection
        chipAll.setChecked(propertyType == null);
        chipLand.setChecked(propertyType == PropertyType.LAND);
        chipFlat.setChecked(propertyType == PropertyType.FLAT);
        chipRowHouse.setChecked(propertyType == PropertyType.ROW_HOUSE);
        chipBungalow.setChecked(propertyType == PropertyType.BUNGALOW);

        // Filter properties
        if (propertyType == null) {
            filteredProperties.clear();
            filteredProperties.addAll(allProperties);
        } else {
            filteredProperties.clear();
            for (Property property : allProperties) {
                if (property.getPropertyType() == propertyType) {
                    filteredProperties.add(property);
                }
            }
        }

        // Apply search filter if search text exists
        String searchText = etSearch.getText().toString().trim();
        if (!searchText.isEmpty()) {
            filterProperties(searchText);
        } else {
            propertyAdapter.notifyDataSetChanged();
        }
    }

    private void filterProperties(String searchText) {
        if (searchText.isEmpty()) {
            propertyAdapter.updateProperties(filteredProperties);
            return;
        }

        List<Property> searchResults = new ArrayList<>();
        for (Property property : filteredProperties) {
            if (property.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                property.getCity().toLowerCase().contains(searchText.toLowerCase()) ||
                property.getState().toLowerCase().contains(searchText.toLowerCase()) ||
                property.getAddress().toLowerCase().contains(searchText.toLowerCase())) {
                searchResults.add(property);
            }
        }
        
        propertyAdapter.updateProperties(searchResults);
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            rvProperties.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            rvProperties.setVisibility(View.VISIBLE);
        }
    }

    private void onInquireClicked(Property property) {
        Intent intent = new Intent(this, PropertyDetailsActivity.class);
        intent.putExtra("property", property);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh properties when returning to dashboard
        loadProperties();
    }
}
