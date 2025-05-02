package com.example.moment_app.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moment_app.R;
import com.example.moment_app.models.request.AccountInfoRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;
import com.example.moment_app.models.response.ProfileResponse;
import com.example.moment_app.models.response.UserResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.repository.AuthenticationRepository;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeProfileFragment extends Fragment {

    private EditText editTextNameSetting;
    private TextView textViewNameErrorSetting;

    private EditText editTextBirthdaySetting;

    private TextView textViewBirthdayErrorSetting;
    private RadioGroup radioGroupSexSetting;

    private Button buttonSaveProfile;

    private Button buttonBackChangeProfile;

    private AuthenticationRepository authenticationRepository;
    private RadioButton radioMaleSetting;
    private RadioButton radioFemaleSetting;

    private AccountRepository accountRepository;

    private String username;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeProfileFragment newInstance(String param1, String param2) {
        ChangeProfileFragment fragment = new ChangeProfileFragment();
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
        return inflater.inflate(R.layout.fragment_change_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         editTextNameSetting = view.findViewById(R.id.editTextNameSetting);
        textViewNameErrorSetting = view.findViewById(R.id.textViewNameErrorSetting);

         editTextBirthdaySetting = view.findViewById(R.id.editTextBirthdaySetting);

        textViewBirthdayErrorSetting = view.findViewById(R.id.textViewBirthdayErrorSetting);
         radioGroupSexSetting = view.findViewById(R.id.radioGroupSexSetting);

        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile);

        radioMaleSetting = view.findViewById(R.id.radioMaleSetting);
        radioFemaleSetting = view.findViewById(R.id.radioFemaleSetting);

        buttonBackChangeProfile = view.findViewById(R.id.buttonBackChangeProfile);


        editTextBirthdaySetting.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (datePickerView, year1, month1, dayOfMonth) -> {
                        // Định dạng: yyyy-MM-dd
                        String selectedDate = String.format("%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        editTextBirthdaySetting.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        buttonBackChangeProfile.setOnClickListener(v -> {
            Fragment setting = new SettingFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, setting) // ID của container chứa Fragment
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
                        editTextNameSetting.setText(response.getResult().getName());
                        editTextBirthdaySetting.setText(response.getResult().getBirthday());

                        if (response.getResult().getSex().equals("male")) {
                            radioGroupSexSetting.check(radioMaleSetting.getId());
                        }
                        else if (response.getResult().getSex().equals("female")) {
                            radioGroupSexSetting.check(radioFemaleSetting.getId());
                        }
                        username = response.getResult().getUserName();



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

        textViewNameErrorSetting.setVisibility(View.GONE);
        textViewBirthdayErrorSetting.setVisibility(View.GONE);

        editTextNameSetting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewNameErrorSetting.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextBirthdaySetting.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewBirthdayErrorSetting.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        accountRepository = new AccountRepository(requireContext());
        buttonSaveProfile.setOnClickListener(v -> {

            // Để bật lại button
            buttonSaveProfile.setEnabled(false);
            buttonSaveProfile.setText("Đang lưu thông tin ....");

            AccountInfoRequest accountInfoRequest = new AccountInfoRequest();
            accountInfoRequest.setBirthday(editTextBirthdaySetting.getText().toString().trim());
            accountInfoRequest.setName(editTextNameSetting.getText().toString().trim());
            accountInfoRequest.setUserName(username);

            int selectedId = radioGroupSexSetting.getCheckedRadioButtonId();
            String gender = ""; // Biến chứa giá trị cần trả về

            if (selectedId == R.id.radioMaleSetting) {
                gender = "male";
            } else if (selectedId == R.id.radioFemaleSetting) {
                gender = "female";
            }

            accountInfoRequest.setSex(gender);



            accountRepository.updateAccountInfo(accountInfoRequest, new AccountRepository.AccountInfoCallback() {
                @Override
                public void onSuccess(ApiResponse<Void> response) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                        if (response.getStatus() == 200) {

                            Fragment setting = new SettingFragment(); // fragment bạn muốn mở
                            requireActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainerMain, setting) // ID của container chứa Fragment
                                    .addToBackStack(null) // cho phép quay lại bằng nút back
                                    .commitAllowingStateLoss();

                        } else {

                            buttonSaveProfile.setEnabled(true);
                            buttonSaveProfile.setText("Lưu thông tin");


                            for (int i =0; i<response.getErrors().size(); i++)
                            {
                                if (response.getErrors().get(i).getCode().equals("INVALID_6")) {
                                    textViewNameErrorSetting.setText("Không đúng định dạng tên");
                                    textViewNameErrorSetting.setVisibility(View.VISIBLE);
                                }

                                if (response.getErrors().get(i).getCode().equals("INVALID_5"))
                                {
                                    textViewBirthdayErrorSetting.setText("Không đúng định dạng ngày");
                                    textViewBirthdayErrorSetting.setVisibility(View.VISIBLE);
                                }
                                if (response.getErrors().get(i).getCode().equals("INVALID_7"))
                                {
                                    textViewBirthdayErrorSetting.setText("Không đúng định dạng ngày");
                                    textViewBirthdayErrorSetting.setVisibility(View.VISIBLE);
                                }

                            }

                        }


                    });
                }

                @Override
                public void onError(Throwable t) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });






        });



    }
}