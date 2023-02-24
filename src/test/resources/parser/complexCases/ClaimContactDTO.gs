package de.friday.webservice.claims.dto

uses de.friday.entity.Contact
uses de.friday.database.Query
uses de.friday.database.transaction.Bundle
uses de.friday.webservice.exception.BadIdentifierException
uses de.friday.webservice.annotation.WsiExportable

@Export
@WsiExportable("https://friday.de/webservice/claims/dto/ClaimContactDTO")
final class ClaimContactDTO {
  var _claimantFlag : Boolean as ClaimantFlag
  var _claimPublicID : String as ClaimPublicID
  var _contactNameDenorm : String as ContactNameDenorm
  var _contactProhibited : Boolean as ContactProhibited
  var _contactPublicID : String as ContactPublicID
  var _createTime : Date as CreateTime
  var _createUserPublicID : String as CreateUserPublicID
  var _newRoles : gw.webservice.cc.cc901.dto.ClaimContactRoleDTO[]as NewRoles = {}
  var _personFirstNameDenorm : String as PersonFirstNameDenorm
  var _personLastNameDenorm : String as PersonLastNameDenorm
  var _policyPublicID : String as PolicyPublicID
  var _publicID : String as PublicID
  var _rolePublicIDs : String[]as RolePublicIDs = {}
  var _updateTime : Date as UpdateTime
  var _updateUserPublicID : String as UpdateUserPublicID
  var _newContact : gw.webservice.cc.cc901.dto.ContactDTO as NewContact

  construct() {
  }

  static function valueOf(that : ClaimContact) : ClaimContactDTO {
    return new ClaimContactDTO().readFrom(that)
  }

  private static function fetchByPublicID<T extends KeyableBean>(publicIDs : String[]) : T[] {
    var results : T[] = {}
    if (publicIDs.HasElements) {
      results = Query.make(T)
          .compareIn(T#PublicID, publicIDs)
          .select()
          .toTypedArray()
      var badIDs = publicIDs.subtract(results*.PublicID)
      if (badIDs.HasElements) throw BadIdentifierException.badPublicId(T, "{" + badIDs.join(", ") + "}")
    }
    return results
  }

  private static function fetchByPublicID<T extends KeyableBean>(publicID : String) : T {
    var result : T
    if (publicID != null) {
      result = Query.make(T)
          .compare(T#PublicID, Equals, publicID)
          .select()
          .AtMostOneRow
      if (result == null) throw BadIdentifierException.badPublicId(T, publicID)
    }
    return result
  }

  protected function _copyReadOnlyFieldsFrom(that : ClaimContact) {
    // if field is on base class
    _contactNameDenorm = that.ContactNameDenorm
    _createTime = that.CreateTime
    _createUserPublicID = that.CreateUser.PublicID
    _personFirstNameDenorm = that.PersonFirstNameDenorm
    _personLastNameDenorm = that.PersonLastNameDenorm
    _rolePublicIDs = that.Roles*.PublicID
    _updateTime = that.UpdateTime
    _updateUserPublicID = that.UpdateUser.PublicID
    //
  }


  final function readFrom(that : ClaimContact) : ClaimContactDTO {
    _copyReadOnlyFieldsFrom(that)

    // if field is on base class
    ClaimPublicID = that.Claim.PublicID
    ClaimantFlag = that.ClaimantFlag
    ContactProhibited = that.ContactProhibited
    ContactPublicID = that.Contact.PublicID
    PolicyPublicID = that.Policy.PublicID
    PublicID = that.PublicID
    //
    return this
  }

  final function writeTo(that : ClaimContact, ignoreNullValues = true) : ClaimContact {
    // if field is on base class
    if (ClaimPublicID != null or !ignoreNullValues) that.Claim = Claim
    if (ClaimantFlag != null or !ignoreNullValues) that.ClaimantFlag = ClaimantFlag
    if (ContactProhibited != null or !ignoreNullValues) that.ContactProhibited = ContactProhibited
    if (ContactPublicID != null or !ignoreNullValues) that.Contact = Contact
    if (PolicyPublicID != null or !ignoreNullValues) that.Policy = Policy
    if (PublicID != null or !ignoreNullValues) that.PublicID = PublicID
    //
    return that
  }

  final function writeToNewEntityIn(bundle : Bundle, createNew : block() : ClaimContact = null, ignoreNullValues = true) : ClaimContact {
    var instance : ClaimContact
    if (createNew == null) {
      instance = bundle == null
          ? new ClaimContact()
          : new ClaimContact(bundle)
    } else {
      instance = createNew()

      if (bundle != null) {
        instance = bundle.add(instance)
      }
    }
    return writeTo(instance, ignoreNullValues)
  }

  property get Claim() : Claim {
    return fetchByPublicID(ClaimPublicID)
  }

  property get Contact() : Contact {
    return fetchByPublicID(ContactPublicID)
  }

  property get CreateUser() : User {
    return fetchByPublicID(CreateUserPublicID)
  }

  property get Policy() : Policy {
    return fetchByPublicID(PolicyPublicID)
  }

  property get Roles() : ClaimContactRole[] {
    return fetchByPublicID(RolePublicIDs)
  }

  property get UpdateUser() : User {
    return fetchByPublicID(UpdateUserPublicID)
  }

}
