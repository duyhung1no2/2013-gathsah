package com.duyhung.hashtagnote;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.duyhung.hashtagnote.dao.DB;
import com.duyhung.hashtagnote.dao.NoteDAO;
import com.duyhung.hashtagnote.model.Note;

public class NoteDetailActivity extends Activity {
	
	private NoteDAO noteDAO;
	
	private TextView /*title,*/ tvContent, tvDate;
	private Note currentNote;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_detail);
		
		ActionBar bar = getActionBar();
		bar.setHomeButtonEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);     

		noteDAO = new NoteDAO(this);
		
//		title = (TextView) findViewById(R.id.tvNoteTitle);
		tvContent = (TextView) findViewById(R.id.tvNoteContent);
		tvDate = (TextView) findViewById(R.id.tvNoteModifyDate);
		
		currentNote = (Note) getIntent().getSerializableExtra(DB.TABLE_NOTE);
		displayNoteInfo(currentNote);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.note_detail, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case R.id.action_note_edit:
			Intent i = new Intent(getApplicationContext(), NoteEditActivity.class);
			i.putExtra(DB.TABLE_NOTE, currentNote);
			startActivityForResult(i, 1);
			break;
			
		case R.id.action_note_delete:
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Confirm delete");
			dialog.setMessage(getResources().getString(R.string.confirm_delete));

			dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					noteDAO.delete(currentNote);
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
			
		default:
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		currentNote = noteDAO.get(currentNote.getId());

		displayNoteInfo(currentNote);
	}
	
	private void displayNoteInfo(Note note){
//		title.setText(currentNote.getTitle());
		String cont = note.getContent();
		String updatedDate = getResources().getString(R.string.last_updated) + " " + DB.DISPLAY_DATE_FORMAT.format(note.getModify());
		
		int tagColor = getResources().getColor(R.color.main_color);
		List<String> tags = noteDAO.getTag(cont);
		cont = cont.replaceAll("\n", "<br/>");
		cont = cont.replaceAll(" ", "&nbsp;");
		sortList(tags);
		for(String s : tags){
			cont = cont.replace(s, "<font color='" + tagColor + "'>" + s + "</font>");
		}
		
		tvDate.setText(updatedDate);
		tvContent.setText(Html.fromHtml(cont));
		
	}
	
	public void sortList(List<String> list){
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.size()-1; j++) {
				if(list.get(j).length() < list.get(j+1).length()){
					String tmp = list.get(j);
					list.set(j, list.get(j+1));
					list.set(j+1, tmp);
				}
			}
		}
	}

}
