package com.example.moment_app.ui.auth;

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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moment_app.R;
import com.example.moment_app.models.request.RegisterRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.repository.AuthenticationRepository;
import com.example.moment_app.ui.main.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private Button buttonRegister;
    private EditText editTextBirthday;

    private EditText editTextName;
    private TextView textViewNameError;

    private EditText editTextUsername;
    private TextView textViewUsernameError;

    private EditText editTextPassword;

    private TextView textViewPasswordError;

    private RadioGroup radioGroupSex;

    private AccountRepository accountRepository;

    private TextView textViewBirthdayError;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonRegister =  view.findViewById(R.id.buttonRegister);

        editTextName  =  view.findViewById(R.id.editTextName);
        textViewNameError =  view.findViewById(R.id.textViewNameError);

        editTextUsername =  view.findViewById(R.id.editTextUsernameRe);
        textViewUsernameError =  view.findViewById(R.id.textViewUsernameError);

         editTextPassword =  view.findViewById(R.id.editTextPasswordRe);

        textViewPasswordError =  view.findViewById(R.id.textViewPasswordError);

        radioGroupSex =  view.findViewById(R.id.radioGroupSex);
        textViewBirthdayError = view.findViewById(R.id.textViewBirthdayError);



        editTextBirthday = view.findViewById(R.id.editTextBirthday);

        editTextBirthday.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (datePickerView, year1, month1, dayOfMonth) -> {
                        // Định dạng: yyyy-MM-dd
                        String selectedDate = String.format("%d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        editTextBirthday.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });


        textViewNameError.setVisibility(View.GONE);
        textViewUsernameError.setVisibility(View.GONE);
        textViewPasswordError.setVisibility(View.GONE);
        textViewBirthdayError.setVisibility(View.GONE);

        editTextBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewBirthdayError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewUsernameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewNameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        accountRepository = new AccountRepository(requireContext());

        buttonRegister.setOnClickListener(v -> {

            RegisterRequest request = new RegisterRequest();
            String name = editTextName.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();
            String birthday = editTextBirthday.getText().toString().trim();

            request.setName(name);
            request.setPassword(password);
            request.setUserName(username);
            request.setBirthday(birthday);

            // Khi cần lấy giá trị giới tính:
            int selectedId = radioGroupSex.getCheckedRadioButtonId();
            String gender = ""; // Biến chứa giá trị cần trả về

            if (selectedId == R.id.radioMale) {
                gender = "male";
            } else if (selectedId == R.id.radioFemale) {
                gender = "female";
            }
            request.setSex(gender);






            accountRepository.register(request, new AccountRepository.AccountCallback() {
                @Override
                public void onSuccess(ApiResponse<AuthenticationResponse> response) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                        if (response.getStatus() == 200) {


                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MomentPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", response.getResult().getToken());
                            editor.apply(); // hoặc editor.commit();

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent); // Mở MainActivity
                            requireActivity().finish(); // Kết thúc màn hình hiện tại (đăng nhập)

                        } else {

                            for (int i =0; i<response.getErrors().size(); i++)
                            {
                                if (response.getErrors().get(i).getCode().equals("INVALID_6")) {
                                    textViewNameError.setText("Không đúng định dạng tên");
                                    textViewNameError.setVisibility(View.VISIBLE);
                                }
                                if (response.getErrors().get(i).getCode().equals("INVALID_1")) {
                                    textViewUsernameError.setText("Không đúng định dạng username");
                                    textViewUsernameError.setVisibility(View.VISIBLE);
                                }
                                if (response.getErrors().get(i).getCode().equals("INVALID_2"))
                                {
                                    textViewPasswordError.setText("Không đúng định dạng password");
                                    textViewPasswordError.setVisibility(View.VISIBLE);
                                }
                                if (response.getErrors().get(i).getCode().equals("ACCOUNT_2"))
                                {
                                    textViewUsernameError.setText("Username tồn tại");
                                    textViewUsernameError.setVisibility(View.VISIBLE);
                                }

                                if (response.getErrors().get(i).getCode().equals("INVALID_5"))
                                {
                                    textViewBirthdayError.setText("Chọn ngày");
                                    textViewBirthdayError.setVisibility(View.VISIBLE);
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