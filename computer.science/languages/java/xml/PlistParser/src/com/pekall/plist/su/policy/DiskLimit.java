/*---------------------------------------------------------------------------------------------
 *                       Copyright (c) 2013 Capital Alliance Software(Pekall) 
 *                                    All Rights Reserved
 *    NOTICE: All information contained herein is, and remains the property of Pekall and
 *      its suppliers,if any. The intellectual and technical concepts contained herein are
 *      proprietary to Pekall and its suppliers and may be covered by P.R.C, U.S. and Foreign
 *      Patents, patents in process, and are protected by trade secret or copyright law.
 *      Dissemination of this information or reproduction of this material is strictly 
 *      forbidden unless prior written permission is obtained from Pekall.
 *                                     www.pekall.com
 *--------------------------------------------------------------------------------------------- 
*/

package com.pekall.plist.su.policy;

/**
 * XML element for "memory_size_policy.memory.diskLimit"
 */
public class DiskLimit {
    /**
     * Maximum usage ratio of handset memory
     */
    int maxRatio;

    /**
     * Event name for punishment
     */
    String eventId;

    public DiskLimit() {
        this(-1, "");
    }

    public DiskLimit(int maxRatio, String eventId) {
        this.maxRatio = maxRatio;
        this.eventId = eventId;
    }

    public int getMaxRatio() {
        return maxRatio;
    }

    public void setMaxRatio(int max_ratio) {
        this.maxRatio = max_ratio;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        this.eventId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiskLimit)) return false;

        DiskLimit diskLimit = (DiskLimit) o;

        if (maxRatio != diskLimit.maxRatio) return false;
        if (eventId != null ? !eventId.equals(diskLimit.eventId) : diskLimit.eventId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = maxRatio;
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DiskLimit{" +
                "maxRatio=" + maxRatio +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}