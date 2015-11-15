package co.com.seguridog

class K9Handler extends K9User{
    String basicInductionCourse
    Date dateApprovedInduction
    String typeTrainingHandler

    static hasMany = [workcanines : WorkCanine]

    static constraints = {
        basicInductionCourse (blank: false)
        dateApprovedInduction (blank: false)
        typeTrainingHandler (blank: false, inList: ["B�squeda de Narc�ticos","B�squeda de Explosivos","Defensa Controlada","B�squeda o Detecci�n de Moneda","B�squeda y Rescate de Personas"])
    }

}
