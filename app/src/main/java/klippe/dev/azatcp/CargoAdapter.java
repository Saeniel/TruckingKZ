package klippe.dev.azatcp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Developer on 29.01.2018.
 */

public class CargoAdapter extends BaseAdapter implements Filterable {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Cargo> originalCargos;
    ArrayList<Cargo> filteredCargos;
    ImageView imageView;

    private ItemFilter mFilter = new ItemFilter();

    CargoAdapter(Context context, ArrayList<Cargo> products) {
        ctx = context;
        originalCargos = products;
        filteredCargos = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // кол-во элементов
    @Override
    public int getCount() {
        return filteredCargos.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return filteredCargos.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_cargo_list, parent, false);
        }
        final Cargo cargo = getCargo(position);

        ((TextView) view.findViewById(R.id.tvTitleFromTo)).setText(cargo.from + "-" + cargo.to);
        ((TextView) view.findViewById(R.id.tvComment)).setText(cargo.comment);
        ((TextView) view.findViewById(R.id.tvWhen)).setText(cargo.when);
        ((TextView) view.findViewById(R.id.tvPrice)).setText(cargo.price);
        ((TextView) view.findViewById(R.id.tvMachineType)).setText(cargo.machineType);
        imageView = ((ImageView) view.findViewById(R.id.ivPicture));

        Picasso.with(ctx).load(cargo.img).into(imageView);

        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargo.isChecked = true;
                view.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    // товар по позиции
    Cargo getCargo(int position) {
        return ((Cargo) getItem(position));
    }

    // содержимое корзины
    ArrayList<Cargo> getBox() {
        ArrayList<Cargo> box = new ArrayList<Cargo>();
        for (Cargo p : originalCargos) {
            // если в корзине
            if (p.isChecked)
                box.add(p);
        }
        return box;
    }

    //Фильтр даннных
    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Cargo> list = originalCargos;

            int count = list.size();
            final ArrayList<Cargo> nlist = new ArrayList<Cargo>(count);

            String filterableString ;
            String filterableString2 ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).from;
                filterableString2 = list.get(i).to;
                if (filterableString.toLowerCase().contains(filterString)||
                        filterableString2.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredCargos = (ArrayList<Cargo>) results.values;
            notifyDataSetChanged();
        }

    }
}