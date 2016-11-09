package com.iyad.sultan.callme.Controller;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyad.sultan.callme.Model.Contacts;
import com.iyad.sultan.callme.R;

import io.realm.RealmResults;

/**
 * Created by salkhmis on 10/24/2016.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    //Interface
    public interface ItemClickCallback {
        void onImgPhoneClicked(String phoneNumber);
    }

    private ItemClickCallback itemClickCallback;
    public RealmResults<Contacts> contactsRealmResults;

    public void setItemClickedCallback(ItemClickCallback callback) {
        itemClickCallback = callback;
    }

    public Adapter(RealmResults<Contacts> c) {
        contactsRealmResults = c;
    }

    public void setAdapterData(RealmResults<Contacts> data) {
        contactsRealmResults = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        myViewHolder vh = new myViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        final Contacts contacts = contactsRealmResults.get(position);
        holder.txtName.setText(contacts.getName());
        holder.txtNumber.setText(contacts.getNumber());


    }

    @Override
    public int getItemCount() {
        return contactsRealmResults.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtNumber;
        private ImageView imgCall;

        public myViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtNumber = (TextView) itemView.findViewById(R.id.txt_number);
            imgCall = (ImageView) itemView.findViewById(R.id.icon_imageview);
            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemClickCallback.onImgPhoneClicked(contactsRealmResults.get(getAdapterPosition()).getNumber());

                }
            });
        }
    }
}
