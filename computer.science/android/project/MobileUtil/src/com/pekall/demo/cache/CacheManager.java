/*
 * Copyright (C) 2013 Capital Alliance Software LTD (Pekall)
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

package com.pekall.demo.cache;

import com.pekall.demo.bean.DemoBeanCollection;

public class CacheManager {
    private static final CacheManager mInstance = new CacheManager();

    private DemoBeanCollection mDemoBeanCollection;

    public static CacheManager getInstance() {
        return mInstance;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void clearAllCache() {
        mDemoBeanCollection = null;
    }

    public DemoBeanCollection getDemoBeanCollection() {
        return mDemoBeanCollection;
    }

    public void setDemoBeanCollection(DemoBeanCollection collection) {
        this.mDemoBeanCollection = collection;
    }
}
