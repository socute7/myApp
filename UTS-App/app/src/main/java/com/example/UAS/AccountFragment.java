package com.example.UAS;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    TextView labelPassword, btnChangePassword;
    EditText nama, username, umur, alamat, password;
    Button btn_simpan, btn_logout, btn_ganti;
    CircleImageView imageView;
    SharedPreferences sharedPreferences;
    String imageBase64;

    Boolean isChangePassword = false;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 999;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = requireContext().getSharedPreferences("user", MODE_PRIVATE);

        nama = view.findViewById(R.id.nama);
        username = view.findViewById(R.id.username);
        umur = view.findViewById(R.id.umur);
        alamat = view.findViewById(R.id.alamat);
        btn_simpan = view.findViewById(R.id.btn_simpan);
        btn_logout = view.findViewById(R.id.btn_logout);
        imageView = view.findViewById(R.id.profile_image);
        labelPassword = view.findViewById(R.id.labelPassword);
        password = view.findViewById(R.id.password);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btn_ganti = view.findViewById(R.id.btn_ganti);
        ProgressBar pgsBar = (ProgressBar) view.findViewById(R.id.pBar);

        hidePassword();

        btn_ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Password harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    AndroidNetworking.post(ServerAPI.USER_PASSWORD + "?id=" + sharedPreferences.getInt("id", 0))
                            .addBodyParameter("password", password.getText().toString())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        int value = response.getInt("value");
                                        if (value == 1) {
                                            hidePassword();
                                            Toast.makeText(getContext(), "Berhasil!", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (isChangePassword) {
                                                showPassword();
                                            }
                                            Toast.makeText(getContext(), "Gagal!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        if (isChangePassword) {
                                            showPassword();
                                        }
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    if (isChangePassword) {
                                        showPassword();
                                    }
                                    Log.d("anError", anError.getMessage());
                                    Toast.makeText(getContext(), "Gagal!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChangePassword = true;
                showPassword();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(requireContext());
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().apply();
                startActivity(new Intent(requireContext(), LoginActivity.class));
            }
        });

        pgsBar.setVisibility(View.VISIBLE);
        AndroidNetworking.get(ServerAPI.USER_GET_BY_ID + "?id=" + sharedPreferences.getInt("id", 0))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pgsBar.setVisibility(View.GONE);
                        try {
                            JSONObject data = response.getJSONObject("data");
                            nama.setText(data.getString("nama"));
                            username.setText(data.getString("username"));
                            umur.setText(data.getString("umur"));
                            alamat.setText(data.getString("alamat"));
                            String image = data.getString("gambar");
                            if (image != "null" && !image.isEmpty()) {
                                Bitmap imageBitmap = base64ToBitmap(image);
                                imageView.setImageBitmap(imageBitmap);
                            } else {
                                imageView.setImageResource(R.drawable.avatar_svgrepo_com);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pgsBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Gagal mengambil data!", Toast.LENGTH_LONG).show();
                        Log.d("get data", anError.getMessage());
                    }
                });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgsBar.setVisibility(View.VISIBLE);
                AndroidNetworking.post(ServerAPI.USER_UPDATE + "?id=" + sharedPreferences.getInt("id", 0))
                        .addBodyParameter("nama", nama.getText().toString())
                        .addBodyParameter("username", username.getText().toString())
                        .addBodyParameter("umur", umur.getText().toString())
                        .addBodyParameter("alamat", alamat.getText().toString())
                        .addBodyParameter("gambar", imageBase64)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                pgsBar.setVisibility(View.GONE);
                                try {
                                    int value = response.getInt("value");
                                    if (value == 1) {
                                        hidePassword();
                                        isChangePassword = false;
                                        Toast.makeText(requireContext(), "Berhasil!", Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("nama", nama.getText().toString());
                                        editor.putString("username", username.getText().toString());
                                        editor.commit();
                                    } else {
                                        pgsBar.setVisibility(View.GONE);
                                        if (isChangePassword) {
                                            showPassword();
                                        }
                                        Toast.makeText(requireContext(), "Gagal!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    pgsBar.setVisibility(View.GONE);
                                    if (isChangePassword) {
                                        showPassword();
                                    }
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                pgsBar.setVisibility(View.GONE);
                                if (isChangePassword) {
                                    showPassword();
                                }
                                Log.d("anError", anError.getMessage());
                                Toast.makeText(requireContext(), "Gagal simpan!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        return view;
    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "FlagUp Requires Access to Camera.", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "FlagUp Requires Access to Your Storage.", Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(requireContext());
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                        imageBase64 = bitmapToBase64(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Bitmap selectedImageGallery = BitmapFactory.decodeFile(picturePath);
                                imageView.setImageBitmap(selectedImageGallery);
                                cursor.close();
                                imageBase64 = bitmapToBase64(selectedImageGallery);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private void hidePassword() {
        password.setVisibility(View.GONE);
        labelPassword.setVisibility(View.GONE);
        btn_ganti.setVisibility(View.GONE);
    }

    private void showPassword() {
        password.setVisibility(View.VISIBLE);
        btn_ganti.setVisibility(View.VISIBLE);
        labelPassword.setVisibility(View.VISIBLE);
    }
}
