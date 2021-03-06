package brice_bastien.epicture;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogSort extends DialogFragment {

	public MyPostsRecyclerViewAdapter adapter;

	static DialogSort newInstance(MyPostsRecyclerViewAdapter adapter) {
		DialogSort frag = new DialogSort();
		frag.adapter = adapter;
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_sort, container);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		RadioButton newer = view.findViewById(R.id.sort_newer);
		newer.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.orderByNewest();
			}
		});

		RadioButton older = view.findViewById(R.id.sort_older);
		older.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.orderByOldest();
			}
		});

		RadioButton most_view = view.findViewById(R.id.sort_most_view);
		most_view.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.orderByMostView();
			}
		});

		RadioButton most_like = view.findViewById(R.id.sort_like);
		most_like.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.orderByUps();
			}
		});

		RadioButton username = view.findViewById(R.id.sort_username);
		username.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.orderByName();
			}
		});


	}
}
