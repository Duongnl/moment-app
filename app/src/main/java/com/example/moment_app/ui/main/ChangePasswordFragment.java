package com.example.moment_app.ui.main;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.moment_app.R;
import com.example.moment_app.models.request.ChangePasswordRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.ui.auth.AuthActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    private EditText editTextPasswordOld;
    private TextView textViewPasswordOldError;

    private EditText editTextPasswordNew;

    private TextView textViewPasswordNewError;

    private EditText editTextPasswordReNew;

    private TextView textViewPasswordReNewError;

    private Button buttonSavePassword;
    private AccountRepository accountRepository;

    private Button buttonBackChangePassword;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTextPasswordOld = view.findViewById(R.id.editTextPasswordOld);
       textViewPasswordOldError = view.findViewById(R.id.textViewPasswordOldError);

        editTextPasswordNew = view.findViewById(R.id.editTextPasswordNew);
        textViewPasswordNewError = view.findViewById(R.id.textViewPasswordNewError);

        editTextPasswordReNew = view.findViewById(R.id.editTextPasswordReNew);
        textViewPasswordReNewError = view.findViewById(R.id.textViewPasswordReNewError);

        buttonSavePassword = view.findViewById(R.id.buttonSavePassword);
        buttonBackChangePassword = view.findViewById(R.id.buttonBackChangePassword);


        textViewPasswordOldError.setVisibility(View.GONE);
        textViewPasswordNewError.setVisibility(View.GONE);
        textViewPasswordReNewError.setVisibility(View.GONE);
        buttonBackChangePassword.setOnClickListener(v -> {
            Fragment setting = new SettingFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, setting) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });

        editTextPasswordOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewPasswordOldError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPasswordNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewPasswordNewError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPasswordReNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewPasswordReNewError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        accountRepository = new AccountRepository(requireContext());
        buttonSavePassword.setOnClickListener(v -> {

            buttonSavePassword.setEnabled(false);
            buttonSavePassword.setText("Đang lưu thông tin ....");

            String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!.@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

            Pattern pattern = Pattern.compile(PASSWORD);

            Matcher matcher = pattern.matcher(editTextPasswordOld.getText().toString().trim());
            if (!matcher.matches()) {
                textViewPasswordOldError.setText("Mật khẩu không hợp lệ");
                textViewPasswordOldError.setVisibility(View.VISIBLE);
                buttonSavePassword.setEnabled(true);
                buttonSavePassword.setText("Lưu thông tin");
                return;
            }
            matcher = pattern.matcher(editTextPasswordNew.getText().toString().trim());
            if (!matcher.matches()) {
                textViewPasswordNewError.setText("Mật khẩu không hợp lệ");
                textViewPasswordNewError.setVisibility(View.VISIBLE);
                buttonSavePassword.setEnabled(true);
                buttonSavePassword.setText("Lưu thông tin");
                return;
            }

            if (!editTextPasswordReNew.getText().toString().trim().equals(editTextPasswordNew.getText().toString().trim())) {
                textViewPasswordReNewError.setText("Mật khẩu không khớp");
                textViewPasswordReNewError.setVisibility(View.VISIBLE);
                buttonSavePassword.setEnabled(true);
                buttonSavePassword.setText("Lưu thông tin");
                return;
            }


            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
            changePasswordRequest.setNewPassword(editTextPasswordNew.getText().toString().trim());
            changePasswordRequest.setOldPassword(editTextPasswordOld.getText().toString().trim());

            accountRepository.changePassword(changePasswordRequest, new AccountRepository.AccountInfoCallback() {
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

                            buttonSavePassword.setEnabled(true);
                            buttonSavePassword.setText("Lưu thông tin");
                            //            ACCOUNT_6 mk moi khong duoc giong mk cu
//            ACCOUNT_4 mkk cu khong dung
//            INVALID_2 khong dung dinh dang mk

                            for (int i =0; i<response.getErrors().size(); i++)
                            {
                                if (response.getErrors().get(i).getCode().equals("ACCOUNT_6")) {
                                    textViewPasswordNewError.setText("Mk mới không được giống mk cũ");
                                    textViewPasswordNewError.setVisibility(View.VISIBLE);
                                }

                                if (response.getErrors().get(i).getCode().equals("ACCOUNT_4"))
                                {
                                    textViewPasswordOldError.setText("Mk cũ không đúng");
                                    textViewPasswordOldError.setVisibility(View.VISIBLE);
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


    };
}