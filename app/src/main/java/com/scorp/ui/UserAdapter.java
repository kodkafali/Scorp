package com.scorp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.scorp.BR;
import com.scorp.R;
import com.scorp.data.Person;
import com.scorp.databinding.ListItemBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<Person> mPersonList;

    public UserAdapter(List<Person> personList) {
        this.mPersonList = personList;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person dataModel = mPersonList.get(position);
        holder.bind(dataModel);
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    public void clear(){
        mPersonList.clear();
        notifyDataSetChanged();
    }

    public void addItem(Person person){
        mPersonList.add(person);
    }

    public Object getLastID(){
        return (mPersonList == null || mPersonList.isEmpty()) ? null : mPersonList.get(getItemCount() - 1).getId();
    }

    @SuppressLint("NewApi")
    public void addAll(List<Person> personList){
        if (mPersonList != null && !mPersonList.isEmpty()){
            for (Person person : personList){
                if (mPersonList.stream().anyMatch(c -> c.getId() != person.getId()))
                    addItem(person);
            }
        }
        else
            mPersonList.addAll(personList);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ListItemBinding itemRowBinding;

        public ViewHolder(ListItemBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }
    }
}
