package com.piyush004.studentroom.Dashboard;

import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

import java.util.ArrayList;
import java.util.List;

public class Holder extends RecyclerView.ViewHolder  implements Filterable {

    TextView textViewTitle;
    private List<Model> exampleList;
    private List<Model> exampleListFull;

    public Holder(@NonNull View itemView) {
        super(itemView);

        this.textViewTitle = itemView.findViewById(R.id.title_card);

    }

    public void setTxtTitle(String string) {
        textViewTitle.setText(string);
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            System.out.println(constraint);
            List<Model> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Model item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
        }
    };

}
