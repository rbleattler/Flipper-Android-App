package com.flipperdevices.metric.api.events

enum class SimpleEvent(val id: String) {
    APP_OPEN("app_open"),
    OPEN_SAVE_KEY("open_save_key"),
    OPEN_EMULATE("open_emulate"),
    OPEN_EDIT("open_edit"),
    OPEN_SHARE("open_share"),
    EXPERIMENTAL_OPEN_FM("experimental_open_fm"),
    EXPERIMENTAL_OPEN_SCREEN_STREAMING("experimental_open_screenstreaming"),
    SHARE_SHORT_LINK("share_shortlink"),
    SHARE_LONG_LINK("share_longlink"),
    SHARE_FILE("share_file"),
    SAVE_DUMP("save_dump"),
    MFKEY32("mfkey32"),
    OPEN_NFC_DUMP_EDITOR("open_nfc_dump_editor"),
    OPEN_FAPHUB("open_faphub"),
    OPEN_FAPHUB_CATEGORY("open_faphub_category"),
    OPEN_FAPHUB_SEARCH("open_faphub_search"),
    OPEN_FAPHUB_APP("open_faphub_app"),
    INSTALL_FAPHUB_APP("install_faphub_app"),
    HIDE_FAPHUB_APP("hide_faphub_app"),
}
