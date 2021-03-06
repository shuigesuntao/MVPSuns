/**
  * Copyright 2018 Sun
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
package com.mercy.suns.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * ================================================
 * {@link ActivityDelegate} 默认实现类
 * Created by Sun on 2018/2/2
 * ================================================
 */
public class ActivityDelegateImpl implements ActivityDelegate , HasSupportFragmentInjector {
    private Activity mActivity;
    private IActivity iActivity;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity.useEventBus()){
            //注册到事件主线
            EventBus.getDefault().register(mActivity);
        }

        AndroidInjection.inject(mActivity);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity.useEventBus())
            EventBus.getDefault().unregister(mActivity);
        this.iActivity = null;
        this.mActivity = null;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return this.mFragmentInjector;
    }
}
