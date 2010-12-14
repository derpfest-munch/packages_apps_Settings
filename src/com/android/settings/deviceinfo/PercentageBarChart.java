/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.settings.deviceinfo;

import com.android.settings.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;

/**
 * 
 */
public class PercentageBarChart extends View {
    private final Paint mEmptyPaint = new Paint();

    private Collection<Entry> mEntries;

    public static class Entry {
        public final float percentage;
        public final Paint paint;

        protected Entry(float percentage, Paint paint) {
            this.percentage = percentage;
            this.paint = paint;
        }
    }

    public PercentageBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PercentageBarChart, 0, 0);

        int emptyColor = Color.BLACK;

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.PercentageBarChart_emptyColor:
                    emptyColor = a.getColor(attr, 0);
                    break;
            }
        }

        a.recycle();

        mEmptyPaint.setColor(emptyColor);
        mEmptyPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int left = getPaddingLeft();
        final int right = getWidth() - getPaddingRight();
        final int top = getPaddingTop();
        final int bottom = getHeight() - getPaddingBottom();

        final int width = right - left;

        int lastX = left;

        if (mEntries != null) {
            for (final Entry e : mEntries) {
                final int entryWidth;
                if (e.percentage == 0f) {
                    entryWidth = 0;
                } else {
                    entryWidth = Math.max(1, (int) (width * e.percentage));
                }

                final int nextX = lastX + entryWidth;
                if (nextX >= right) {
                    break;
                }

                canvas.drawRect(lastX, top, nextX, bottom, e.paint);
                lastX = nextX;
            }
        }

        canvas.drawRect(lastX, top, lastX + width, bottom, mEmptyPaint);
    }

    /**
     * Sets the background for this chart. Callers are responsible for later
     * calling {@link #invalidate()}.
     */
    public void setBackgroundColor(int color) {
        mEmptyPaint.setColor(color);
    }

    /**
     * Adds a new slice to the percentage bar chart. Callers are responsible for
     * later calling {@link #invalidate()}.
     * 
     * @param percentage the total width that
     * @param color the color to draw the entry
     */
    public static Entry createEntry(float percentage, int color) {
        final Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);

        return new Entry(percentage, p);
    }

    public void setEntries(Collection<Entry> entries) {
        mEntries = entries;
    }
}
