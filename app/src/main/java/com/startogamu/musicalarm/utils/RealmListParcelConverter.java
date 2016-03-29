package com.startogamu.musicalarm.utils;

import org.parceler.converter.CollectionParcelConverter;

import io.realm.RealmList;
import io.realm.RealmObject;

// Abstract class for working with RealmLists
public abstract class RealmListParcelConverter<T extends RealmObject> extends CollectionParcelConverter<T, RealmList<T>> {
    @Override
    public RealmList<T> createCollection() {
        return new RealmList<T>();
    }
}
