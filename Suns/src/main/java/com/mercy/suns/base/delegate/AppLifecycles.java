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
package com.mercy.suns.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * ================================================
 * 用于代理 {@link Application} 的生命周期
 *
 * @see AppDelegate
 * Created by Sun on 2018/2/2
 * ================================================
 */
public interface AppLifecycles {
    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
