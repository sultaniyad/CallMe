package com.iyad.sultan.callme.Controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyad.sultan.callme.Model.Contacts;
import com.iyad.sultan.callme.R;

import io.realm.RealmResults;

/**
 * Created by salkhmis on 10/24/2016.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    public RealmResults<Contacts> contactsRealmResults;

   public Adapter(RealmResults<Contacts> c){
       contactsRealmResults = c;
   }
    public void setAdapterData( RealmResults<Contacts> data){
        contactsRealmResults = data;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

       myViewHolder vh =new myViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
final  Contacts contacts = contactsRealmResults.get(position);
        holder.txtName.setText(contacts.getName());
        holder.txtNumber.setText(contacts.getNumber());
    }

    @Override
    public int getItemCount() {
        return contactsRealmResults.size();
    }

    public  class myViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtNumber;

        public myViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            txtNumber = (TextView) itemView.findViewById(R.id.txt_number);
        }
    }
}
