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
public class EnCuantoPueda extends Fragment implements View.OnClickListener{

    @BindView(R.id.btn_teLlamo)
    ImageButton btn_tellamo;

    @BindView(R.id.btn_llamame)
    ImageButton btn_llamame;

    private Unbinder unbinder;

    public EnCuantoPueda() {
        // Required empty public constructor
    }

    EnCuantoPuedaSelected mCallback;

    public interface EnCuantoPuedaSelected{
        void enCuantoPuedaSelected(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback= (EnCuantoPuedaSelected) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "must implement EnCuantoPuedaSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_en_cuanto_pueda, container, false);
        unbinder= ButterKnife.bind(this, rootView);
        btn_llamame.setOnClickListener(this);
        btn_tellamo.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        mCallback.enCuantoPuedaSelected(v.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
