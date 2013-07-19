package com.duyhung.hashtagnote.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.duyhung.hashtagnote.R;
import com.duyhung.hashtagnote.dao.NoteDAO;
import com.duyhung.hashtagnote.model.Note;

public class NoteListAdapter extends ArrayAdapter<Note> {

	NoteDAO noteDAO;
	LayoutInflater inflater;
	List<Note> notes;
	Context mContext;
	
	public NoteListAdapter(Context context, int textViewResourceId, List<Note> objects) {
		super(context, textViewResourceId, objects);
		noteDAO = new NoteDAO(context);
		inflater = LayoutInflater.from(context);
		notes = objects;
		mContext = context;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = inflater.inflate(R.layout.listview_item_note, null);
		
//		TextView title = (TextView) view.findViewById(R.id.tvListItemTitle);
		TextView content = (TextView) view.findViewById(R.id.tvListItemContent);
		TextView createDate = (TextView) view.findViewById(R.id.tvListItemCreateDate);

		Note note = notes.get(position);
//		title.setText(cs.getString(cs.getColumnIndex(DB.NOTE_TITLE)));
		String cont = note.getContent().trim();
		cont = cont.substring(0, cont.length() > 30 ? 30 : cont.length()).replaceAll("[\n\r]", " ");
		cont = note.getContent().length() > 30 ? cont + " ..." : cont;
		
		int tagColor = mContext.getResources().getColor(R.color.main_color);
		List<String> tags = noteDAO.getTag(cont);
		cont = cont.replaceAll(" ", "&nbsp;");
		this.sortList(tags);
		for(String s : tags){
			cont = cont.replace(s, "<font color='" + tagColor + "'>" + s + "</font>");
		}
		
		content.setText(Html.fromHtml(cont));
		createDate.setText(new SimpleDateFormat("dd/MM").format(note.getCreate()));

		return view;
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
