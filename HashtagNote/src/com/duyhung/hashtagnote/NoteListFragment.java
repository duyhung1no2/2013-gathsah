package com.duyhung.hashtagnote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.duyhung.hashtagnote.adapter.NoteListAdapter;
import com.duyhung.hashtagnote.adapter.SeparatedListAdapter;
import com.duyhung.hashtagnote.dao.DB;
import com.duyhung.hashtagnote.dao.NoteDAO;
import com.duyhung.hashtagnote.model.Note;

public class NoteListFragment extends Fragment {

	private ListView listViewNote;

	private NoteDAO noteDAO;

	private String currentSearch = "";
	private List<List<Note>> multiListNote;
	private int currentPositionNote = 0;
	
	private NoteContextItemSelectedListener handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_note_list, container, false);

		noteDAO = new NoteDAO(getActivity());

		listViewNote = (ListView) view.findViewById(R.id.listViewNote);

		loadListNote();
		registerForContextMenu(listViewNote);
		
		handler = (NoteContextItemSelectedListener) getActivity();

		return view;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.note_detail, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		this.onPause();

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		final Note selectedNote = (Note) listViewNote.getAdapter().getItem(info.position);

		switch (item.getItemId()) {
		case R.id.action_note_edit:
			Intent i = new Intent(getActivity(), NoteEditActivity.class);
			i.putExtra(DB.TABLE_NOTE, selectedNote);
			startActivityForResult(i, 1);
			break;

		case R.id.action_note_delete:
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("Confirm delete");
			dialog.setMessage(getResources().getString(R.string.confirm_delete));

			dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					noteDAO.delete(selectedNote);
					loadListNote();
					handler.refreshList();
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
	public void onPause() {
		super.onPause();
		currentPositionNote = listViewNote.getFirstVisiblePosition();
	}

	@Override
	public void onResume() {
		super.onResume();
		listViewNote.setSelection(currentPositionNote);
	}

	private OnItemClickListener onNoteItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			Note note = (Note) listView.getAdapter().getItem(position);
			Intent i = new Intent(getActivity(), NoteDetailActivity.class);
			i.putExtra(DB.TABLE_NOTE, note);
			startActivityForResult(i, 1);

		}
	};

	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
	@SuppressWarnings({ "deprecation" })
	private void loadListNote() {
		if (currentSearch.equals("") || currentSearch == null) {
			multiListNote = noteDAO.getMultiList();
		} else {
			multiListNote = noteDAO.searchNote(currentSearch);
		}

		SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity(), R.layout.listview_header_note);

		for (List<Note> list : multiListNote) {
			int month = list.size() > 0 ? list.get(0).getCreate().getMonth() + 1 : 0;
			int year = list.size() > 0 ? list.get(0).getCreate().getYear() : 0;

			String caption = "";
			if (currentSearch.equals("") || currentSearch == null) {
				caption = new SimpleDateFormat("MMM yyyy").format(new Date(year, month, 0)) + " (" + list.size() + ")";
			} else {
				caption = "Notes contain tag: #" + currentSearch;
			}

			adapter.addSection(caption.toUpperCase(), new NoteListAdapter(getActivity(), R.layout.listview_item_note, list));
		}

		listViewNote.setAdapter(adapter);
		listViewNote.setOnItemClickListener(onNoteItemClickListener);

		this.onResume();
	}
	
	public void search(String tag){
		currentSearch = tag;
		loadListNote();
				
	}
	
	interface NoteContextItemSelectedListener{
		void refreshList();
	}

}
