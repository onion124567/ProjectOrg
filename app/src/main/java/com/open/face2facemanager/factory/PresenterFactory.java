package com.open.face2facemanager.factory;


import com.open.face2facemanager.presenter.Presenter;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}
