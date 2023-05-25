package com.begdev.bigbid.ui.profile

sealed class ProfileEvent {
    object ToggleEditMode : ProfileEvent()
    class AvatarChanged(val newImage: String) : ProfileEvent()
    object ApplyChanges : ProfileEvent()

}