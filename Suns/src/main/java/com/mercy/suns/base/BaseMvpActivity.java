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
package com.mercy.suns.base;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.mercy.suns.base.delegate.IActivity;
import com.mercy.suns.integration.cache.Cache;
import com.mercy.suns.integration.cache.CacheType;
import com.mercy.suns.integration.lifecycle.ActivityLifecycleable;
import com.mercy.suns.mvp.IPresenter;
import com.mercy.suns.utils.SunsUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


/**
 * ================================================
 * 因为 Java 只能单继承,所以如果要用到需要继承特定 {@link Activity} 的三方库,那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity},然后再按照 {@link BaseMvpActivity} 的格式,将代码复制过去,记住一定要实现{@link IActivity}
 *
 * Created by Sun on 2018/2/2
 * ================================================
 */
public abstract class BaseMvpActivity<P extends IPresenter> extends BaseActivity{

    @Inject
    protected P mPresenter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
            this.mPresenter = null;
        }
    }
}
