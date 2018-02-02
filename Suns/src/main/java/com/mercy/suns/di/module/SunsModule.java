/**
  * Copyright 2017 JessYan
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
package com.mercy.suns.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mercy.suns.integration.FragmentLifecycle;
import com.mercy.suns.integration.IRepositoryManager;
import com.mercy.suns.integration.RepositoryManager;
import com.mercy.suns.integration.cache.Cache;
import com.mercy.suns.integration.cache.CacheType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * ================================================
 * 提供一些框架必须的实例的 {@link Module}
 * Created by Sun on 2018/2/2.
 * ================================================
 */
@Module(includes = {AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class})
public class SunsModule {
//    private Application mApplication;
//
//    public SunsModule(Application application) {
//        this.mApplication = application;
//    }
//
//    @Singleton
//    @Provides
//    public Application provideApplication() {
//        return mApplication;
//    }

    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

    @Singleton
    @Provides
    public FragmentManager.FragmentLifecycleCallbacks provideFragmentLifecycle() {
        return new FragmentLifecycle();
    }

    @Singleton
    @Provides
    public List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles(){
        return new ArrayList<>();
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }

}
