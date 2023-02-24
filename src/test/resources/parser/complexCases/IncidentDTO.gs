package de.friday.webservice.claims.dto

uses de.friday.entity.Address
uses de.friday.entity.Incident
uses de.friday.entity.TypeofProperty
uses de.friday.database.Query
uses de.friday.database.transaction.Bundle
uses de.friday.webservice.exception.BadIdentifierException
uses de.friday.webservice.annotation.WsiExportable
uses de.friday.utils.reflections.ReflectUtil

@Export
@WsiExportable("https://friday.de/webservice/claims/dto/IncidentDTO")
final class IncidentDTO {
  var _ambulanceUsed : Boolean as AmbulanceUsed
  var _claimPublicID : String as ClaimPublicID
  var _createTime : Date as CreateTime
  var _createUserPublicID : String as CreateUserPublicID
  var _description : String as Description
  var _detailedInjuryType : DetailedInjuryType as DetailedInjuryType
  var _disabledDueToAccident : DisabledDueToAccident as DisabledDueToAccident
  var _driverPublicID : String as driverPublicID
  var _exposurePublicIDs : String[]as ExposurePublicIDs = {}
  var _generalInjuryType : InjuryType as GeneralInjuryType
  var _injuredPublicID : String as injuredPublicID
  var _locationAddressPublicID : String as LocationAddressPublicID
  var _lossDesc : String as LossDesc
  var _lossOccured : LossOccured as LossOccured
  var _lossofUse : Boolean as LossofUse
  var _medicalTreatmentType : MedicalTreatmentType as MedicalTreatmentType
  var _newExposures : de.friday.webservice.claims.dto.ExposureDTO[]as NewExposures = {}
  var _propertyDesc : String as PropertyDesc
  var _publicID : String as PublicID
  var _severity : SeverityType as Severity
  var _subtype : typekey.Incident as Subtype
  var _typeofPropertyPublicIDs : String[]as TypeofPropertyPublicIDs = {}
  var _updateTime : Date as UpdateTime
  var _updateUserPublicID : String as UpdateUserPublicID
  var _vehiclePublicID : String as VehiclePublicID

  construct() {
  }

