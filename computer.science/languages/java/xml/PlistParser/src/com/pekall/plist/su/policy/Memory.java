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
 * XML element in memory_size_policy
 */
@SuppressWarnings({"UnusedDeclaration", "SimplifiableIfStatement"})
public class Memory {
    /**
     * Detailed memory information, see {@link MemoryLimit}
     */
    private MemoryLimit memoryLimit;

    /**
     * Detailed disk information, see {@link DiskLimit}
     */
    private DiskLimit diskLimit;

    public Memory() {
        this(new MemoryLimit(), new DiskLimit());
    }

    public Memory(MemoryLimit memoryLimit, DiskLimit diskLimit) {
        this.memoryLimit = memoryLimit;
        this.diskLimit = diskLimit;
    }

    public MemoryLimit getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(MemoryLimit memory_limit) {
        this.memoryLimit = memory_limit;
    }

    public DiskLimit getDiskLimit() {
        return diskLimit;
    }

    public void setDiskLimit(DiskLimit disk_limit) {
        this.diskLimit = disk_limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Memory)) return false;

        Memory memory = (Memory) o;

        if (diskLimit != null ? !diskLimit.equals(memory.diskLimit) : memory.diskLimit != null) return false;
        return !(memoryLimit != null ? !memoryLimit.equals(memory.memoryLimit) : memory.memoryLimit != null);

    }

    @Override
    public int hashCode() {
        int result = memoryLimit != null ? memoryLimit.hashCode() : 0;
        result = 31 * result + (diskLimit != null ? diskLimit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Memory{" +
                "memoryLimit=" + memoryLimit +
                ", diskLimit=" + diskLimit +
                '}';
    }
}
