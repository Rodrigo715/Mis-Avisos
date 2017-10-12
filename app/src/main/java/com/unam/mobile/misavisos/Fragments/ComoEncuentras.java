package com.unam.mobile.misavisos.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.unam.mobile.misavisos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComoEncuentras extends Fragment  implements View.OnClickListener{

    @BindView(R.id.btn_bien)
    ImageButton btn_bien;

    @BindView(R.id.btn_auxilio)
    ImageButton btn_auxilio;

    @BindView(R.id.btn_peligro)
    ImageButton btn_peligro;


    private Unbinder unbinder;

    public ComoEncuentras() {
        // Required empty public constructor
    }

    ComoEncuentrasSelected mCallback;

    public interface ComoEncuentrasSelected {
        void comoEncuentrasSelected(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ComoEncuentrasSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement ComoEncuentrasSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_como_encuentras, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        btn_bien.setOnClickListener(this);
        btn_peligro.setOnClickListener(this);
        btn_auxilio.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        mCallback.comoEncuentrasSelected(v.getId());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
