/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antonioleiva.materializeyourapp;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE = "com.antonioleiva.materializeyourapp.extraImage";
    private static final String EXTRA_TITLE = "com.antonioleiva.materializeyourapp.extraTitle";

    @BindView(R.id.image) ImageView image;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.scroll) NestedScrollView scroll;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.revealView) LinearLayout revealView;
    @BindView(R.id.layoutButtons) LinearLayout layoutButtons;
    @BindView(R.id.duplicate) Button duplicate;
    @BindView(R.id.edit) Button edit;
    @BindView(R.id.delete) Button delete;

    private Animation fadeIn;
    private Animation fadeOut;
    private Animation rotateClock;
    private Animation rotateAnti;
    private boolean flag = false;

    public static void navigate(AppCompatActivity fromActivity, View transitionImage, ViewModel viewModel) {
        Intent intent = new Intent(fromActivity, DetailActivity.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getImage());
        intent.putExtra(EXTRA_TITLE, viewModel.getText());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fromActivity, transitionImage, EXTRA_IMAGE);
        ActivityCompat.startActivity(fromActivity, intent, options.toBundle());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        rotateClock = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
        rotateAnti = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlock);
        rotateClock.setFillAfter(true);
        rotateAnti.setFillAfter(true);

        ViewCompat.setTransitionName(appBarLayout, EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
        collapsingToolbar.setTitle(itemTitle);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE)).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {
            }
        });

        title.setText(itemTitle);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revealTheLayout();
            }
        });
    }

    private void revealTheLayout() {
        final int x = image.getRight();
        final int y = image.getBottom();
        final int hypotenuse = (int) Math.hypot(image.getWidth(), image.getHeight());

        ColorStateList colorList = fab.getBackgroundTintList();
        int color = colorList.getDefaultColor();
        if (color == Color.parseColor("#FFEB3B")) {
            duplicate.setTextColor(getResources().getColor(R.color.faded_black));
            duplicate.setBackground(getResources().getDrawable(R.drawable.black_stroke_button));
            edit.setTextColor(getResources().getColor(R.color.faded_black));
            edit.setBackground(getResources().getDrawable(R.drawable.black_stroke_button));
            delete.setTextColor(getResources().getColor(R.color.faded_black));
            delete.setBackground(getResources().getDrawable(R.drawable.black_stroke_button));
        } else {
            duplicate.setTextColor(getResources().getColor(R.color.white));
            duplicate.setBackground(getResources().getDrawable(R.drawable.white_stroke_button));
            edit.setTextColor(getResources().getColor(R.color.white));
            edit.setBackground(getResources().getDrawable(R.drawable.white_stroke_button));
            delete.setTextColor(getResources().getColor(R.color.white));
            delete.setBackground(getResources().getDrawable(R.drawable.white_stroke_button));
        }
        revealView.setBackgroundColor(color);

        if (!flag) {
            flag = true;
            fab.startAnimation(rotateClock);
            revealView.setVisibility(View.VISIBLE);
            layoutButtons.setVisibility(View.GONE);

            Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
            anim.setDuration(250);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    layoutButtons.setVisibility(View.VISIBLE);
                    layoutButtons.startAnimation(fadeIn);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            anim.start();
        } else {
            fab.startAnimation(rotateAnti);
            layoutButtons.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutButtons.setVisibility(View.GONE);
                    final Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                    anim.setDuration(250);
                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            revealView.setVisibility(View.GONE);
                            flag = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    anim.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground(fab, palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.accent));
        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }

}
