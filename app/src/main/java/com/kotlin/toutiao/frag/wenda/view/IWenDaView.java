package com.kotlin.toutiao.frag.wenda.view;

import com.kotlin.toutiao.ui.view.IView;

import java.util.List;

public interface IWenDaView extends IView {
    void onSetAdapter(List<?> list);
}
