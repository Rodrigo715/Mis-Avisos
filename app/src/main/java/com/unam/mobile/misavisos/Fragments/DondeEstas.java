package com.unam.mobile.misavisos.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unam.mobile.misavisos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DondeEstas extends Fragment implements View.OnClickListener{


    @BindView(R.id.btn_escuela)
    ImageButton btn_escuela;

    @BindView(R.id.btn_trabajo)
    ImageButton btn_trabajo;

    @BindView(R.id.btn_casa)
    ImageButton btn_casa;

    @BindView(R.id.btn_fuera)
    ImageButton btn_fuera;

    @BindView(R.id.btn_edificio)
    ImageButton btn_edificio;

    @BindView(R.id.btn_personal)
    ImageButton btn_personal;

    @BindView(R.id.et_text_personal)
    EditText textoPersonal;



    private Unbinder unbinder;


    public DondeEstas() {
        // Required empty public constructor
    }

    DondeEstasSelected mCallback;


    public interface DondeEstasSelected{
        void dondeEstasSelected(int id, String textoPersonal);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback= (DondeEstasSelected) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "must implement DondeEstasSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_donde_estas, container, false);
        unbinder= ButterKnife.bind(this, rootView);
        btn_casa.setOnClickListener(this);
        btn_edificio.setOnClickListener(this);
        btn_escuela.setOnClickListener(this);
        btn_fuera.setOnClickListener(this);
        btn_trabajo.setOnClickListener(this);
        btn_personal.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btn_personal){
            textoPersonal.setText("");
            textoPersonal.setVisibility(View.VISIBLE);
            textoPersonal.setFocusableInTouchMode(true);
            textoPersonal.requestFocus();
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(textoPersonal, InputMethodManager.SHOW_IMPLICIT);

            //To handle ENTER pressed and hide keyboard
            textoPersonal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_DONE){
                        imm.hideSoftInputFromWindow(v.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        textoPersonal.setVisibility(View.GONE);
                        mCallback.dondeEstasSelected(R.id.btn_personal,textoPersonal.getText().toString()+".\n");
                        return true;
                    }
                    return false;
                }
            });

        }else{
            mCallback.dondeEstasSelected(v.getId(),"");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
