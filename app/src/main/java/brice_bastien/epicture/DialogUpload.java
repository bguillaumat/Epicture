package brice_bastien.epicture;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import brice_bastien.epicture.ImgurApi.ImgurApi;

public class DialogUpload extends DialogFragment {
	public MyPostsRecyclerViewAdapter adapter;
	public ImgurApi imgurApi;


	EditText title;
	EditText description;
	public Uri img;

	static DialogUpload newInstance(MyPostsRecyclerViewAdapter adapter, Uri image, ImgurApi imgurApi) {
		DialogUpload frag = new DialogUpload();
		frag.adapter = adapter;
		frag.img = image;
		frag.imgurApi = imgurApi;
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_upload, container);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		title = view.findViewById(R.id.uploadPhotoTitle);

		description = view.findViewById(R.id.uploadPhotoDescription);

		Button validate = view.findViewById(R.id.uploadButtonFinal);
		validate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uploadPhoto();
			}
		});
	}

	private void uploadPhoto() {
		if (title.getText().toString().isEmpty())
			Toast.makeText(getContext(), "Your photo need a title", Toast.LENGTH_SHORT).show();
		else {
			imgurApi.uploadImg(img, title.getText().toString(), description.getText().toString());
			dismiss();
		}
	}
}
