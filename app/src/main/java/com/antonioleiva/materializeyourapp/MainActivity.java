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

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.antonioleiva.materializeyourapp.picasso.CircleTransform;
import com.antonioleiva.materializeyourapp.widgets.GridRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String AVATAR_URL = "http://lorempixel.com/200/200/people/1/";
    private static List<ViewModel> items = new ArrayList<>();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appBarLayout) AppBarLayout appBarLayout;
    @BindView(R.id.recycler_view) GridRecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.content) CoordinatorLayout content;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    private GridLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeUI();

        avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        Picasso.with(this).load(AVATAR_URL).transform(new CircleTransform()).into(avatar);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        adapter = new RecyclerViewAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ViewModel viewModel) {
                DetailActivity.navigate(MainActivity.this, view.findViewById(R.id.image), viewModel);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 1; i <= 10; i++) {
            items.add(new ViewModel("Item " + i, "http://lorempixel.com/500/500/animals/" + i));
        }
    }

    private void initializeUI() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
