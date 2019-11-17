package sheridan.demirkaf.demirkaf_assignment2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;

import sheridan.demirkaf.demirkaf_assignment2.utility.ImageConverter;
import sheridan.demirkaf.demirkaf_assignment2.utility.ZoomActivity;

public class CreateActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;

    private ImageView mImgCostume;
    private Uri mBitmapUri;
    private Bitmap mBitmap;
    private String mBase64Image;

    private TextInputLayout mTxtLayoutMaterial;
    private TextInputEditText mTxtName;
    private TextInputEditText mTxtMaterial;
    private TextInputEditText mTxtInstructions;

    private ChipGroup mChipGrpDifficulty;
    private ChipGroup mChipGrpMaterials;

    private FloatingActionButton mFabAdd;
    private FloatingActionButton mFabTakePicture;

    private ArrayList<String> mMaterials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        mImgCostume = findViewById(R.id.imgCostume);

        mTxtName = findViewById(R.id.txtName);
        mTxtMaterial = findViewById(R.id.txtMaterial);
        mTxtInstructions = findViewById(R.id.txtInstructions);

        mTxtLayoutMaterial = findViewById(R.id.txtLayoutMaterial);

        mChipGrpMaterials = findViewById(R.id.chipGrpMaterials);
        mChipGrpDifficulty = findViewById(R.id.chipGrpDifficulty);

        mFabAdd = findViewById(R.id.fabAdd);
        mFabTakePicture = findViewById(R.id.fabTakePicture);

        mMaterials = new ArrayList<>();

        initFabAdd();
        initFabTakePicture();
        initChipGroupMaterials();
        initImage();
    }

    private void initFabAdd() {
        mFabAdd.setOnClickListener(v -> addCostume());
    }

    private void initFabTakePicture() {
        mFabTakePicture.setOnClickListener(v -> {
            if(isReadStoragePermissionGranted()) {
                pickPicture();
            }
        });
    }

    private void initChipGroupMaterials() {
        mTxtLayoutMaterial.setEndIconOnClickListener(v -> {
            String material =  mTxtMaterial.getText() != null ? mTxtMaterial.getText().toString() : "";

            if(!material.equals("")) {
                if(mMaterials.indexOf(material) == -1) {
                    mTxtMaterial.setText("");
                    addChip(material);
                    mMaterials.add(material);
                } else {
                    mTxtMaterial.setError("This material is already added.");
                }
            }
        });
    }

    private void initImage() {
        mImgCostume.setOnClickListener(v -> ZoomActivity.zoomImageFromThumb(this, mBitmap, mImgCostume));
    }

    private Chip getChip(String text) {
        final Chip chip = new Chip(this);
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.chip));
        int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setChipBackgroundColorResource(R.color.secondaryLightColor);
        chip.setText(text);

        // remove chip
        chip.setOnCloseIconClickListener(v -> removeChip(chip));

        // edit chip
        chip.setOnClickListener(v -> editChip(chip));

        return chip;
    }

    private void addChip(String material) {
        Chip chip = getChip(material);
        mChipGrpMaterials.addView(chip);
    }

    private void editChip(Chip chip) {
        String chipText = ((ChipDrawable) chip.getChipDrawable()).getText().toString();
        mTxtMaterial.setText(chipText);
        mChipGrpMaterials.removeView(chip);
        mMaterials.remove(chipText);

    }

    private void removeChip(Chip chip) {
        int index = mMaterials.indexOf(((ChipDrawable) chip.getChipDrawable()).getText().toString());
        mMaterials.remove(index);
        mChipGrpMaterials.removeView(chip);
    }

    private void addCostume() {
        String name = mTxtName.getText() != null ? mTxtName.getText().toString() : "";

        if(name.isEmpty())
        {
            mTxtName.setError("Name cannot be empty");
        }
        else {
            Chip chip = (mChipGrpDifficulty.findViewById(mChipGrpDifficulty.getCheckedChipId()));
            String difficulty = chip != null ? ((ChipDrawable) chip.getChipDrawable()).getText().toString() : "";

            String instructions = mTxtInstructions.getText() != null? mTxtInstructions.getText().toString() : "";

            Costume costume = new Costume(name, difficulty, mMaterials, instructions);
            costume.setBase64Image(mBase64Image);

            Intent intent = getIntent();
            intent.putExtra("costume", costume);

            setResult(RESULT_OK, intent);

            Toast.makeText(this, "Costume is added successfully.", Toast.LENGTH_LONG).show();

            finish();
        }
    }

    private void pickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(this, "Device is not compatible.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if(data != null)
                mBitmapUri = data.getData();

            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mBitmapUri);
                mImgCostume.setImageBitmap(mBitmap);

                mBase64Image = ImageConverter.bitmapToBase64(mBitmap);
            } catch (IOException e) {
                Toast.makeText(this,"Error while getting the image.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //resume tasks needing this permission
                pickPicture();
            }
            else{
                Log.d("MEOW", "No permission granted");
            }
        }
    }
}
