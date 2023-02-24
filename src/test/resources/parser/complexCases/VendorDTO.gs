package de.friday.sample.dto

class VendorDTO {
  var _vendorNetwork: VendorNetwork_Ext as VendorNetwork
  var _name: String as Name
  var _streetAddress: String as StreetAddress
  var _city: String as City
  var _postalCode: String as PostalCode
  var _country: Country as Country = TC_DE
  var _telephone: String as Telephone
  var _fax: String as Fax
  var _primaryEmail: String as PrimaryEmail
  var _alternativeEmail: String as AlternativeEmail
  var _latitude: Double as Latitude
  var _longitude: Double as Longitude

  override function toString(): String {
    return "${this.IntrinsicType}: { \
      Network: ${_vendorNetwork}, \
      Name: ${_name}, \
      Street Address: ${_streetAddress} \
      City: ${_city}, \
      Postal Code: ${_postalCode} \
      Telephone: ${_telephone}, \
      Fax: ${_fax}, \
      Primary Email: ${_primaryEmail}, \
      Alternative Email: ${_alternativeEmail}, \
      Latitude: ${_latitude}, \
      Longitude: ${_longitude} \
    }"
  }

  function hasMandatoryInfo(): boolean {
    return _name.NotBlank
          and _streetAddress.NotBlank
          and _city.NotBlank
          and _postalCode.NotBlank
  }
}
