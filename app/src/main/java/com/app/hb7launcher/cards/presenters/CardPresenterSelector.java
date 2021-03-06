/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.app.hb7launcher.cards.presenters;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

import com.app.hb7launcher.R;
import com.app.hb7launcher.model.AppModel;
import com.app.hb7launcher.model.Card;
import com.app.hb7launcher.model.FunctionModel;

import java.util.HashMap;

/**
 * This PresenterSelector will decide what Presenter to use depending on a given card's type.
 */
public class CardPresenterSelector extends PresenterSelector {

    private final Context mContext;
    private final HashMap<Card.Type, Presenter> presenters = new HashMap<Card.Type, Presenter>();

    public CardPresenterSelector(Context context) {
        mContext = context;
    }

    @Override
    public Presenter getPresenter(Object item) {
        /*if (!(item instanceof Card)) throw new RuntimeException(
                String.format("The PresenterSelector only supports data items of type '%s'",
                        Card.class.getName()));*/

        Presenter presenter=null;
        if(item instanceof AppModel){
            presenter = new AppCardPresenter();
        }
        if(item instanceof FunctionModel){
            presenter = new FunctionCardPresenter();
        }

        return presenter;
    }

}
