package com.example.moment_app.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.UserResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.repository.AuthenticationRepository;
import com.example.moment_app.ui.auth.AuthActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private Button buttonLogout;

    private ImageView imageViewAvtSetting;
    private TextView textViewNameSetting;
    private TextView textViewUsernameSetting;
    private TextView textViewSexSetting;
    private TextView textViewBirthdaySetting;
    private Button buttonChangeProfile;
    private Button buttonChangePassword;
    private Button buttonChangeUsername;

    private  Button buttonChangeAvtSetting;

    private AccountRepository accountRepository;


    private AuthenticationRepository authenticationRepository;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonLogout = view.findViewById(R.id.buttonLogout);

         imageViewAvtSetting  = view.findViewById(R.id.imageViewAvtSetting);
         textViewNameSetting  = view.findViewById(R.id.textViewNameSetting);
         textViewUsernameSetting  = view.findViewById(R.id.textViewUsernameSetting);
         textViewSexSetting  = view.findViewById(R.id.textViewSexSetting);
         textViewBirthdaySetting  = view.findViewById(R.id.textViewBirthdaySetting);
         buttonChangeProfile  = view.findViewById(R.id.buttonChangeProfile);
         buttonChangePassword  = view.findViewById(R.id.buttonChangePassword);
         buttonChangeUsername  = view.findViewById(R.id.buttonChangeUsername);
        buttonChangeAvtSetting = view.findViewById(R.id.buttonChangeAvtSetting);

        buttonLogout.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MomentPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("access_token"); // Xóa key access_token
            editor.apply(); // hoặc editor.commit();


            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent); // Mở MainActivity
            requireActivity().finish();
        });

        buttonChangeProfile.setOnClickListener(v -> {
            Fragment profileFragment = new ChangeProfileFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, profileFragment) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });

        buttonChangeUsername.setOnClickListener(v -> {
            Fragment username = new ChangeUsernameFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, username) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });

        buttonChangePassword.setOnClickListener(v -> {
            Fragment password = new ChangePasswordFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, password) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });

        buttonChangeAvtSetting.setOnClickListener(v -> {
            Fragment password = new ChangeAvtFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, password) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });


        authenticationRepository = new AuthenticationRepository(requireContext());

        authenticationRepository.getMyInfo(new AuthenticationRepository.UserCallback() {
            @Override
            public void onSuccess(ApiResponse<UserResponse> response) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Status: " + response.getStatus(), Toast.LENGTH_SHORT).show();

                    if (response.getStatus() == 200) {
                        // Lấy thông tin người dùng
                        textViewNameSetting.setText(response.getResult().getName());
                        String imageUrl = "https://res.cloudinary.com/moment-images/image/upload/" + response.getResult().getUrlPhoto();

                        Glide.with(requireContext())
                                .load(imageUrl)
                                .circleCrop()
                                .error(R.drawable.avatar)
                                .placeholder(R.drawable.avatar)
                                .into(imageViewAvtSetting);
                        textViewUsernameSetting.setText(response.getResult().getUserName());
                        textViewBirthdaySetting.setText(response.getResult().getBirthday());
                        if (response.getResult().getSex().equals("male")) {
                            textViewSexSetting.setText("Nam");
                        }
                        else if (response.getResult().getSex().equals("female")) {
                            textViewSexSetting.setText("Nữ");
                        } else if (response.getResult().getSex().equals("other")) {
                            textViewSexSetting.setText("Khác");
                        }




                    } else {

                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                });
            }
        });





    }
}