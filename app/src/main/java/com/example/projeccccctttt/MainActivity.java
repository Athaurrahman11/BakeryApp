package com.example.projeccccctttt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalItems, tvWishlistValue, tvSeeAll;
    private Button btnAddItem, btnViewWishlist;
    private LinearLayout containerRecentlyAdded;
    private LinearLayout navHome, navAdd, navWishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
        loadData();
    }

    private void initViews() {
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvWishlistValue = findViewById(R.id.tvWishlistValue);
        tvSeeAll = findViewById(R.id.tvSeeAll);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnViewWishlist = findViewById(R.id.btnViewWishlist);
        containerRecentlyAdded = findViewById(R.id.containerRecentlyAdded);

        navHome = findViewById(R.id.navHome);
        navAdd = findViewById(R.id.navAdd);
        navWishlist = findViewById(R.id.navWishlist);
    }

    private void setupListeners() {
        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

        btnViewWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(intent);
        });

        tvSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(intent);
        });


        navHome.setOnClickListener(v -> {});

        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            startActivity(intent);
        });

        navWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(intent);
        });
    }

    private void loadData() {
        List<BakeryItem> items = BakeryRepository.getAllItems();

        int totalCount = items.size();
        double totalValue = 0;
        for (BakeryItem item : items) {
            totalValue += item.getPrice();
        }

        tvTotalItems.setText(String.valueOf(totalCount));
        tvWishlistValue.setText("LKR " + String.format(Locale.getDefault(), "%,.2f", totalValue));

        containerRecentlyAdded.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        int limit = Math.min(items.size(), 4);
        for (int i = 0; i < limit; i++) {
            BakeryItem item = items.get(i);
            View itemView = inflater.inflate(R.layout.item_bakery, containerRecentlyAdded, false);

            ImageView ivProduct = itemView.findViewById(R.id.ivProduct);
            TextView tvName = itemView.findViewById(R.id.tvProductName);
            TextView tvCategory = itemView.findViewById(R.id.tvCategory);
            TextView tvDate = itemView.findViewById(R.id.tvDate);
            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
            ImageButton btnEdit = itemView.findViewById(R.id.btnEditItem);

            ivProduct.setImageResource(getCategoryIconResId(item.getCategory()));
            tvName.setText(item.getName());
            tvCategory.setText(item.getCategory());
            tvDate.setText(item.getDateAdded());
            tvPrice.setText("LKR " + String.format(Locale.getDefault(), "%,.2f", item.getPrice()));

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("ITEM_ID", item.getId());
                startActivity(intent);
            });

            containerRecentlyAdded.addView(itemView);
        }
    }

    public static int getCategoryIconResId(String category) {
        if (category == null) return R.drawable.logo;
        switch (category) {
            case "Cake":
                return R.drawable.ic_cake;
            case "Donut":
                return R.drawable.ic_donut;
            case "Bun":
                return R.drawable.ic_bun;
            case "Pastry":
                return R.drawable.ic_pastry;
            case "Cookies":
                return R.drawable.ic_cookie;
            default:
                return R.drawable.logo;
        }
    }
}
