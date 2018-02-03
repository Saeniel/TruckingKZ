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

public class EventAdapter extends BaseAdapter implements Filterable {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Event> originalEvents;
    ArrayList<Event> filteredEvents;
    ImageView imageView;

    private ItemFilter mFilter = new ItemFilter();

    EventAdapter(Context context, ArrayList<Event> products) {
        ctx = context;
        originalEvents = products;
        filteredEvents = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // кол-во элементов
    @Override
    public int getCount() {
        return filteredEvents.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return filteredEvents.get(position);
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
        ((TextView) view.findViewById(R.id.tvDate)).setText(event.date);
        ((TextView) view.findViewById(R.id.tvCategory)).setText(event.category);
        ((TextView) view.findViewById(R.id.tvPlace)).setText(event.place);
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
        for (Event p : originalEvents) {
            // если в корзине
            if (p.checked)
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

            final List<Event> list = originalEvents;

            int count = list.size();
            final ArrayList<Event> nlist = new ArrayList<Event>(count);

            String filterableString ;
            String filterableString2 ;
            String filterableString3 ;
            String filterableString4 ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).title;
                filterableString2 = list.get(i).category;
                filterableString3 = list.get(i).place;
                filterableString4 = list.get(i).descriptin;
                if (filterableString.toLowerCase().contains(filterString)||
                        filterableString2.toLowerCase().contains(filterString)||
                        filterableString3.toLowerCase().contains(filterString)||
                        filterableString4.toLowerCase().contains(filterString)) {
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
            filteredEvents = (ArrayList<Event>) results.values;
            notifyDataSetChanged();
        }

    }
    }

