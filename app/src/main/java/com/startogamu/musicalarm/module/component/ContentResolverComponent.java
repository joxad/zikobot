package com.startogamu.musicalarm.module.component;

import com.startogamu.musicalarm.module.content_resolver.ContentResolverBaseComponent;
import com.startogamu.musicalarm.module.content_resolver.ContentResolverModule;
import com.startogamu.musicalarm.viewmodel.fragment.FragmentLocalVM;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 11/04/16.
 */
@Singleton
@Component(modules = ContentResolverModule.class)
public interface ContentResolverComponent extends ContentResolverBaseComponent{
    void init(FragmentLocalVM fragmentLocalVM);
}
