package com.scorp.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scorp.R;
import com.scorp.data.Person;
import com.scorp.databinding.FragmentUserBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserFragment extends Fragment {

    private UserViewModel mViewModel;
    private FragmentUserBinding mBinding;
    private HashMap<Integer, String> mPagingIndex = new HashMap<>();

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);

        initView();

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView(){
        mBinding.setAdapter(new UserAdapter(new ArrayList<>()));
        mBinding.setRefreshListener(this::onRefresh);
        mBinding.pnlEmpty.setOnClickListener(view -> onRefresh());

        showAndHideShimmer(true);

        Glide.with(getActivity()).load(R.drawable.scorp).into(mBinding.imgEmpty);

        mBinding.spPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);

                if (!mPagingIndex.containsKey(i)){
                    Object lastId = mBinding.getAdapter().getLastID();
                    mPagingIndex.put(i, lastId == null ? null : String.valueOf(lastId));
                }

                mBinding.getAdapter().clear();
                showAndHideShimmer(true);

                mViewModel.getPersonList(mPagingIndex.get(i)).removeObservers(getActivity());
                mViewModel.getPersonList(mPagingIndex.get(i)).observe(getActivity(), personList -> loadData(personList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Test amaçlı eklendi..
        String[] pages = {"1", "2", "3", "4", "5"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, pages);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mBinding.spPage.setAdapter(arrayAdapter);

    }

    private void loadData(List<Person> personList){
        if (personList == null){
            mBinding.pnlEmpty.setVisibility(View.VISIBLE);
            mBinding.slRefresh.setVisibility(View.GONE);
            mBinding.txtMessage.setText(getString(R.string.error_message));
        }else if (personList.isEmpty()){
            mBinding.pnlEmpty.setVisibility(View.VISIBLE);
            mBinding.slRefresh.setVisibility(View.GONE);
            mBinding.txtMessage.setText(getString(R.string.empty_message));
        }else{
            mBinding.getAdapter().addAll(personList);
            mBinding.pnlEmpty.setVisibility(View.GONE);
            mBinding.slRefresh.setVisibility(View.VISIBLE);
        }

        showAndHideShimmer(false);
        mBinding.slRefresh.setRefreshing(false);
    }

    private void showAndHideShimmer(boolean isShow){
        if (isShow){
            mBinding.shimmerViewContainer.startShimmerAnimation();
            mBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
        }
        else{
            mBinding.shimmerViewContainer.stopShimmerAnimation();
            mBinding.shimmerViewContainer.setVisibility(View.GONE);
        }
    }

    private void onRefresh(){
        mBinding.getAdapter().clear();
        showAndHideShimmer(true);

        mViewModel.getPersonList(null).removeObservers(getActivity());
        mViewModel.getPersonList(null).observe(getActivity(), this::loadData);
    }

}