package klippe.dev.truckkz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
    Context context;
    LayoutInflater lInflater;
    List<Cargo> originalCargos;
    List<Cargo> filteredCargos;
    ImageView imageView;


    private ItemFilterFrom mFilter = new ItemFilterFrom();

    private boolean isSelected = false;

    CargoAdapter(Context context, boolean isSelected) {
        this.context = context;
        this.isSelected = isSelected;
        originalCargos = isSelected ? CargoStorage.selectedCargos : CargoStorage.notSelectedCargos;
        filteredCargos = originalCargos;
        lInflater = (LayoutInflater) this.context
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

        ((TextView) view.findViewById(R.id.tvTitleFromTo)).setText("Откуда: " + cargo.from + "\nКуда: " + cargo.to);
        ((TextView) view.findViewById(R.id.tvComment)).setText(cargo.comment);
        ((TextView) view.findViewById(R.id.tvWhen)).setText(cargo.when);

        if (isSelected) {
            ((TextView) view.findViewById(R.id.tvPrice)).setText(cargo.priceTemp + " руб, заказано " +
                    Integer.toString(cargo.priceTemp/Integer.parseInt(cargo.price)) + " кг");
        } else {
            ((TextView) view.findViewById(R.id.tvPrice)).setText(cargo.price + " руб/кг");
        }

        ((TextView) view.findViewById(R.id.tvMachineType)).setText(cargo.machineType);
        imageView = ((ImageView) view.findViewById(R.id.ivPicture));

        Picasso.with(context).load(cargo.img).into(imageView);

        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!isSelected) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Вес");
                    alert.setMessage("Введите количество кг");

                    final EditText input = new EditText(context);
                    // ввод чисел
                    input.setInputType(2);

                    alert.setView(input);
                    alert.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            cargo.priceTemp = Integer.parseInt(cargo.price) *
                                    Integer.parseInt(input.getText().toString());
                            CargoStorage.notSelectedCargos.remove(cargo);
                            CargoStorage.selectedCargos.add(cargo);
                            notifyDataSetChanged();
                        }
                    });

                    final AlertDialog dialog = alert.create();
                    dialog.show();

                } else {
                    CargoStorage.selectedCargos.remove(cargo);
                    CargoStorage.notSelectedCargos.add(cargo);
                    notifyDataSetChanged();
                }
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

    class ItemFilterFrom extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Cargo> list = originalCargos;

            int count = list.size();
            final ArrayList<Cargo> nlist = new ArrayList<Cargo>(count);

            String filterableStringFrom;
            String filterableStringTo;

            for (int i = 0; i < count; i++) {
                filterableStringFrom = list.get(i).from;
                filterableStringTo = list.get(i).to;
                if (filterableStringFrom.toLowerCase().contains(filterString) ||
                        filterableStringTo.toLowerCase().contains(filterableStringFrom)) {
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