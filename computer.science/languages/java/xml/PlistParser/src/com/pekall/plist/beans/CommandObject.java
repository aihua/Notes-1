package com.pekall.plist.beans;

import com.pekall.plist.Utils;

/**
 * Base class for server commands
 */
@SuppressWarnings("UnusedDeclaration")
public class CommandObject {
    /**  Empty message for ending a session */
    public static final String REQ_TYPE_EMPTY_MSG = "EmptyMessage";

    /**
     * Locks the device immediately
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_DEVICE_LOCK = "DeviceLock";

    /**
     * Remotely erase a device
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_ERASE_DEVICE = "EraseDevice";

    /** Clear the passcode for a device */
    public static final String REQ_TYPE_CLEAR_PASSCODE = "ClearPasscode";

    /** Get information about the device */
    public static final String REQ_TYPE_DEVICE_INFO = "DeviceInformation";

    /**  Install a configuration profile */
    public static final String REQ_TYPE_INST_PROF = "InstallProfile";

    /** Remove a profile from the device */
    public static final String REQ_TYPE_RM_PROF = "RemoveProfile";

    /**
     * Get a list of installed profiles
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_PROF_LIST = "ProfileList";

    /**
     * Get a list of installed provisioning profiles
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_PROV_PROF_LIST = "ProvisioningProfileList";

    /** Install provisioning profiles */
    public static final String REQ_TYPE_INST_PROV_PROF = "InstallProvisioningProfile";

    /** Remove installed provisioning profiles */
    public static final String REQ_TYPE_RM_PROV_PROF = "RemoveProvisioningProfile";

    /**
     * Get a list of installed certificates
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_CERT_LIST = "CertificateList";

    /**
     * Get a List of Third-Party Applications
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_INST_APP_LIST = "InstalledApplicationList";

    /**
     * Request Security-Related Information
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_SECURITY_INFO = "SecurityInfo";

    /** Get a list of installed restrictions */
    public static final String REQ_TYPE_RESTRICTIONS = "Restrictions";

    /** Install a third-party application */
    public static final String REQ_TYPE_INST_APP = "InstallApplication";

    /** Install paid applications via redemption code */
    public static final String REQ_TYPE_APPLY_REDEMPTION_CODE = "ApplyRedemptionCode";

    /**
     * Provide the status of managed applications
     * Simple command format, use CommandObject
     */
    public static final String REQ_TYPE_MANAGED_APP_LIST = "ManagedApplicationList";

    /**
     * Remove installed managed applications
     */
    public static final String REQ_TYPE_RM_APP = "RemoveApplication";

    /**
     * Manage settings
     */
    public static final String REQ_TYPE_SETTINGS = "Settings";

    /**
     * Install document
     */
    public static final String REQ_TYPE_INST_DOC = "InstallDocument";

    /**
     * Remove document
     */
    public static final String REQ_TYPE_RM_DOC = "RemoveDocument";

    /**
     * Get location of device
     */
    public static final String REQ_TYPE_LOCATION = "Location";


    /**
     * Get advertise statistic information, just use for SU
     */
    public static final String REQ_TYPE_ADVT_STA_INFO = "AdvertiseStatisticInformation";

    /**
     * Get browser statistic information, just use for SU
     */
     public static final String REQ_TYPE_BROWSER_STA_INFO = "BrowserStatisticInformation";

    /**
     * Upload settings, just use for SU
     */
    public static final String REQ_TYPE_UPLOAD_SETTINGS = "UploadSettings";

    /**
     * Light screen, just use for SU
     */
    public static final String REQ_TYPE_LIGHT_SCREEN = "LightScreen";

    /**
     * Selectable erase, just use for SU
     */
    public static final String REQ_TYPE_SELECT_ERASE = "SelectErase";

    public static final String REQ_TYPE_SEND_RES = "SendRes";

    // Request type, see REQ_TYPE_DEVICE_LOCK, ....
    private String RequestType;

    public CommandObject() {
    }

    public CommandObject(String requestType) {
        RequestType = requestType;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandObject)) return false;

        CommandObject that = (CommandObject) o;

        return Utils.safeString(RequestType)
                .equals(Utils.safeString(that.RequestType));

    }

    @Override
    public int hashCode() {
        return Utils.safeString(RequestType).hashCode();
    }

    @Override
    public String toString() {
        return "CommandObject{" +
                "RequestType='" + RequestType + '\'' +
                '}';
    }
}