  static function valueOf(that : Incident) : de.friday.webservice.claims.dto.IncidentDTO {
    return new de.friday.webservice.claims.dto.IncidentDTO().readFrom(that)
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

  /**
   * Returns the Incident whose public ID is in the supplied list, or null if the publicID is null.
   *
   * @param publicIDs A list of the PublicIDs.
   */
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

  protected function _copyReadOnlyFieldsFrom(that : Incident) {
    _createTime = that.CreateTime
    _createUserPublicID = that.CreateUser.PublicID
    _exposurePublicIDs = that.Exposures*.PublicID
    _subtype = that.Subtype
    _updateTime = that.UpdateTime
    _updateUserPublicID = that.UpdateUser.PublicID

    if (that typeis PropertyIncident) {
      _typeofPropertyPublicIDs = that.TypeofProperty*.PublicID
    }
  }

  final function readFrom(that : Incident) : de.friday.webservice.claims.dto.IncidentDTO {
    _copyReadOnlyFieldsFrom(that)

    ClaimPublicID = that.Claim.PublicID
    Description = that.Description
    PublicID = that.PublicID
    Severity = that.Severity

    if (that typeis InjuryIncident) {
      AmbulanceUsed = that.AmbulanceUsed
      DetailedInjuryType = that.DetailedInjuryType
      DisabledDueToAccident = that.DisabledDueToAccident
      GeneralInjuryType = that.GeneralInjuryType
      MedicalTreatmentType = that.MedicalTreatmentType
      injuredPublicID = that.injured.PublicID
    }
    if (that typeis MobilePropertyIncident) {
      LocationAddressPublicID = that.LocationAddress.PublicID
      LossDesc = that.LossDesc
      LossOccured = that.LossOccured
    }
    if (that typeis PropertyIncident) {
      LossofUse = that.LossofUse
      PropertyDesc = that.PropertyDesc
    }
    if (that typeis VehicleIncident) {
      VehiclePublicID = that.Vehicle.PublicID
      driverPublicID = that.driver.PublicID
    }
    return this
  }

  final function writeTo(that : Incident, ignoreNullValues = true) : Incident {
    // if field is on base class
    if (ClaimPublicID != null or !ignoreNullValues) that.Claim = Claim
    if (Description != null or !ignoreNullValues) that.Description = Description
    if (PublicID != null or !ignoreNullValues) that.PublicID = PublicID
    if (Severity != null or !ignoreNullValues) that.Severity = Severity
    //
    if (that typeis InjuryIncident) {
      if (AmbulanceUsed != null or !ignoreNullValues) that.AmbulanceUsed = AmbulanceUsed
      if (DetailedInjuryType != null or !ignoreNullValues) that.DetailedInjuryType = DetailedInjuryType
      if (DisabledDueToAccident != null or !ignoreNullValues) that.DisabledDueToAccident = DisabledDueToAccident
      if (GeneralInjuryType != null or !ignoreNullValues) that.GeneralInjuryType = GeneralInjuryType
      if (MedicalTreatmentType != null or !ignoreNullValues) that.MedicalTreatmentType = MedicalTreatmentType
      if (injuredPublicID != null or !ignoreNullValues) that.injured = injured
    }
    if (that typeis MobilePropertyIncident) {
      if (LocationAddressPublicID != null or !ignoreNullValues) that.LocationAddress = LocationAddress
      if (LossDesc != null or !ignoreNullValues) that.LossDesc = LossDesc
      if (LossOccured != null or !ignoreNullValues) that.LossOccured = LossOccured
    }
    if (that typeis PropertyIncident) {
      if (LossofUse != null or !ignoreNullValues) that.LossofUse = LossofUse
      if (PropertyDesc != null or !ignoreNullValues) that.PropertyDesc = PropertyDesc
    }
    if (that typeis VehicleIncident) {
      if (VehiclePublicID != null or !ignoreNullValues) that.Vehicle = Vehicle
      if (driverPublicID != null or !ignoreNullValues) that.driver = driver
    }
    return that
  }

  final function writeToNewEntityIn(bundle : Bundle, createNew : block() : Incident = null, ignoreNullValues = true) : Incident {
    var instance : Incident
    if (createNew == null) {
      instance = bundle == null
          ? ReflectUtil.construct(ConcreteSubtypeClassName, {})
          : ReflectUtil.construct(ConcreteSubtypeClassName, {bundle})
    } else {
      instance = createNew()

      if (bundle != null) {
        instance = bundle.add(instance)
      }
    }
    return writeTo(instance, ignoreNullValues)
  }

  final property get ConcreteSubtypeClassName() : String {
    return "entity." + (Subtype.Code?:"Incident")
  }

  property get Claim() : Claim {
    return fetchByPublicID(ClaimPublicID)
  }

  property get CreateUser() : User {
    return fetchByPublicID(CreateUserPublicID)
  }

  property get driver() : Person {
    return fetchByPublicID(driverPublicID)
  }

  property get Exposures() : Exposure[] {
    return fetchByPublicID(ExposurePublicIDs)
  }

  property get injured() : Person {
    return fetchByPublicID(injuredPublicID)
  }

  property get LocationAddress() : Address {
    return fetchByPublicID(LocationAddressPublicID)
  }

  property get TypeofProperty() : TypeofProperty[] {
    return fetchByPublicID(TypeofPropertyPublicIDs)
  }

  property get UpdateUser() : User {
    return fetchByPublicID(UpdateUserPublicID)
  }

  property get Vehicle() : Vehicle {
    return fetchByPublicID(VehiclePublicID)
  }
}
