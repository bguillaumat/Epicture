package brice_bastien.epicture;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import brice_bastien.epicture.post.CommentAdapter;

public class DialogComment extends DialogFragment {

	public CommentAdapter adapter;

	static DialogComment newInstance(CommentAdapter adapter) {
		DialogComment frag = new DialogComment();
		frag.adapter = adapter;
//		Bundle args = new Bundle();
//		args.putString("title", title);
//		frag.setArguments(args);
		return frag;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_comment, container);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		EditText mEditText = view.findViewById(R.id.edit_comment);
		getDialog().setTitle(getString(R.string.title_activity_post_comment));
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

}
