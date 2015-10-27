package co.com.seguridog

class CanineProfile {
    Integer obedience
    Integer velLearning
    Integer naturePlay
    Integer natureExplore
    Integer levelContantactitude
    Integer levelExiting
    Integer actitudeGeneral
    Integer dominated
    Integer relationshipHandler
    Integer comunicativeCanine
    Integer stablityEmotional
    Integer interAgressive

    static constraints = {
        obedience (blank: false)
        velLearning (blank: false)
        naturePlay (blank: false)
        natureExplore (blank: false)
        levelContantactitude (blank: false)
        levelExiting (blank: false)
        actitudeGeneral (blank: false)
        dominated (blank: false)
        relationshipHandler (blank: false)
        comunicativeCanine (blank: false)
        stablityEmotional (blank: false)
        interAgressive (blank: false)
    }

}
