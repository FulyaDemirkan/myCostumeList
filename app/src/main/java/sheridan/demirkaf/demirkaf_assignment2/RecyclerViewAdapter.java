package sheridan.demirkaf.demirkaf_assignment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import sheridan.demirkaf.demirkaf_assignment2.utility.ImageConverter;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Costume> mCostumeList;
    private static final int REQUEST_CODE = 1;

    RecyclerViewAdapter(Context mContext, List<Costume> mCostumeList) {
        this.mContext = mContext;
        this.mCostumeList = mCostumeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_costume_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Costume costume = mCostumeList.get(i);
        viewHolder.txtName.setText(costume.getName());

        if(!costume.getBase64Image().equals("")) {
            Bitmap bitmap = ImageConverter.base64ToBitmap(costume.getBase64Image());
            viewHolder.imageView.setImageBitmap(bitmap);
        }
        else {
            viewHolder.imageView.setImageResource(R.drawable.image_placeholder);
        }

        viewHolder.fab.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("costume", costume);
            intent.putExtra("index", i);
            ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    public int getItemCount() {
        return mCostumeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imageView;
        FloatingActionButton fab;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.listName);
            imageView = itemView.findViewById(R.id.listImageView);
            fab = itemView.findViewById(R.id.fab);
        }
    }
}
