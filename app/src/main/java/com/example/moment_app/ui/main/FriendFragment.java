package com.example.moment_app.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moment_app.R;
import com.example.moment_app.adapters.FriendAdapter;
import com.example.moment_app.adapters.HomeAdapter;
import com.example.moment_app.adapters.OnFriendActionListener;
import com.example.moment_app.models.response.AccountResponse;
import com.example.moment_app.models.response.ApiResponse;
import com.example.moment_app.models.response.PhotoResponse;
import com.example.moment_app.repository.AccountRepository;
import com.example.moment_app.repository.PhotoRepository;
import com.example.moment_app.models.request.FriendInviteRequest;
import com.example.moment_app.ui.auth.AuthActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    private FriendAdapter adapter;


    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonReceivedInvites;
    private Button buttonSentInvites;

    private Button buttonFriendList;

    private AccountRepository accountRepository;

    private RecyclerView recyclerViewSearchResults;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
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
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         editTextSearch = view.findViewById(R.id.editTextSearch);
         buttonSearch = view.findViewById(R.id.buttonSearch);
         buttonReceivedInvites = view.findViewById(R.id.buttonReceivedInvites);
         buttonSentInvites = view.findViewById(R.id.buttonSentInvites);
        buttonFriendList =  view.findViewById(R.id.buttonFriendList);

        recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonFriendList.setOnClickListener(v -> {
            Fragment fr = new FriendListFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, fr) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();
        });

        buttonReceivedInvites.setOnClickListener(v -> {
            Fragment fr = new ReceivedInvitesFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, fr) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();
        });

        buttonSentInvites.setOnClickListener(v -> {
            Fragment fr = new SentInvitesFragment(); // fragment bạn muốn mở

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerMain, fr) // ID của container chứa Fragment
                    .addToBackStack(null) // cho phép quay lại bằng nút back
                    .commit();
        });

         accountRepository = new AccountRepository(requireContext());

        buttonSearch.setOnClickListener(v -> {
            String s = editTextSearch.getText().toString().trim();


            accountRepository.searchFriend(s, new AccountRepository.SearchFriendCallback() {
                @Override
                public void onSuccess(ApiResponse<List<AccountResponse>> response) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                        if (response.getStatus() == 200) {

                             adapter = new FriendAdapter(getContext(), response.getResult(), new OnFriendActionListener() {
                                @Override
                                public void onSendFriendRequest(FriendInviteRequest request, int position) {
                                    // Gửi lời mời kết bạn (gọi API từ AccountRepository chẳng hạn)
                                    Toast.makeText(getContext(), "Gửi lời mời" , Toast.LENGTH_SHORT).show();

                                    accountRepository.addFriend(request, new AccountRepository.FriendCallback() {
                                        @Override
                                        public void onSuccess(ApiResponse<AccountResponse> response) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                                if (response.getStatus() == 200) {
                                                    AccountResponse updated = adapter.getAccountList().get(position);
                                                    updated.setFriendStatus("sent"); // hoặc trạng thái phù hợp
                                                    adapter.notifyItemChanged(position); // cập nhật UI của item
                                                } else {

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


                                }

                                @Override
                                public void onCancelFriend(FriendInviteRequest request,  int position) {
                                    // Hủy kết bạn
                                    Toast.makeText(getContext(), "Hủy kết bạn" , Toast.LENGTH_SHORT).show();
                                    accountRepository.changeFriendStatus(request, new AccountRepository.FriendCallback() {
                                        @Override
                                        public void onSuccess(ApiResponse<AccountResponse> response) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                                if (response.getStatus() == 200) {
                                                    AccountResponse updated = adapter.getAccountList().get(position);
                                                    updated.setFriendStatus("none"); // hoặc trạng thái phù hợp
                                                    adapter.notifyItemChanged(position); // cập nhật UI của item
                                                } else {

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

                                }

                                @Override
                                public void onAcceptFriend(FriendInviteRequest request,  int position) {
                                    // Chấp nhận lời mời
                                    Toast.makeText(getContext(), "Chấp nhận lời mời" , Toast.LENGTH_SHORT).show();
                                    accountRepository.changeFriendStatus(request, new AccountRepository.FriendCallback() {
                                        @Override
                                        public void onSuccess(ApiResponse<AccountResponse> response) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                                if (response.getStatus() == 200) {
                                                    AccountResponse updated = adapter.getAccountList().get(position);
                                                    updated.setFriendStatus("accepted"); // hoặc trạng thái phù hợp
                                                    adapter.notifyItemChanged(position); // cập nhật UI của item
                                                } else {

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
                                }

                                @Override
                                public void onRejectFriend(FriendInviteRequest request,  int position) {
                                    // Từ chối lời mời
                                    Toast.makeText(getContext(), "Từ chối lời mời" , Toast.LENGTH_SHORT).show();
                                    accountRepository.changeFriendStatus(request, new AccountRepository.FriendCallback() {
                                        @Override
                                        public void onSuccess(ApiResponse<AccountResponse> response) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                                if (response.getStatus() == 200) {
                                                    AccountResponse updated = adapter.getAccountList().get(position);
                                                    updated.setFriendStatus("none"); // hoặc trạng thái phù hợp
                                                    adapter.notifyItemChanged(position); // cập nhật UI của item
                                                } else {

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
                                }

                                @Override
                                public void onCancelRequest(FriendInviteRequest request,  int position) {
                                    // Hủy lời mời đã gửi
                                    Toast.makeText(getContext(), "Hủy lời mời đã gửi" , Toast.LENGTH_SHORT).show();
                                    accountRepository.changeFriendStatus(request, new AccountRepository.FriendCallback() {
                                        @Override
                                        public void onSuccess(ApiResponse<AccountResponse> response) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(),  String.valueOf(response.getStatus())  , Toast.LENGTH_SHORT).show();

                                                if (response.getStatus() == 200) {
                                                    AccountResponse updated = adapter.getAccountList().get(position);
                                                    updated.setFriendStatus("none"); // hoặc trạng thái phù hợp
                                                    adapter.notifyItemChanged(position); // cập nhật UI của item
                                                } else {

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
                                }
                            });

                            recyclerViewSearchResults.setAdapter(adapter);


                        } else {

                        }
                        // TODO: Chuyển màn hình hoặc lưu token, v.v.
                    });
                }
                @Override
                public void onError(Throwable t) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("fail response"+ t.getMessage());
                    });
                }
            });

        });




    }
}