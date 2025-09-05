package com.propertyapp.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.propertyapp.android.PropertyDetailsActivity;
import com.propertyapp.android.R;
import com.propertyapp.android.model.Property;
import com.propertyapp.android.model.User;
import com.propertyapp.android.model.UserType;
import com.propertyapp.android.utils.SharedPrefManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private Context context;
    private List<Property> properties;
    private OnPropertyClickListener listener;

    public interface OnPropertyClickListener {
        void onInquireClick(Property property);
    }

    public PropertyAdapter(Context context, List<Property> properties) {
        this.context = context;
        this.properties = properties;
    }

    public void setOnPropertyClickListener(OnPropertyClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = properties.get(position);
        holder.bind(property);
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public void updateProperties(List<Property> newProperties) {
        this.properties.clear();
        this.properties.addAll(newProperties);
        notifyDataSetChanged();
    }

    class PropertyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPropertyImage;
        private TextView tvTitle, tvPropertyType, tvPrice, tvLocation, tvSize, tvSellerName, tvSellerContact;
        private MaterialButton btnInquire;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ivPropertyImage = itemView.findViewById(R.id.ivPropertyImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPropertyType = itemView.findViewById(R.id.tvPropertyType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvSellerName = itemView.findViewById(R.id.tvSellerName);
            tvSellerContact = itemView.findViewById(R.id.tvSellerContact);
            btnInquire = itemView.findViewById(R.id.btnInquire);
        }

        public void bind(Property property) {
            tvTitle.setText(property.getTitle());
            tvPropertyType.setText(property.getPropertyType().toString().replace("_", " "));
            
            // Format price in Indian currency
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
            tvPrice.setText(formatter.format(property.getPrice()));
            
            tvLocation.setText(property.getCity() + ", " + property.getState());
            
            if (property.getPropertySize() != null) {
                tvSize.setText(property.getPropertySize() + " sq ft");
            } else {
                tvSize.setText("Size not specified");
            }
            
            if (property.getSeller() != null) {
                tvSellerName.setText(property.getSeller().getFullName());
                tvSellerContact.setText(property.getSeller().getMobileNumber());
            }

            // Check if current user is customer to show inquire button
            User currentUser = SharedPrefManager.getInstance(context).getUser();
            if (currentUser != null && currentUser.getUserType() == UserType.CUSTOMER) {
                btnInquire.setVisibility(View.VISIBLE);
                btnInquire.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onInquireClick(property);
                    }
                });
            } else {
                btnInquire.setVisibility(View.GONE);
            }

            // Click listener for property details
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PropertyDetailsActivity.class);
                intent.putExtra("property_id", property.getPropertyId());
                context.startActivity(intent);
            });
        }
    }
}
