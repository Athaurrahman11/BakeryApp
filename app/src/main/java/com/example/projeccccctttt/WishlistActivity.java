package com.example.projeccccctttt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

public class WishlistActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView tvStatTotal, tvStatValue, tvStatUnpurchased;
    private LinearLayout containerWishlist, layoutEmpty;
    private LinearLayout navHome, navAdd, navWishlist;

    private List<BakeryItem> allItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            View bottomNav = findViewById(R.id.bottomNav);
            if (bottomNav != null) {
                bottomNav.setPadding(
                    bottomNav.getPaddingLeft(),
                    bottomNav.getPaddingTop(),
                    bottomNav.getPaddingRight(),
                    systemBars.bottom + 8
                );
            }
            return insets;
        });

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishlistData("");
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        tvStatTotal = findViewById(R.id.tvStatTotal);
        tvStatValue = findViewById(R.id.tvStatValue);
        tvStatUnpurchased = findViewById(R.id.tvStatUnpurchased);
        containerWishlist = findViewById(R.id.containerWishlist);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        navHome = findViewById(R.id.navHome);
        navAdd = findViewById(R.id.navAdd);
        navWishlist = findViewById(R.id.navWishlist);
    }

    private void setupListeners() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadWishlistData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(WishlistActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(WishlistActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

        navWishlist.setOnClickListener(v -> {});
    }

    private void loadWishlistData(String query) {
        allItems = BakeryRepository.getAllItems();

        int totalCount = allItems.size();
        double totalValue = 0;
        double unpurchasedValue = 0;
        for (BakeryItem item : allItems) {
            totalValue += item.getPrice();
            if (!item.isPurchased()) {
                unpurchasedValue += item.getPrice();
            }
        }

        tvStatTotal.setText(String.valueOf(totalCount));
        tvStatValue.setText("LKR " + String.format(Locale.getDefault(), "%,.2f", totalValue));
        tvStatUnpurchased.setText("LKR " + String.format(Locale.getDefault(), "%,.2f", unpurchasedValue));

        containerWishlist.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        int visibleCount = 0;

        for (BakeryItem item : allItems) {
            if (!query.isEmpty() && !item.getName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                    && !item.getCategory().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                continue;
            }

            visibleCount++;
            View itemView = inflater.inflate(R.layout.item_wishlist, containerWishlist, false);

            ImageView ivProduct = itemView.findViewById(R.id.ivProduct);
            TextView tvName = itemView.findViewById(R.id.tvProductName);
            TextView tvCategory = itemView.findViewById(R.id.tvCategory);
            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
            CheckBox cbPurchased = itemView.findViewById(R.id.cbPurchased);
            ImageButton btnEdit = itemView.findViewById(R.id.btnEdit);
            ImageButton btnDelete = itemView.findViewById(R.id.btnDelete);

            ivProduct.setImageResource(MainActivity.getCategoryIconResId(item.getCategory()));
            tvName.setText(item.getName());
            tvCategory.setText(item.getCategory());
            tvPrice.setText("LKR " + String.format(Locale.getDefault(), "%,.2f", item.getPrice()));
            cbPurchased.setChecked(item.isPurchased());

            cbPurchased.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setPurchased(isChecked);
                BakeryRepository.updateItem(item);
                String msg = isChecked ? "Item marked as purchased!" : "Item marked as unpurchased";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                loadWishlistData(etSearch.getText().toString());
            });

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(WishlistActivity.this, AddEditActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                BakeryRepository.deleteItem(item.getId());
                Toast.makeText(this, "Item deleted from wishlist", Toast.LENGTH_SHORT).show();
                loadWishlistData(etSearch.getText().toString());
            });

            containerWishlist.addView(itemView);
        }

        if (visibleCount == 0) {
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
        }
    }
}
