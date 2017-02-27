/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.common.view.recyclerviewpage.indicator;

import android.support.v7.widget.RecyclerView;

/**
 * A RecyclerPageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 */
public interface RecyclerPageIndicator /*extends ViewPager.OnPageChangeListener*/ {
    /**
     * Bind the indicator to a RecyclerView.
     *
     * @param view
     */
    void setViewPager(RecyclerView view);

    /**
     * Bind the indicator to a RecyclerView.
     *
     * @param view
     * @param initialPosition
     */
    void setViewPager(RecyclerView view, int initialPosition);

    /**
     * <p>Set the current page of both the ViewPager and indicator.</p>
     *
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param item
     */
    void setCurrentItem(int item);

    /**
     * Set a page change listener which will receive forwarded events.
     *
     * @param listener
     */
//    void setOnPageChangeListener(RecyclerView.OnPageChangeListener listener);

    /**
     * Notify the indicator that the fragment list has changed.
     */
    void notifyDataSetChanged();
}
