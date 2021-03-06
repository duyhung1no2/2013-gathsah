package com.duyhung.hashtagnote;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import com.duyhung.hashtagnote.adapter.SpaceTokenizer;
import com.duyhung.hashtagnote.dao.HashtagDAO;
import com.duyhung.hashtagnote.dao.NoteDAO;
import com.duyhung.hashtagnote.model.Hashtag;
import com.duyhung.hashtagnote.model.Note;

public class NoteAddActivity extends Activity {

//	private EditText title;
	private MultiAutoCompleteTextView content;
	private Button btnHashtag;
	private NoteDAO noteDAO;
	private HashtagDAO hashtagDAO;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_add);
				
		noteDAO = new NoteDAO(this);
		hashtagDAO = new HashtagDAO(this);

//		title = (EditText) findViewById(R.id.noteTitle);
		content = (MultiAutoCompleteTextView) findViewById(R.id.noteContent);
	
		content.setAdapter(new ArrayAdapter<Hashtag>(this, android.R.layout.simple_list_item_1, hashtagDAO.getAll()));
		content.setTokenizer(new SpaceTokenizer());
		content.addTextChangedListener(watcher);

		btnHashtag = (Button) findViewById(R.id.btnHashtag);
		btnHashtag.setOnClickListener(btnHashtagOnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.note_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.action_note_save:
			noteDAO.insert(new Note(0, /*title.getText().toString()*/"", content.getText().toString(), new Date(), new Date()));
			this.finish();
			break;

		case R.id.action_note_cancel:
			if(/*!title.getText().toString().equals("") | */!content.getText().toString().equals("")){
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("Confirm discard");
				dialog.setMessage(getResources().getString(R.string.confirm_discard));
	
				dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
	
				dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
	
				dialog.show();
				break;
			}
			finish();

		default:
			break;
		}

		return true;
	}
	
	@Override
	public void onBackPressed() {
		if(/*!title.getText().toString().equals("") | */!content.getText().toString().equals("")){
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirm discard");
			dialog.setMessage(getResources().getString(R.string.confirm_discard));

			dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

			dialog.show();
			return;
		}
		finish();
	};
	
	OnClickListener btnHashtagOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int position = content.getSelectionStart();
			content.getText().insert(position, "#");
		}
	};

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

}
