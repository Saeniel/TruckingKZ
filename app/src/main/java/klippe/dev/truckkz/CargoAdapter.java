package klippe.dev.truckkz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
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

public class CargoAdapter extends RecyclerView.Adapter<CargoAdapter.CargoViewHolder> {
    Context context;
    List<Cargo> originalCargos;

    CargoAdapter(Context context) {
        this.context = context;
        originalCargos = new ArrayList<>();
    }

    @Override
    public CargoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CargoViewHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_cargo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(CargoViewHolder holder, int position) {

        final Cargo cargo = originalCargos.get(position);

        holder.tvTitleFromTo.setText("Откуда: " + cargo.from + "\nКуда: " + cargo.to);
        holder.tvComment.setText(cargo.comment);
        holder.tvWhen.setText(cargo.when);

        if (cargo.isChecked) {
            holder.tvPrice.setText(cargo.priceTemp + " руб, заказано " +
                    Integer.toString(cargo.priceTemp / Integer.parseInt(cargo.price)) + " кг");
        } else {
            holder.tvPrice.setText(cargo.price + " руб/кг");
        }

        holder.tvMachineType.setText(cargo.machineType);

        holder.btnAdd.setRotation(cargo.isChecked ? 45 : 0);

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (!cargo.isChecked) {

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
                            cargo.isChecked = true;
                            CargoStorage.notSelectedCargos.remove(cargo);
                            originalCargos.remove(cargo);
                            CargoStorage.selectedCargos.add(cargo);
                            notifyDataSetChanged();
                        }
                    });

                    final AlertDialog dialog = alert.create();
                    dialog.show();
                } else {
                    cargo.isChecked = false;
                    CargoStorage.notSelectedCargos.add(cargo);
                    originalCargos.remove(cargo);
                    CargoStorage.selectedCargos.remove(cargo);
                    notifyDataSetChanged();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return originalCargos.size();
    }

    class CargoViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitleFromTo, tvComment, tvPrice, tvWhen, tvMachineType;
        public ImageView ivPicture, btnAdd;

        public CargoViewHolder(View itemView) {
            super(itemView);
            tvTitleFromTo = itemView.findViewById(R.id.tvTitleFromTo);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvWhen = itemView.findViewById(R.id.tvWhen);
            tvMachineType = itemView.findViewById(R.id.tvMachineType);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }


   /* // пункт списка
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
        //imageView = ((ImageView) view.findViewById(R.id.ivPicture));

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
    }*/

   public void updateList(List<Cargo> cargos) {
       originalCargos.clear();
       originalCargos.addAll(cargos);
       notifyDataSetChanged();
   }

}