package com.startogamu.zikobot.home.reco;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.joxad.easydatabinding.fragment.v4.FragmentBaseVM;
import com.joxad.zikobot.data.module.recommandations.Reco;
import com.startogamu.zikobot.BR;
import com.startogamu.zikobot.R;
import com.startogamu.zikobot.databinding.RecoFragmentBinding;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Generated by generator-android-template
 */
public class RecoFragmentVM extends FragmentBaseVM<RecoFragment, RecoFragmentBinding> {

    private static final String TAG = RecoFragmentVM.class.getSimpleName();

    public ObservableArrayList<RecoCardVM> items;
    public ItemBinding<RecoCardVM> itemBinding = ItemBinding.of(BR.recoCardVM, R.layout.news_card_template);

    /***
     * @param fragment
     * @param binding
     */
    public RecoFragmentVM(RecoFragment fragment, RecoFragmentBinding binding, @Nullable Bundle savedInstance) {
        super(fragment, binding, savedInstance);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        items = new ObservableArrayList<>();
        items.add(new RecoCardVM(fragment.getContext(), new Reco("Par rapport à tes favoris :")));
        items.add(new RecoCardVM(fragment.getContext(), new Reco("Une autre proposition :")));
        items.add(new RecoCardVM(fragment.getContext(), new Reco("Pourquoi pas ça aussi :")));
        items.add(new RecoCardVM(fragment.getContext(), new Reco("Une dernière pour la route :")));
        /*{
            @Override
            public RecoCardContentVM getRecoCardContentVM() {
                return new ArtistSimilarRecoCardContentVM(fragment.getContext());
            }
        });*/

        for (RecoCardVM recoCardVM : items)
            recoCardVM.onCreate();
    }
}
