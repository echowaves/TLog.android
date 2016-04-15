package com.echowaves.tlog.controller.user.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.echowaves.tlog.R;
import com.echowaves.tlog.model.TLActionCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 3/31/16.
 */


public class EmployeeActionCodesCompletionsAdapter extends ArrayAdapter<TLActionCode> {

    ArrayList<TLActionCode> actionCodes, tempActionCodes, suggestions;

    public EmployeeActionCodesCompletionsAdapter(Context context, ArrayList<TLActionCode> actionCodes) {
        super(context, 0, actionCodes);
        this.actionCodes = actionCodes;

        tempActionCodes = new ArrayList<TLActionCode>(actionCodes); // this makes the difference.
        suggestions = new ArrayList<TLActionCode>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TLActionCode actionCode = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_employee_item_action_code_completion, parent, false);
        }
        // Lookup view for data population
        TextView descr = (TextView) convertView.findViewById(R.id.user_employee_item_action_code_completions_descr);

        // Populate the data into the template view using the data object
        descr.setText(actionCode.getCode() + ":" + actionCode.getDescr());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return actionCodeFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter actionCodeFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((TLActionCode) resultValue).getCode() + ":" + ((TLActionCode) resultValue).getDescr();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (TLActionCode actionCode : tempActionCodes) {
                    if (actionCode.getCode().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            actionCode.getDescr().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(actionCode);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<TLActionCode> filterList = (ArrayList<TLActionCode>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (TLActionCode actionCode : filterList) {
                    add(actionCode);
                    notifyDataSetChanged();
                }
            }
        }
    };
}