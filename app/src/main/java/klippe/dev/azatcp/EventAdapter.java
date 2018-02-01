package klippe.dev.azatcp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Developer on 29.01.2018.
 */

public class EventAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Event> objects;

    ImageView imageView;


    EventAdapter(Context context, ArrayList<Event> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
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
            view = lInflater.inflate(R.layout.item_event_list, parent, false);
        }

        final Event event = getEvent(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvTtile)).setText(event.title);
        ((TextView) view.findViewById(R.id.tvDescription)).setText(event.descriptin);
        imageView = ((ImageView) view.findViewById(R.id.ivPicture));

        Picasso.with(ctx).load(event.img).into(imageView);

        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.checked = true;
                view.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    // товар по позиции
    Event getEvent(int position) {
        return ((Event) getItem(position));
    }

    // содержимое корзины
    ArrayList<Event> getBox() {
        ArrayList<Event> box = new ArrayList<Event>();
        for (Event p : objects) {
            // если в корзине
            if (p.checked)
                box.add(p);
        }
        return box;
    }
}