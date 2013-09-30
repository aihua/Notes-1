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
package com.pekall.fmradio;

interface IRemoteServiceCallback {
    /**
     * Callback for {@link IFMRadioService#openFMRadio()}
     *
     * @param result
     */
    void openFMRadioCallBack(boolean result);

    void openFMRadioTxCallBack(boolean result);

    /**
     * Callback for {@link IFMRadioService#setFMFreq()}
     *
     * @param result
     */
    void setFrequencyCallback(boolean result);

    /**
     * Callback for {@link IFMRadioService#seekFM()}
     *
     * @param result
     * @param frequency of the new found station
     */
    void seekStationCallback(boolean result, int frequency);
}