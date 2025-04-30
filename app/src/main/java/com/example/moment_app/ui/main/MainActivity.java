package com.example.moment_app.ui.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.moment_app.R;
import com.example.moment_app.ui.auth.LoginFragment;
import com.example.moment_app.ui.auth.RegisterFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tabLayoutMain = findViewById(R.id.tabLayoutMain);
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText("Home"));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText("Bạn bè"));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText("Cài đặt"));

        // Mặc định hiển thị fragment đầu tiên
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerMain, new HomeFragment())
                .commit();



        // Lắng nghe sự kiện tab được chọn
        tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new HomeFragment();
                        break;
                    case 1:
                        selectedFragment = new FriendFragment();
                        break;
                    case 2:
                        selectedFragment = new SettingFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerMain, selectedFragment)
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