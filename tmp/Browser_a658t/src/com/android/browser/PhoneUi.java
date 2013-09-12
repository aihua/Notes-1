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

package com.android.browser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.browser.UrlInputView.StateListener;

/**
 * Ui for regular phone screen sizes
 */
public class PhoneUi extends BaseUi {

    private static final String LOGTAG = "PhoneUi";
    public static final String LOGTAG_TWICE = "Load_twice";
    private static final int MSG_INIT_NAVSCREEN = 100;

    private PieControlPhone mPieControl;
    private NavScreen mNavScreen;
    private AnimScreen mAnimScreen;
    private NavigationBarPhone mNavigationBar;
    private int mActionBarHeight;
    private BrowserSettings mSettings;

    boolean mExtendedMenuOpen;
    boolean mOptionsMenuOpen;
    boolean mAnimating;

    /**
     * @param browser
     * @param controller
     */
    public PhoneUi(Activity browser, UiController controller) {
        super(browser, controller);
        setUseQuickControls(BrowserSettings.getInstance().useQuickControls());
        mNavigationBar = (NavigationBarPhone) mTitleBar.getNavigationBar();
        TypedValue heightValue = new TypedValue();
        browser.getTheme().resolveAttribute(
                com.android.internal.R.attr.actionBarSize, heightValue, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(heightValue.data,
                browser.getResources().getDisplayMetrics());
        mSettings = BrowserSettings.getInstance();
    }

    @Override
    public void onDestroy() {
        hideTitleBar();
        getQuickNavigationScreen().closeScreenCursor();
    }

    @Override
    public void editUrl(boolean clearInput) {
        if (mUseQuickControls) {
            mTitleBar.setShowProgressOnly(false);
        }
        super.editUrl(clearInput);
    }

    @Override
    public void Gotourl(){
        mTitleBar.Gotourl();
        mTitleBar.show();
    }
    @Override
    public boolean onBackKey() {
        if (showingNavScreen()) {
            mNavScreen.close(mUiController.getTabControl().getCurrentPosition());
            return true;
        }
        return super.onBackKey();
    }

    public boolean showingNavScreen() {
        return mNavScreen != null && mNavScreen.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean dispatchKey(int code, KeyEvent event) {
        return false;
    }

    @Override
    public void onProgressChanged(Tab tab) {
        if (tab.inForeground()) {
            int progress = tab.getLoadProgress();
            mTitleBar.setProgress(progress);
            if (progress == 100) {
                if (!mOptionsMenuOpen || !mExtendedMenuOpen) {
                    suggestHideTitleBar();
                    if (mUseQuickControls) {
                        mTitleBar.setShowProgressOnly(false);
                    }
                }
            } else {
                if (!mOptionsMenuOpen || mExtendedMenuOpen) {
                    if (mUseQuickControls && !mTitleBar.isEditingUrl()) {
                        mTitleBar.setShowProgressOnly(true);
                        setTitleGravity(Gravity.TOP);
                    }
                    showTitleBar();
                }
            }
        }
        if (mNavScreen == null && getTitleBar().getHeight() > 0) {
            mHandler.sendEmptyMessage(MSG_INIT_NAVSCREEN);
        }
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == MSG_INIT_NAVSCREEN) {
            if (mNavScreen == null) {
                mNavScreen = new NavScreen(mActivity, mUiController, this);
                mCustomViewContainer.addView(mNavScreen, COVER_SCREEN_PARAMS);
                mNavScreen.setVisibility(View.GONE);
            }
            if (mAnimScreen == null) {
                mAnimScreen = new AnimScreen(mActivity);
                // initialize bitmaps
                mAnimScreen.set(getTitleBar(), getWebView());
            }
        }
    }

    @Override
    public void setActiveTab(final Tab tab) {
        mTitleBar.cancelTitleBarAnimation(true);
        mTitleBar.setSkipTitleBarAnimations(true);
        super.setActiveTab(tab);
        BrowserWebView view = (BrowserWebView) tab.getWebView();
        // TabControl.setCurrentTab has been called before this,
        // so the tab is guaranteed to have a webview
        if (view == null) {
            Log.e(LOGTAG, "active tab with no webview detected");
            return;
        }
        // Request focus on the top window.
        if (mUseQuickControls) {
            mPieControl.forceToTop(mContentView);
        } else {
            // check if title bar is already attached by animation
            if (mTitleBar.getParent() == null) {
                view.setEmbeddedTitleBar(mTitleBar);
            }
        }
        if (tab.isInVoiceSearchMode()) {
            showVoiceTitleBar(tab.getVoiceDisplayTitle(), tab.getVoiceSearchResults());
        } else {
            revertVoiceTitleBar(tab);
        }
        // update nav bar state
        mNavigationBar.onStateChanged(StateListener.STATE_NORMAL);
        updateLockIconToLatest(tab);
        tab.getTopWindow().requestFocus();
        mTitleBar.setSkipTitleBarAnimations(false);
    }

    // menu handling callbacks

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuState(mActiveTab, menu);
        return true;
    }

