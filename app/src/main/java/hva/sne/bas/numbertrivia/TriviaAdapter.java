package hva.sne.bas.numbertrivia;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by basb on 4-2-2018.
 */

public class TriviaAdapter extends RecyclerView.Adapter<TriviaAdapter.ViewHolder> {
    private List<Trivia> values;

    public TriviaAdapter(List<Trivia> triviaDataset) {
        values = triviaDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.trivia_row_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.trivia_row_item_reversed, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String number = values.get(position).getNumber();
        holder.number.setText(number);
        final String text = values.get(position).getText();
        holder.description.setText(text);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number;
        public TextView description;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            number = (TextView) view.findViewById(R.id.number_textview);
            description = (TextView) view.findViewById(R.id.text_textview);
        }
    }
}
