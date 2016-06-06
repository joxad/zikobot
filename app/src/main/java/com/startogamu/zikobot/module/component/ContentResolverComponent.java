package com.startogamu.zikobot.module.component;

import com.startogamu.zikobot.module.content_resolver.ContentResolverBaseComponent;
import com.startogamu.zikobot.module.content_resolver.ContentResolverModule;
import com.startogamu.zikobot.viewmodel.fragment.local.FragmentLocalVM;
import com.startogamu.zikobot.viewmodel.fragment.local.FragmentLocalAlbumsVM;
import com.startogamu.zikobot.viewmodel.fragment.local.FragmentLocalArtistVM;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by josh on 11/04/16.
 */
@Singleton
@Component(modules = ContentResolverModule.class)
public interface ContentResolverComponent extends ContentResolverBaseComponent{
    void init(FragmentLocalVM fragmentLocalVM);

    void init(FragmentLocalArtistVM fragmentLocalArtistVM);

    void init(FragmentLocalAlbumsVM fragmentLocalAlbumsVM);
}
