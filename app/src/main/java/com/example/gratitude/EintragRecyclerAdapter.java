package com.example.gratitude;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EintragRecyclerAdapter extends RecyclerView.Adapter<EintragRecyclerAdapter.EintragViewHolder> {

    private List<Eintrag> eintraege = new ArrayList<>();
    private EintragRecyclerAdapter.OnEintragClickListener listener;

    /**
     * Beschreibt einen einzigen Eintrag und stellt den ClickListener zur Verfügung.
     */
    public class EintragViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView titel;

        public EintragViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.eintrag_date);
            titel = itemView.findViewById(R.id.eintrag_titel);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onEintragClick(eintraege.get(position));
                    }
                }
            });
        }
    }

    /**
     * Beim anklicken dieses Listeners, öffnet sich der Eintrag zum bearbeiten.
     */
    public interface OnEintragClickListener {
        void onEintragClick(Eintrag eintrag);
    }
    public void setOnEintragClickListener(EintragRecyclerAdapter.OnEintragClickListener listener) {
        this.listener = listener;
    }

    /**
     * Der RecyclerView wird hier Inflated.
     */
    @NonNull
    @Override
    public EintragRecyclerAdapter.EintragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_eintrag_item, parent, false);
        return new EintragRecyclerAdapter.EintragViewHolder(itemView);
    }

    /**
     * Der Name und das Datum des Eintrags wird im Holder dargestellt.
     */
    @Override
    public void onBindViewHolder(@NonNull EintragRecyclerAdapter.EintragViewHolder holder, int position) {
        Eintrag currentEintrag = eintraege.get(position);

        holder.date.setText(currentEintrag.getmDatum());
        holder.titel.setText(currentEintrag.getmTitel());
    }

    /**
     * Größe der Liste der Eintrag-Objekte wird berechnet
     */
    @Override
    public int getItemCount() {
        return eintraege.size();
    }

    /**
    * Liste der Eintrag-Objekte wird gesetzt, falls sich etwas ändert.
    */
    public void setEintraege(List<Eintrag> eintraege) {
        this.eintraege = eintraege;
        notifyDataSetChanged();
    }

}
