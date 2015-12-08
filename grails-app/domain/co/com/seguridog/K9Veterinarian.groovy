package co.com.seguridog

import grails.validation.Validateable

@Validateable
class K9Veterinarian extends K9User {
    String professionalCard
    Date dateExpedition

    static hasMany = [clinichistories : ClinicHistory]

    static constraints = {
        professionalCard (blank: false)
        dateExpedition (blank: false)
    }

}
