/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class CategoriesAdapter extends BaseAdapter {

    private static final String TAG = CategoriesAdapter.class.getSimpleName();
    private List<String> mCategories;
    private final Context mContext;



    public CategoriesAdapter(Context context, List<String> categories) {
        if (mCategories == null) {
            if (ServSimplesAppLogger.ISLOGABLE) {
                ServSimplesAppLogger.e(TAG, "can't set adapter");
            }
        }
        this.mContext = context;
        this.mCategories = categories;
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Object getItem(int i) {
        return mCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        CategoriesAdapter.ViewHolder holder;
        if (view == null) {
            v =  LayoutInflater.from(mContext)
                    .inflate(R.layout.categories_drowpdown_item, viewGroup, false);
            holder = new CategoriesAdapter.ViewHolder();
            holder.categoryName = v.findViewById(R.id.categories_dropdown_item_name);
            v.setTag(holder);
        } else {
            v = view;
            holder = (CategoriesAdapter.ViewHolder) view.getTag();
        }
        holder.categoryName.setText(mCategories.get(i));
        return v;
    }

    private static class ViewHolder {
        private TextView categoryName;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}