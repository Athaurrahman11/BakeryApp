package com.example.projeccccctttt;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    private TextView tvTitle, tvError;
    private EditText etItemName, etPrice;
    private Spinner spinnerCategory;
    private RadioGroup rgPriority;
    private RadioButton rbLow, rbMedium, rbHigh;
    private Button btnSave, btnCancel;
    private ImageView ivCategoryPreview;

    private String editingItemId = null;
    private BakeryItem currentItem = null;

    private final String[] categories = {"Cake", "Donut", "Bun", "Pastry", "Cookies"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupSpinner();
        checkEditingMode();
        setupListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvError = findViewById(R.id.tvError);
        etItemName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        rgPriority = findViewById(R.id.rgPriority);
        rbLow = findViewById(R.id.rbLow);
        rbMedium = findViewById(R.id.rbMedium);
        rbHigh = findViewById(R.id.rbHigh);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        ivCategoryPreview = findViewById(R.id.ivCategoryPreview);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePreviewIcon(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updatePreviewIcon(String category) {
        if (ivCategoryPreview == null) return;
        int resId;
        switch (category) {
            case "Cake":
                resId = R.drawable.ic_cake;
                break;
            case "Donut":
                resId = R.drawable.ic_donut;
                break;
            case "Bun":
                resId = R.drawable.ic_bun;
                break;
            case "Pastry":
                resId = R.drawable.ic_pastry;
                break;
            case "Cookies":
                resId = R.drawable.ic_cookie;
                break;
            default:
                resId = R.drawable.logo;
                break;
        }
        ivCategoryPreview.setImageResource(resId);
    }

    private void checkEditingMode() {
        if (getIntent() != null && getIntent().hasExtra("ITEM_ID")) {
            editingItemId = getIntent().getStringExtra("ITEM_ID");
            currentItem = BakeryRepository.getItemById(editingItemId);
            if (currentItem != null) {
                tvTitle.setText("Edit Bakery Item");
                btnSave.setText("Update Item");
                etItemName.setText(currentItem.getName());
                etPrice.setText(String.valueOf(currentItem.getPrice()));

                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equals(currentItem.getCategory())) {
                        spinnerCategory.setSelection(i);
                        updatePreviewIcon(categories[i]);
                        break;
                    }
                }

                if ("Low".equals(currentItem.getPriority())) {
                    rbLow.setChecked(true);
                } else if ("High".equals(currentItem.getPriority())) {
                    rbHigh.setChecked(true);
                } else {
                    rbMedium.setChecked(true);
                }
            }
        }
    }

    private void setupListeners() {
        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name = etItemName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            int selectedPriorityId = rgPriority.getCheckedRadioButtonId();
            String priority = "Medium";
            if (selectedPriorityId == R.id.rbLow) {
                priority = "Low";
            } else if (selectedPriorityId == R.id.rbHigh) {
                priority = "High";
            }

            if (name.isEmpty()) {
                showError("Please enter bakery item name");
                return;
            }

            if (priceStr.isEmpty()) {
                showError("Please enter price in LKR");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    showError("Price cannot be negative");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid price");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            if (editingItemId != null && currentItem != null) {
                currentItem.setName(name);
                currentItem.setCategory(category);
                currentItem.setPrice(price);
                currentItem.setPriority(priority);
                BakeryRepository.updateItem(currentItem);
                Toast.makeText(this, "Bakery item updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                BakeryItem newItem = new BakeryItem(name, category, price, priority, currentDate);
                BakeryRepository.addItem(newItem);
                Toast.makeText(this, "Bakery item added successfully!", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
