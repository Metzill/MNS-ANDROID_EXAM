package com.example.mns_android_exam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AdapterQuoteList extends RecyclerView.Adapter<AdapterQuoteList.QuoteViewHolder> {

    private ArrayList<Quote> quoteList;
    private ClickQuoteListner listner;

    interface ClickQuoteListner {
        void onClickQuote(Quote quote);
    }

    public AdapterQuoteList(ArrayList<Quote> quoteList, ClickQuoteListner listner) {
        this.quoteList = quoteList;
        this.listner = listner;
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCharacterQuote;
        private TextView tvQuoteQuote;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCharacterQuote = itemView.findViewById(R.id.characterQuote);
            tvQuoteQuote = itemView.findViewById(R.id.quoteQuote);
        }

        public TextView getTvCharacterQuote() {
            return tvCharacterQuote;
        }

        public void setTvCharacterQuote(TextView tvCharacterQuote) {
            this.tvCharacterQuote = tvCharacterQuote;
        }

        public TextView getTvQuoteQuote() {
            return tvQuoteQuote;
        }

        public void setTvQuoteQuote(TextView tvQuoteQuote) {
            this.tvQuoteQuote = tvQuoteQuote;
        }
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vueItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_quote, parent, false);

        return new QuoteViewHolder(vueItem);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        Quote quote = quoteList.get(position);
        holder.getTvCharacterQuote().setText(quote.getCharacter());
        holder.getTvQuoteQuote().setText(quote.getQuote());

        holder.itemView.setOnClickListener(view -> listner.onClickQuote(quote));

    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }
}
