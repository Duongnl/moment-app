package com.example.moment_app.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.moment_app.R;
import com.example.moment_app.ui.main.MainActivity;
import com.google.android.material.tabs.TabLayout;

public class AuthActivity extends AppCompatActivity {

    private TabLayout tabLayoutAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MomentPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        if (token != null) {
            Intent intent = new Intent(this, MainActivity.class); // Sử dụng `this` thay vì `getContext()`
            startActivity(intent); // Mở MainActivity
            finish(); // Kết thúc màn hình hiện tại (đăng nhập)
        }

        tabLayoutAuth = findViewById(R.id.tabLayoutAuth);
        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setText("Đăng nhập"));
        tabLayoutAuth.addTab(tabLayoutAuth.newTab().setText("Đăng ký"));

        // Mặc định hiển thị fragment đầu tiên
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerAuth, new LoginFragment())
                .commit();



        // Lắng nghe sự kiện tab được chọn
        tabLayoutAuth.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new LoginFragment();
                        break;
                    case 1:
                        selectedFragment = new RegisterFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerAuth, selectedFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý nếu không cần lưu trạng thái tab cũ
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Có thể dùng để refresh lại fragment
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }
}