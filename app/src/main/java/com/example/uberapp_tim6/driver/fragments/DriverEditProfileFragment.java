package com.example.uberapp_tim6.driver.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.ChangePasswordDTO;
import com.example.uberapp_tim6.DTOS.DriverInfoDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.driver.DriverMainActivity;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.FragmentTransition;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverEditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverEditProfileFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private UserInfoDTO driver;

    private static final String ARG_DRIVER = "arg_driver";

    private static final int GALLERY_REQUEST_CODE = 901;
    ImageView img;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ActivityResultLauncher<Intent> someActivityResultLauncher;

    public DriverEditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverEditProfileFragment newInstance(UserInfoDTO d) {
        DriverEditProfileFragment fragment = new DriverEditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                        Intent data = result.getData();
                        Glide.with(requireContext()).load(data.getData()).into(img);
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back = view.findViewById(R.id.edit_driver_back);
        DriverMainActivity dvm = (DriverMainActivity) this.getActivity();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(DriverProfileFragment.newInstance(driver),dvm,false,R.id.mainContent);
            }
        });
        Button submit = view.findViewById(R.id.submit_edit);
        img = view.findViewById(R.id.profileIcon);
        Glide.with(getContext()).load(driver.getProfilePicture()).into(img);
        TextView firstName = view.findViewById(R.id.nameValue);
        TextView lastName = view.findViewById(R.id.surnameValue);
        TextView Username = view.findViewById(R.id.usernameValue);
        TextView PhoneNumber = view.findViewById(R.id.phoneNumberValue);
        TextView Address = view.findViewById(R.id.addressValue);
        TextView errorView = view.findViewById(R.id.addressError);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult();
            }
        });

        firstName.setText(driver.getName());
        lastName.setText(driver.getSurname());
        Username.setText(driver.getEmail());
        PhoneNumber.setText(driver.getTelephoneNumber());
        Address.setText(driver.getAddress());

        TextView changePasswordText = view.findViewById(R.id.changePasswordText);
        LayoutInflater inflater = getLayoutInflater();
        View changePasswordDialogView = inflater.inflate(R.layout.change_password_dialog, null);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this.getContext());
        builder2.setView(changePasswordDialogView);
        AlertDialog Dialog = builder2.create();
        Dialog.setCancelable(false);
        TextView errorChangePassword = changePasswordDialogView.findViewById(R.id.errorChangePassword);
        EditText oldPassword = changePasswordDialogView.findViewById(R.id.password);
        EditText newPassword = changePasswordDialogView.findViewById(R.id.new_password);
        Button cancelButton = changePasswordDialogView.findViewById(R.id.btn_cancel);
        Button submitPasswordButton = changePasswordDialogView.findViewById(R.id.btn_ok);


        submitPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldPassword.getText().length() == 0) {
                    oldPassword.setError("Error: Field old password cannot be empty!");
                    return;                }
                if (newPassword.getText().length() == 0) {
                    newPassword.setError("Error: Field new password cannot be empty!");
                    return;
                }

                if (newPassword.getText().length() < 6) {
                    newPassword.setError("Error: New password needs to be at least 6 characters!");
                    return;
                }
                ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(newPassword.getText().toString(),oldPassword.getText().toString());
                String id = requireActivity().getApplicationContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE).getString("id","0");
                Call<ResponseBody> call = ServiceUtils.userService.changeUserPassword(id,changePasswordDTO);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Success");
                            builder.setMessage("Successfuly updated password");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Dialog.dismiss();
                        }
                        else if(response.code() == 400){
                            errorChangePassword.setVisibility(View.VISIBLE);
                            errorChangePassword.setText("Error: Old password doesnt match!");
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Error");
                            builder.setMessage("Something went wrong when updating your profile!");
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });



        String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String firstNameRegex = "^\\p{Lu}[\\p{L}]*";
        String lastNameRegex = "^\\p{Lu}[\\p{L}]*";
        String phoneNumberRegex = "^[0-9+].{8,11}$";
        String addressRegex = "[\\p{L}\\p{N} ,]*";

        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (Username.getText().length() == 0) {
                    Username.setError("Error: Field Email cannot be empty!");
                    return;
                }
                if (firstName.getText().length() == 0) {
                    firstName.setError("Error: Field Name cannot be empty!");
                    return;
                }
                if (lastName.getText().length() == 0) {
                    lastName.setError("Error: Field Last Name cannot be empty!");
                    return;
                }
                if (PhoneNumber.getText().length() == 0) {
                    PhoneNumber.setError("Error: Field Phone Number cannot be empty!");
                    return;
                }
                if (Address.getText().length() == 0) {
                    Address.setError("Error: Field Address cannot be empty!");
                    return;
                }
                String email = Username.getText().toString();
                String name =  firstName.getText().toString();
                String surname = lastName.getText().toString();
                String phoneNumber = PhoneNumber.getText().toString();
                String address =  Address.getText().toString();

                Pattern emailNamePattern = Pattern.compile(emailRegex);
                Matcher emailNameMatcher = emailNamePattern.matcher(email);
                if (!emailNameMatcher.find()) {
                    Username.setError("Error: Email not in correct format!");
                    return;
                }
                Pattern firstNamePattern = Pattern.compile(firstNameRegex,Pattern.UNICODE_CASE);
                Matcher firstNameMatcher = firstNamePattern.matcher(name);
                if (!firstNameMatcher.find()) {
                    firstName.setError("Error: Name must start with uppercase!");
                    return;
                }
                Pattern surnameNamePattern = Pattern.compile(lastNameRegex,Pattern.UNICODE_CASE);
                Matcher surnameNameMatcher = surnameNamePattern.matcher(surname);
                if (!surnameNameMatcher.find()) {
                    lastName.setError("Error: Last name must start with uppercase!");
                    return;
                }
                Pattern phoneNumberNamePattern = Pattern.compile(phoneNumberRegex);
                Matcher phoneNumberNameMatcher = phoneNumberNamePattern.matcher(phoneNumber);
                if (!phoneNumberNameMatcher.find()) {
                    PhoneNumber.setError("Error: Phone number is in incorrect format!");
                    return;
                }
                Pattern addressNamePattern = Pattern.compile(addressRegex,Pattern.UNICODE_CASE);
                Matcher addressNameMatcher = addressNamePattern.matcher(address);
                if (!addressNameMatcher.find()) {
                    Address.setError("Error: Address is in incorrect format!");
                    return;
                }

                String id = requireActivity().getApplicationContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE).getString("id","0");
                DriverInfoDTO driverInfoDTO = new DriverInfoDTO();
                driverInfoDTO.setEmail(email);
                driverInfoDTO.setId(Integer.parseInt(id));
                driverInfoDTO.setAddress(address);
                driverInfoDTO.setName(name);
                driverInfoDTO.setSurname(surname);
                Bitmap bitmap = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                img.draw(canvas);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageBytes = stream.toByteArray();
                String base64String = "data:image/jpeg;base64,"+Base64.encodeToString(imageBytes, Base64.DEFAULT);
                driverInfoDTO.setTelephoneNumber(phoneNumber);
                driverInfoDTO.setProfilePicture(base64String);
                Call<ResponseBody> call = ServiceUtils.driverService.postDriverProfileChanges(driverInfoDTO);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Success");
                            builder.setMessage("Successfuly sent profile update request");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            FragmentTransition.to(DriverProfileFragment.newInstance(driver),dvm,false,R.id.mainContent);

                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Error");
                            builder.setMessage("Something went wrong when updating your profile!");
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.dismiss();
            }
        });

        changePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.show();
            }
        });
    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.getActivity().setResult(GALLERY_REQUEST_CODE,intent);
        someActivityResultLauncher.launch(intent);
    }


}