package com.augmentedrealityapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.dao.PayPalData;
import com.augmentedrealityapp.delegates.PaymentStatus;
import com.augmentedrealityapp.fragement.CardView;
import com.augmentedrealityapp.fragement.CategoryView;
import com.augmentedrealityapp.fragement.CheckoutView;
import com.augmentedrealityapp.fragement.ProductListView;
import com.augmentedrealityapp.fragement.ProfileView;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.Util;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private CategoryView mCategoryView;
    private ProductListView mProductListView;
    private CardView mCardView;
    private ProfileView mProfileView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    private int badgeCount = 0;
    private static final int SAMPLE2_ID = 34535;
    private SharedPreferences pref;
    private String email, name;
    private TextView userName, userEmail;
    private Menu menu;
    private String TAG = "Payment";
    private static final int REQUEST_CODE_PAYMENT = 1;

    @Override
    protected int myView() {
        return R.layout.home_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setUpToolbar();
        initViews();
    }

    public void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setupNavigationDrawer();
        }
    }

    public void hideToolBar() {
        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();
    }

    public void showToolBar() {
        if (getSupportActionBar() != null)
            this.getSupportActionBar().show();
    }

    private void initViews() {
        fab.setOnClickListener(this);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email);
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name);
        pref = Util.getSharedPreferences(this);
        email = pref.getString("email", "");
        name = pref.getString("userName", "");
        badgeCount = pref.getInt("cardCount", 0);
        userEmail.setText(email);
        userName.setText(name);
        mCategoryView = new CategoryView();
        addFragementView(mCategoryView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {

            } else {
                super.onBackPressed();
            }
        }
    }

    public void updateCart(int cartCount) {
        if (menu != null)
            ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, ActionItemBadge.BadgeStyles.RED, cartCount);
    }

    private void hideCartBadge() {
        ActionItemBadge.hide(menu.findItem(R.id.item_samplebadge));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        this.menu = menu;
        if (badgeCount >= 0) {
            ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, ActionItemBadge.BadgeStyles.RED, badgeCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.item_samplebadge));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_samplebadge) {
            //Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
            //badgeCount--;
            //showToolBar();
            //setUpToolbar();
            hideCartBadge();
            mCardView = new CardView();
            if (mCardView != null && !mCardView.isAdded())
                addFragementView(mCardView);

            //ActionItemBadge.update(item, badgeCount);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_shopping_cart, ActionItemBadge.BadgeStyles.RED, badgeCount);

    }

    public void addFragementView(Fragment fragment) {
        addFragment(R.id.container, fragment, fragment.getClass().getName());
    }

    public void clearBackStackInclusive(String tag) {
        try {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Fragment f = mManager.findFragmentById(R.id.cont);
        if (id == R.id.cat) {
            if (mCategoryView != null && !mCategoryView.isAdded())
                addFragementView(mCategoryView);
        } else if (id == R.id.cart) {
            mCardView = new CardView();
            addFragementView(mCardView);

        } else if (id == R.id.profile) {
            mProfileView = new ProfileView();
            addFragementView(mProfileView);

        } else if (id == R.id.check_out) {
            //mProductDetailView = new ProductDetailView();
            //addFragementView(mProductDetailView);

        } else if (id == R.id.logout) {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
                else{
                    if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        finish();
                    } else {
                        getSupportFragmentManager().popBackStack();
                        //super.onBackPressed();
                    }
                }
                return true;
            }

            return super.onKeyDown(keyCode, event);
        }

        ActionBarDrawerToggle toggle;

    public void setupNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.cat));
        final View.OnClickListener originalToolbarListener = toggle.getToolbarNavigationClickListener();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                final Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.container);

                if (mFragment instanceof CategoryView) {
                    getSupportActionBar().setTitle("Category");
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toggle.setDrawerIndicatorEnabled(true);
                    toggle.setToolbarNavigationClickListener(originalToolbarListener);
                    updateCart(Constants.CART_COUNT);

                } else {


                    /*else if(mFragment instanceof ProductListView)
                    {
                        getSupportActionBar().setTitle("Category");
                    }*/
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                                finish();
                            } else {
                                onBackPressed();
                            }
                        }
                    });
                    if (mFragment instanceof ProductListView) {
                        getSupportActionBar().setTitle("Product");
                        updateCart(Constants.CART_COUNT);
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    protected void displayResultText(String id, String state, String amount, String des) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f instanceof CheckoutView)
            ((CheckoutView) f).checkPayment(id, state, amount, des);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        //PayPalData mPayPalData=new PayPalData
                        JSONObject jsonObject = confirm.toJSONObject().getJSONObject("response");
                        JSONObject payment = confirm.getPayment().toJSONObject();
                        Log.i(TAG, jsonObject.getString("id"));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        String id = jsonObject.getString("id");
                        String state = jsonObject.getString("state");
                        String amount = payment.getString("amount");
                        String shortDescription = payment.getString("short_description");
                        displayResultText(id, state, amount, shortDescription);
                        //checkPayment();

                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

}