    @Override
    public void updateMenuState(Tab tab, Menu menu) {
        MenuItem bm = menu.findItem(R.id.bookmarks_menu_id);
        if (bm != null) {
            bm.setVisible(!showingNavScreen());
        }
        MenuItem abm = menu.findItem(R.id.add_bookmark_menu_id);
        if (abm != null) {
            abm.setVisible((tab != null) && !tab.isSnapshot() && !showingNavScreen());
        }
        MenuItem info = menu.findItem(R.id.page_info_menu_id);
        if (info != null) {
            info.setVisible(false);
        }
        if(BrowserSettings.CU_PLATFORM){
            info.setVisible(true);
        }
        MenuItem newtab = menu.findItem(R.id.new_tab_menu_id);
//       if (newtab != null && !mUseQuickControls) {
//           newtab.setVisible(false);
//        }
        MenuItem incognito = menu.findItem(R.id.incognito_menu_id);
        if (incognito != null) {
//            incognito.setVisible(showingNavScreen() || mUseQuickControls);
            incognito.setVisible(false);
        }
        if (showingNavScreen()) {
            menu.setGroupVisible(R.id.LIVE_MENU, false);
            menu.setGroupVisible(R.id.SNAPSHOT_MENU, false);
            menu.setGroupVisible(R.id.NAV_MENU, false);
            menu.setGroupVisible(R.id.COMBO_MENU, true);
        }
        menu.setGroupVisible(R.id.NAV_MENU, false);
        if(Controller.homepage == true||Controller.homepageBack == true){
            menu.findItem(R.id.back2_menu_id).setEnabled(false);
            menu.findItem(R.id.reload_menu_id).setEnabled(false);
            menu.findItem(R.id.switch_url).setEnabled(false);
            menu.findItem(R.id.tab_switch).setEnabled(false);
            menu.findItem(R.id.add_bookmark_menu_id).setEnabled(false);
            menu.findItem(R.id.share_page_menu_id).setEnabled(false);
            menu.findItem(R.id.save_snapshot_menu_id).setEnabled(false);
            menu.setGroupVisible(R.id.SNAPSHOT_MENU, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (showingNavScreen()
                && (item.getItemId() != R.id.history_menu_id)
                && (item.getItemId() != R.id.snapshots_menu_id)) {
            hideNavScreen(mUiController.getTabControl().getCurrentPosition(), false);
        }
        return false;
    }

    @Override
    public void onContextMenuCreated(Menu menu) {
        hideTitleBar();
    }

    @Override
    public void onContextMenuClosed(Menu menu, boolean inLoad) {
        if (inLoad) {
            showTitleBar();
        }
    }

    // action mode callbacks

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (!isEditingUrl()) {
            hideTitleBar();
        } else {
            //pekall
            //mTitleBar.animate().translationY(mActionBarHeight);
        }
    }

    @Override
    public void onActionModeFinished(boolean inLoad) {
        mTitleBar.animate().translationY(0);
        if (inLoad) {
            if (mUseQuickControls) {
                mTitleBar.setShowProgressOnly(true);
            }
            showTitleBar();
        }
    }

    @Override
    protected void setTitleGravity(int gravity) {
        if (mUseQuickControls) {
            FrameLayout.LayoutParams lp =
                    (FrameLayout.LayoutParams) mTitleBar.getLayoutParams();
            lp.gravity = gravity;
            mTitleBar.setLayoutParams(lp);
        } else {
            super.setTitleGravity(gravity);
        }
    }

    @Override
    public void setUseQuickControls(boolean useQuickControls) {
        mUseQuickControls = useQuickControls;
        mTitleBar.setUseQuickControls(mUseQuickControls);
        if (useQuickControls) {
            mPieControl = new PieControlPhone(mActivity, mUiController, this);
            mPieControl.attachToContainer(mContentView);
            WebView web = getWebView();
            if (web != null) {
                web.setEmbeddedTitleBar(null);
            }
        } else {
            if (mPieControl != null) {
                mPieControl.removeFromContainer(mContentView);
            }
            WebView web = getWebView();
            if (web != null) {
                // make sure we can re-parent titlebar
                if ((mTitleBar != null) && (mTitleBar.getParent() != null)) {
                    ((ViewGroup) mTitleBar.getParent()).removeView(mTitleBar);
                }
                web.setEmbeddedTitleBar(mTitleBar);
            }
            setTitleGravity(Gravity.NO_GRAVITY);
        }
        updateUrlBarAutoShowManagerTarget();
    }

    @Override
    public boolean isWebShowing() {
        return super.isWebShowing() && !showingNavScreen();
    }

    @Override
    public void showWeb(boolean animate) {
        super.showWeb(animate);
        hideNavScreen(mUiController.getTabControl().getCurrentPosition(), animate);
    }

    void showNavScreen() {
        if(BrowserSettings.PAGE_TURN_LOAD_TWICE){
     	   Log.v(LOGTAG_TWICE, "enter  showNavScreen()");
        }
        if(mActiveTab == null){
        	Log.v(LOGTAG, "when show NavScreen , mActiveTab is null!");
        	return ;
        }
        mUiController.getHandler().removeMessages(Controller.LOAD_URL_DELAY);
        mUiController.getHandler().removeMessages(Controller.LOAD_URL_DELAY_BECKGROUND);
        mUiController.setBlockEvents(true);
        if (mNavScreen == null) {
            mNavScreen = new NavScreen(mActivity, mUiController, this);
            mCustomViewContainer.addView(mNavScreen, COVER_SCREEN_PARAMS);
        } else {
            mNavScreen.setVisibility(View.VISIBLE);
            mNavScreen.setAlpha(1f);
            mNavScreen.refreshAdapter();
        }
        mActiveTab.capture();
        if (mAnimScreen == null) {
            mAnimScreen = new AnimScreen(mActivity);
        } else {
            mAnimScreen.mMain.setAlpha(1f);
            mAnimScreen.mTitle.setAlpha(1f);
            mAnimScreen.setScaleFactor(1f);
        }
        mAnimScreen.set(getTitleBar(), getWebView());
        if (mAnimScreen.mMain.getParent() == null) {
            mCustomViewContainer.addView(mAnimScreen.mMain, COVER_SCREEN_PARAMS);
        }
        mCustomViewContainer.setVisibility(View.VISIBLE);
        mCustomViewContainer.bringToFront();
        mAnimScreen.mMain.layout(0, 0, mContentView.getWidth(),
                mContentView.getHeight());
        int fromLeft = 0;
        int fromTop = getTitleBar().getHeight();
        int fromRight = mContentView.getWidth();
        int fromBottom = mContentView.getHeight();
        int width = mActivity.getResources().getDimensionPixelSize(R.dimen.nav_tab_width);
        int height = mActivity.getResources().getDimensionPixelSize(R.dimen.nav_tab_height);
        int ntth = mActivity.getResources().getDimensionPixelSize(R.dimen.nav_tab_titleheight);
        int toLeft = (mContentView.getWidth() - width) / 2;
        int toTop = ((fromBottom - (ntth + height)) / 2 + ntth);
        int toRight = toLeft + width;
        int toBottom = toTop + height;
        float scaleFactor = width / (float) mContentView.getWidth();
        detachTab(mActiveTab);
        mContentView.setVisibility(View.GONE);
        AnimatorSet set1 = new AnimatorSet();
        AnimatorSet inanim = new AnimatorSet();
        ObjectAnimator tx = ObjectAnimator.ofInt(mAnimScreen.mContent, "left",
                fromLeft, toLeft);
        ObjectAnimator ty = ObjectAnimator.ofInt(mAnimScreen.mContent, "top",
                fromTop, toTop);
        ObjectAnimator tr = ObjectAnimator.ofInt(mAnimScreen.mContent, "right",
                fromRight, toRight);
        ObjectAnimator tb = ObjectAnimator.ofInt(mAnimScreen.mContent, "bottom",
                fromBottom, toBottom);
        ObjectAnimator title = ObjectAnimator.ofFloat(mAnimScreen.mTitle, "alpha",
                1f, 0f);
        ObjectAnimator sx = ObjectAnimator.ofFloat(mAnimScreen, "scaleFactor",
                1f, scaleFactor);
        ObjectAnimator blend1 = ObjectAnimator.ofFloat(mAnimScreen.mMain,
                "alpha", 1f, 0f);
        blend1.setDuration(100);

        inanim.playTogether(tx, ty, tr, tb, sx, title);
        inanim.setDuration(200);
        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                mCustomViewContainer.removeView(mAnimScreen.mMain);
                finishAnimationIn();
                mUiController.setBlockEvents(false);
            }
        });
        set1.playSequentially(inanim, blend1);
        set1.start();
        
        boolean flip = mSettings.getSharedPreferences().getBoolean(PreferenceKeys.PREF_PAGE_FLIP, false);
        if(flip && Controller.pageFilpState){
            mUiController.hidePageFlip();            
        }
        
        mUiController.hasFlowViewAdded();
        mUiController.hideInternetFlow();
        
    }

    private void finishAnimationIn() {
        if (showingNavScreen()) {
            // notify accessibility manager about the screen change
            mNavScreen.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            mTabControl.setOnThumbnailUpdatedListener(mNavScreen);
        }
    }

    private void isTurnToHomeTab(Tab tab){
        boolean homeTab = tab.isHomeTab();
        if(homeTab && tab.getOriginalUrl().length()==0 && tab.getUrl().length()==0){
            Controller.homepageBack = true;
            Controller.homepage = true;
            mUiController.getHandler().removeMessages(Controller.LOAD_URL_DELAY);
            mUiController.getHandler().removeMessages(Controller.LOAD_URL_DELAY_BECKGROUND);
            qucikNLRequestFoucs();
            SetSanyaHomepage(); 
            showHomePage();
            qucikNLRequestFoucs();
            getMainMenuControlPhone().setForwardEnabledFalse();
            getMainMenuControlPhone().setBackWardEnabledFalse();
        }  
            mUiController.setActiveTab(tab);
        
    }
    
    void hideNavScreen(int position, boolean animate) {
        if (!showingNavScreen()) return;
        final Tab tab = mUiController.getTabControl().getTab(position);
        if ((tab == null) || !animate) {
            if (tab != null) {
                setActiveTab(tab);
            } else if (mTabControl.getTabCount() > 0) {
                // use a fallback tab
                setActiveTab(mTabControl.getCurrentTab());
            }
            mContentView.setVisibility(View.VISIBLE);
            finishAnimateOut();
            return;
        }
        
        if(Controller.homepage == true||Controller.homepageBack == true){
            Controller.homepage = false;
            Controller.homepageBack = false;
            hideHomePage();
        } 
            
        NavTabView tabview = (NavTabView) mNavScreen.getTabView(position);
        if (tabview == null) {
            if (mTabControl.getTabCount() > 0) {
                // use a fallback tab
                setActiveTab(mTabControl.getCurrentTab());
            }
            mContentView.setVisibility(View.VISIBLE);
            finishAnimateOut();
            return;
        }
        mUiController.setBlockEvents(true);
    //    mUiController.setActiveTab(tab);
        isTurnToHomeTab(tab);
        
       if(BrowserSettings.PAGE_TURN_LOAD_TWICE){
    	   Log.v(LOGTAG_TWICE, "hideNavScreen tab url:"+tab.getUrl());
       }
        mContentView.setVisibility(View.VISIBLE);
        if (mAnimScreen == null) {
            mAnimScreen = new AnimScreen(mActivity);
        }
        mAnimScreen.set(tab.getScreenshot());
        try {
            if (mAnimScreen.mMain.getParent() == null) {
                    mCustomViewContainer.addView(
                            mAnimScreen.mMain, COVER_SCREEN_PARAMS);
             }
        } catch (Exception e) {
            Log.e(LOGTAG, "", e);
        }
        mAnimScreen.mMain.layout(0, 0, mContentView.getWidth(),
                mContentView.getHeight());
        mNavScreen.mScroller.finishScroller();
        ImageView target = tabview.mImage;
        int toLeft = 0;
        int toTop = getTitleBar().getHeight();
        int toRight = mContentView.getWidth();
        int width = target.getDrawable().getIntrinsicWidth();
        int height = target.getDrawable().getIntrinsicHeight();
        int fromLeft = tabview.getLeft() + target.getLeft() - mNavScreen.mScroller.getScrollX();
        int fromTop = tabview.getTop() + target.getTop() - mNavScreen.mScroller.getScrollY();
        int fromRight = fromLeft + width;
        int fromBottom = fromTop + height;
        float scaleFactor = mContentView.getWidth() / (float) width;
        int toBottom = toTop + (int) (height * scaleFactor);
        mAnimScreen.mContent.setLeft(fromLeft);
        mAnimScreen.mContent.setTop(fromTop);
        mAnimScreen.mContent.setRight(fromRight);
        mAnimScreen.mContent.setBottom(fromBottom);
        AnimatorSet set1 = new AnimatorSet();
        AnimatorSet set2 = new AnimatorSet();
        AnimatorSet combo = new AnimatorSet();
        mAnimScreen.setScaleFactor(1f);
        ObjectAnimator fade2 = ObjectAnimator.ofFloat(mAnimScreen.mMain, "alpha", 0f, 1f);
        ObjectAnimator fade1 = ObjectAnimator.ofFloat(mNavScreen, "alpha", 1f, 0f);
        set1.playTogether(fade1, fade2);
        set1.setDuration(10);
        ObjectAnimator l = ObjectAnimator.ofInt(mAnimScreen.mContent, "left",
                fromLeft, toLeft);
        ObjectAnimator t = ObjectAnimator.ofInt(mAnimScreen.mContent, "top",
                fromTop, toTop);
        ObjectAnimator r = ObjectAnimator.ofInt(mAnimScreen.mContent, "right",
                fromRight, toRight);
        ObjectAnimator b = ObjectAnimator.ofInt(mAnimScreen.mContent, "bottom",
                fromBottom, toBottom);
        ObjectAnimator scale = ObjectAnimator.ofFloat(mAnimScreen, "scaleFactor",
                1f, scaleFactor);
        ObjectAnimator otheralpha = ObjectAnimator.ofFloat(mCustomViewContainer, "alpha", 1f, 0f);
        otheralpha.setDuration(100);
        set2.playTogether(l, t, r, b, scale);
        set2.setDuration(200);
        combo.playSequentially(set1, set2, otheralpha);
       
        
        combo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                mCustomViewContainer.removeView(mAnimScreen.mMain);
                finishAnimateOut();
                mUiController.setBlockEvents(false);
            }
        });
        combo.start();
        
        boolean flip = mSettings.getSharedPreferences().getBoolean(PreferenceKeys.PREF_PAGE_FLIP, false);
        if(flip && !Controller.pageFilpState)
            mUiController.addPageFlip();
     
        mUiController.addInternetFlow();
        if(Controller.homepage!=true && Controller.homepageBack!=true){
            getMainMenuControlPhone().setBackWardEnabledTrue();
        }
        
        if(tab.canGoForward()){
            getMainMenuControlPhone().setForwardEnabledTrue();
        }else{
            getMainMenuControlPhone().setForwardEnabledFalse();
        }
    }

    private void finishAnimateOut() {
        mTabControl.setOnThumbnailUpdatedListener(null);
        mNavScreen.setVisibility(View.GONE);
        mCustomViewContainer.setAlpha(1f);
        mCustomViewContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean needsRestoreAllTabs() {
        return false;
    }

    public void toggleNavScreen() {
        if (!showingNavScreen()) {
            showNavScreen();
        } else {
            hideNavScreen(mUiController.getTabControl().getCurrentPosition(), false);
        }
    }

    @Override
    public boolean shouldCaptureThumbnails() {
        return true;
    }

    static class AnimScreen {

        private View mMain;
        private ImageView mTitle;
        private ImageView mContent;
        private float mScale;
        private Bitmap mTitleBarBitmap;
        private Bitmap mContentBitmap;

        public AnimScreen(Context ctx) {
            mMain = LayoutInflater.from(ctx).inflate(R.layout.anim_screen,
                    null);
            mTitle = (ImageView) mMain.findViewById(R.id.title);
            mContent = (ImageView) mMain.findViewById(R.id.content);
            mContent.setScaleType(ImageView.ScaleType.MATRIX);
            mContent.setImageMatrix(new Matrix());
            mScale = 1.0f;
            setScaleFactor(getScaleFactor());
        }

        public void set(TitleBar tbar, WebView web) {
            if (tbar == null || web == null) {
                return;
            }
            if (tbar.getWidth() > 0 && tbar.getEmbeddedHeight() > 0) {
                if (mTitleBarBitmap == null
                        || mTitleBarBitmap.getWidth() != tbar.getWidth()
                        || mTitleBarBitmap.getHeight() != tbar.getEmbeddedHeight()) {
                    mTitleBarBitmap = safeCreateBitmap(tbar.getWidth(),
                            tbar.getEmbeddedHeight());
                }
                if (mTitleBarBitmap != null) {
                    Canvas c = new Canvas(mTitleBarBitmap);
                    tbar.draw(c);
                    c.setBitmap(null);
                }
            } else {
                mTitleBarBitmap = null;
            }
            mTitle.setImageBitmap(mTitleBarBitmap);
            mTitle.setVisibility(View.VISIBLE);
            int h = web.getHeight() - tbar.getEmbeddedHeight();
            if (mContentBitmap == null
                    || mContentBitmap.getWidth() != web.getWidth()
                    || mContentBitmap.getHeight() != h) {
                mContentBitmap = safeCreateBitmap(web.getWidth(), h);
            }
            if (mContentBitmap != null) {
                Canvas c = new Canvas(mContentBitmap);
                int tx = web.getScrollX();
                int ty = web.getScrollY();
                c.translate(-tx, -ty - tbar.getEmbeddedHeight());
                web.draw(c);
                c.setBitmap(null);
            }
            mContent.setImageBitmap(mContentBitmap);
        }

        private Bitmap safeCreateBitmap(int width, int height) {
            if (width <= 0 || height <= 0) {
                Log.w(LOGTAG, "safeCreateBitmap failed! width: " + width
                        + ", height: " + height);
                return null;
            }
            return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }

        public void set(Bitmap image) {
            mTitle.setVisibility(View.GONE);
            mContent.setImageBitmap(image);
        }

        private void setScaleFactor(float sf) {
            mScale = sf;
            Matrix m = new Matrix();
            m.postScale(sf,sf);
            mContent.setImageMatrix(m);
        }

        private float getScaleFactor() {
            return mScale;
        }

    }

}