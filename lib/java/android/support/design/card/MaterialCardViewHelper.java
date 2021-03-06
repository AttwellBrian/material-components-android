/*
 * Copyright (C) 2017 The Android Open Source Project
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

package android.support.design.card;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;

/** @hide */
@RestrictTo(LIBRARY_GROUP)
class MaterialCardViewHelper {

  private static final int DEFAULT_STROKE_VALUE = -1;
  private final MaterialCardView materialCardView;

  private float cornerRadius;
  private int strokeColor;
  private int strokeWidth;

  public MaterialCardViewHelper(MaterialCardView card) {
    materialCardView = card;
  }

  public void loadFromAttributes(TypedArray attributes) {
    cornerRadius = attributes.getDimensionPixelSize(R.styleable.CardView_cardCornerRadius, 0);
    strokeColor =
        attributes.getColor(R.styleable.MaterialCardView_strokeColor, DEFAULT_STROKE_VALUE);
    strokeWidth = attributes.getDimensionPixelSize(R.styleable.MaterialCardView_strokeWidth, 0);
    ViewCompat.setBackground(materialCardView, createBackgroundDrawable());
    adjustContentPadding(strokeWidth);
  }

  /**
   * Creates a drawable background for the card in order to handle a stroke outline.
   *
   * @return drawable representing background for a card.
   */
  private Drawable createBackgroundDrawable() {
    GradientDrawable bgDrawable = new GradientDrawable();
    bgDrawable.setCornerRadius(cornerRadius);
    // In order to set a stroke, a size and color both need to be set. We default to a zero-width
    // width size, but won't set a default color. This prevents drawing a stroke that blends in with
    // the card but that could affect card spacing.
    if (strokeColor != DEFAULT_STROKE_VALUE) {
      bgDrawable.setStroke(strokeWidth, strokeColor);
    }

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      bgDrawable.setColor(materialCardView.getCardBackgroundColor());
    } else {
      // For API < 21, use the default background color from the colorStateList that's provided by
      // {@code getCardBackgroundColor}; this class is not currently attempting to handle states.
      bgDrawable.setColor(materialCardView.getCardBackgroundColor().getDefaultColor());
    }

    return bgDrawable;
  }

  /**
   * Guarantee at least enough content padding to account for the stroke width.
   *
   * @param strokeWidth width of the stroke on the card
   */
  private void adjustContentPadding(int strokeWidth) {
    int contentPaddingLeft = materialCardView.getContentPaddingLeft() + strokeWidth;
    int contentPaddingTop = materialCardView.getContentPaddingTop() + strokeWidth;
    int contentPaddingRight = materialCardView.getContentPaddingRight() + strokeWidth;
    int contentPaddingBottom = materialCardView.getContentPaddingBottom() + strokeWidth;
    materialCardView.setContentPadding(
        contentPaddingLeft, contentPaddingTop, contentPaddingRight, contentPaddingBottom);
  }
}
