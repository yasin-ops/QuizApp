package com.muhammadyaseenfatimamazharsarfarz.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muhammadyaseenfatimamazharsarfarz.quizapp.databinding.FragmentWalletBinding;


public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;
    FirebaseFirestore dataBase;
    User user;

    public WalletFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        dataBase = FirebaseFirestore.getInstance();
        dataBase.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                binding.currentCoins.setText(String.valueOf(user.getCoins()));
                //  binding.currentCoins.setText(user.getCoins()+"");
            }
        });
        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getCoins() > 50000) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String payPal = binding.emailBox.getText().toString();
                    WithdrawRequest request = new WithdrawRequest(uid, payPal, user.getName());
                    dataBase.collection("withdraws")
                            .document(uid)
                            .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show();

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "you need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return binding.getRoot();
    }
}