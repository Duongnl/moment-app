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
import com.example.moment_app.models.request.AccountInfoRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.UserResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.repository.AuthenticationRepository;
import com.example.moment_app.ui.auth.AuthActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeUsernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUsernameFragment extends Fragment {

    private Button buttonBackChangeUsername;
    private EditText editTextUsernameNew;

    private TextView textViewUsernameErrorSetting;

    private Button buttonSaveUsername;

    private TextView textViewUsernameOld;
    private String name;
    private String birthday;
    private String sex;

    private AccountRepository accountRepository;


    private AuthenticationRepository authenticationRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeUsernameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeUsernameFragment newInstance(String param1, String param2) {
        ChangeUsernameFragment fragment = new ChangeUsernameFragment();
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
        return inflater.inflate(R.layout.fragment_change_username, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonBackChangeUsername = view.findViewById(R.id.buttonBackChangeUsername);
        editTextUsernameNew = view.findViewById(R.id.editTextUsernameNew);
         textViewUsernameErrorSetting = view.findViewById(R.id.textViewUsernameErrorSetting);
         buttonSaveUsername = view.findViewById(R.id.buttonSaveUsername);
        textViewUsernameOld = view.findViewById(R.id.textViewUsernameOld);



        authenticationRepository = new AuthenticationRepository(requireContext());

        authenticationRepository.getMyInfo(new AuthenticationRepository.UserCallback() {
            @Override
            public void onSuccess(ApiResponse<UserResponse> response) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Status: " + response.getStatus(), Toast.LENGTH_SHORT).show();

                    if (response.getStatus() == 200) {

                        textViewUsernameOld.setText(response.getResult().getUserName());
                        // Lấy thông tin người dùng
                        name = response.getResult().getName();
                        birthday = response.getResult().getBirthday();
                        sex = response.getResult().getSex();

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

        textViewUsernameErrorSetting.setVisibility(View.GONE);
        editTextUsernameNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewUsernameErrorSetting.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonBackChangeUsername.setOnClickListener(v -> {
            Fragment setting = new SettingFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, setting) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();

        });


        accountRepository = new AccountRepository(requireContext());
        buttonSaveUsername.setOnClickListener(v -> {

            // Để bật lại button
            buttonSaveUsername.setEnabled(false);
            buttonSaveUsername.setText("Đang lưu thông tin ....");

            AccountInfoRequest accountInfoRequest = new AccountInfoRequest();
            accountInfoRequest.setBirthday(birthday);
            accountInfoRequest.setName(name);
            accountInfoRequest.setUserName(editTextUsernameNew.getText().toString().trim());
            accountInfoRequest.setSex(sex);



            accountRepository.changeUserName(accountInfoRequest, new AccountRepository.AccountInfoCallback() {
                @Override
                public void onSuccess(ApiResponse<Void> response) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                        if (response.getStatus() == 200) {

                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MomentPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("access_token"); // Xóa key access_token
                            editor.apply(); // hoặc editor.commit();


                            Intent intent = new Intent(getContext(), AuthActivity.class);
                            startActivity(intent); // Mở MainActivity
                            requireActivity().finish();

                        } else {


                            buttonSaveUsername.setEnabled(true);
                            buttonSaveUsername.setText("Lưu thông tin");

                            for (int i =0; i<response.getErrors().size(); i++)
                            {
                                if (response.getErrors().get(i).getCode().equals("INVALID_1")) {
                                    textViewUsernameErrorSetting.setText("Không đúng định dạng username");
                                    textViewUsernameErrorSetting.setVisibility(View.VISIBLE);
                                }

                                if (response.getErrors().get(i).getCode().equals("ACCOUNT_2"))
                                {
                                    textViewUsernameErrorSetting.setText("Username tồn tại");
                                    textViewUsernameErrorSetting.setVisibility(View.VISIBLE);
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