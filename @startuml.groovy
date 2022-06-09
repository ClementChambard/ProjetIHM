@startuml
class observation{
  -String espece
  -String scientific_name
  -Date debut
  -Date fin
  -ArrayList<feature> zone
  -Scale scale
  +String getEspece()
  +String getScientificName()

}
class feature{
   -float t
   -float b
   -float r
   -float l
   -int nombre_observation

}

class scale{
   -ArrayList<int> scale
   -ArrayList<PhongMaterial> color
   +getScale()
   +getColor()

}
class requete{
  -String espece
  -String scientific_name
  -Date debut
  -Date fin
    
    


    + JSON launchrequest()
}
class Jsonreader{
      {static}observation readJson(Json fichier)
}
@enduml