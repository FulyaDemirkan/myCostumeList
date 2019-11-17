package sheridan.demirkaf.demirkaf_assignment2.utility;

import android.app.Activity;
import android.util.TypedValue;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import sheridan.demirkaf.demirkaf_assignment2.R;

public class ChipHandler {

    private Activity activity;
    private ChipGroup chipGroup;
    private TextInputEditText txtMaterial;

    public ChipHandler(Activity activity) {
        this.activity = activity;
        chipGroup = activity.findViewById(R.id.chipGrpMaterials);
        txtMaterial = activity.findViewById(R.id.txtMaterial);
    }

    public Chip getChip(String text) {
        final Chip chip = new Chip(activity);
        chip.setChipDrawable(ChipDrawable.createFromResource(activity, R.xml.chip));
        int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, activity.getResources().getDisplayMetrics());
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setChipBackgroundColorResource(R.color.secondaryLightColor);
        chip.setText(text);

        // remove chip
        chip.setOnCloseIconClickListener(v -> removeChip(chip));

        // edit chip
        chip.setOnClickListener(v -> editChip(chip));

        return chip;
    }

    public void addChip(String material) {
        Chip chip = getChip(material);
        chipGroup.addView(chip);
    }

    public void editChip(Chip chip) {
        String chipText = ((ChipDrawable) chip.getChipDrawable()).getText().toString();
        txtMaterial.setText(chipText);
        removeChip(chip);
    }

    public void removeChip(Chip chip) {
        chipGroup.removeView(chip);
    }
}
