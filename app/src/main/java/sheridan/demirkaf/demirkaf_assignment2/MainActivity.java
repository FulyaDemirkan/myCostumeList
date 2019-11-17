package sheridan.demirkaf.demirkaf_assignment2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;

    private ArrayList<Costume> mCostumeList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);

        initRecyclerView();
        initRecyclerViewAdapter();

        FloatingActionButton mFab = findViewById(R.id.fabAdd);
        mFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
    }

    private void initRecyclerViewAdapter() {
        if(mAdapter == null)
        {
            mAdapter = new RecyclerViewAdapter(MainActivity.this, mCostumeList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            int index = data.getIntExtra("index", -1);
            Costume costume = data.getParcelableExtra("costume");

            if(index == -1) {
                mCostumeList.add(costume);
                mAdapter.notifyItemInserted(mCostumeList.size()-1);
            }
            else {
                mCostumeList.set(index, costume);
                mAdapter.notifyItemChanged(index);
            }
        }
    }
}
