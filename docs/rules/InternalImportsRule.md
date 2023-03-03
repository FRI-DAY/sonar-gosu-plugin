Some code packages contain Guidewire internal methods that are reserved for Guidewire use only. Gosu code written to configure Guidewire must never call an internal method for any reason. Future releases of Guidewire can change or remove an internal method without notification.

The following packages contain Guidewire internal methods

 *  All packages in `com.guidewire.*`
 *  Any package whose name or location includes the word `internal`

Gosu configuration code can safely call methods defined in any `gw.*`package (except for those packages whose name or location includes the word `internal`).