package nation.procrastination.medicineapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mathieu Morin on 11/17/2017.
 */

public class CustomListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listHeaders;
    private HashMap<String, List<String>> listBody;

    public CustomListAdapter(Context context, List<String> listHeaders, HashMap<String, List<String>> listBody) {
        this.context = context;
        this.listHeaders = listHeaders;
        this.listBody = listBody;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.listBody.get(this.listHeaders.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView listBodyText = (TextView) convertView.findViewById(R.id.listBodyText);
        listBodyText.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.listBody.get(this.listHeaders.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.listHeaders.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listHeaders.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_header, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listHeaderText);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
