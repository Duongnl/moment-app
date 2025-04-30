package com.example.moment_app.ui.auth;

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
import com.example.moment_app.models.request.AuthenticationRequest;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.AuthenticationResponse;
import com.example.moment_app.repository.AuthenticationRepository;
import com.example.moment_app.ui.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editTextUsername;
    private  EditText editTextPassword;
    private Button buttonLogin;

    private TextView textViewNotiLoginFail;

    private AuthenticationRepository authenticationRepository;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewNotiLoginFail = view.findViewById(R.id.textViewNotiLoginFail);
        textViewNotiLoginFail.setVisibility(View.GONE);

        editTextUsername =  view.findViewById(R.id.editTextUsername);
        editTextPassword =  view.findViewById(R.id.editTextPassword);
        buttonLogin =  view.findViewById(R.id.buttonLogin);

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewNotiLoginFail.setVisibility(View.GONE);
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
                textViewNotiLoginFail.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        authenticationRepository = new AuthenticationRepository(requireContext());

        // Lắng nghe sự kiện click
        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            AuthenticationRequest request = new AuthenticationRequest(username, password);

            authenticationRepository.authenticate(request, new AuthenticationRepository.AuthenticationCallback() {
                @Override
                public void onSuccess(ApiResponse<AuthenticationResponse> response) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                        if (response.getStatus() == 200) {
                            textViewNotiLoginFail.setVisibility(View.GONE);
                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MomentPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", response.getResult().getToken());
                            editor.apply(); // hoặc editor.commit();

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent); // Mở MainActivity
                            requireActivity().finish(); // Kết thúc màn hình hiện tại (đăng nhập)
                        } else {
                            textViewNotiLoginFail.setVisibility(View.VISIBLE);
                        }



                        // TODO: Chuyển màn hình hoặc lưu token, v.v.
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