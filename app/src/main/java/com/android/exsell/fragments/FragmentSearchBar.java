package com.android.exsell.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.exsell.R;

public class FragmentSearchBar extends Fragment {
    private SearchBarOnSearch searchBarOnSearch;
    private SearchBarBack searchBarBack;
    private EditText searchText;
    private String TAG = "FragmentSearchBar";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);
        this.searchBarOnSearch = (SearchBarOnSearch) getActivity();
        this.searchBarBack = (SearchBarBack) getActivity();
        searchText = (EditText) view.findViewById(R.id.searchText);
        view.findViewById(R.id.searchBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
                fragmentTransaction.commit();
                searchBarBack.onSearchBack();
            }
        });

        view.findViewById(R.id.searchBarSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("InsideFragmentSearchBar", "Clicked search button");
                if(searchBarOnSearch != null) {
                    searchBarOnSearch.onSearch(searchText.getText().toString());
                    searchText.setText("");
                }
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBarOnSearch.onSearch(searchText.getText().toString());
            }
        });

        return view;
    }

    public interface SearchBarOnSearch {
        void onSearch(String search);
    }

    public interface SearchBarBack {
        void onSearchBack();
    }
}