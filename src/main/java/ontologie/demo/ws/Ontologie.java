package ontologie.demo.ws;

import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdfconnection.SparqlQueryConnection;
import org.apache.jena.sparql.function.library.print;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.soap.support.SoapUtils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class Ontologie {

    @RequestMapping(value = "/ontologies",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getontologies() {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator ontologiesIter = model.listOntologies();
            while (ontologiesIter.hasNext()) {
                Ontology ontology = (Ontology) ontologiesIter.next();

                JSONObject obj = new JSONObject();
                obj.put("name",ontology.getLocalName());
                obj.put("uri",ontology.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/classesList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getClasses() {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator classIter = model.listClasses();
            while (classIter.hasNext()) {
                OntClass ontClass = (OntClass) classIter.next();
                JSONObject obj = new JSONObject();
                obj.put("name",ontClass.getLocalName());
                obj.put("uri",ontClass.getURI());
                list.add(obj);

            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getLocations",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getLocations() {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            
            String sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?object WHERE { ?object rdfs:subClassOf+/rdfs:subClassOf my:Location}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("location",solution.get("object").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getCostLevels",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getCostLevels() {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            
            String sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT ?ind  WHERE { 	?ind rdf:type ?X . ?X rdfs:subClassOf my:CostRange .}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                obj.put("levels",solution.get("ind").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getLocationBasedServices",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> searchByLocation(@RequestParam("location") String location) {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            System.out.println(location);
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            
            String sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?ide ?object ?subject ?Z ?A WHERE { ?subject my:hasLocation ?object. FILTER REGEX (str(?object), \""+location+"\" , \"i\"). ?subject my:identifier ?ide. optional {?subject my:imageURL ?Z} optional {?subject my:discription ?A} }";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                
                obj.put("ide",solution.get("ide").toString());
                obj.put("subject",solution.get("subject").toString());
                obj.put("object",solution.get("object").toString());
                RDFNode a = solution.get("Z");
                if(a!=null){
                    obj.put("image",a.toString());
                }else{
                    obj.put("image","");
                }
                RDFNode b = solution.get("A");
                if(b!=null){
                    obj.put("discription",b.toString());
                }else{
                    obj.put("discription","");
                }
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/searchAdvance",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> searchByLocationAdv(@RequestParam(defaultValue = "",required = false,name="location") String location,@RequestParam(name="cost",defaultValue  = "",required = false) String cost,@RequestParam(name="service",required = true) String service) {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        
        try {
            System.out.println(location);
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String sprql = "";
            System.out.print(location+":"+service+":"+cost);
            if(location.equals("")&&cost.equals("")){
                sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?Y ?X ?Z ?A WHERE { ?subject my:identifier ?object. FILTER REGEX (str(?object), \""+service+"\", \"i\"). ?X my:isSupplierOf ?subject. optional {?X my:imageURL ?Z} optional {?X my:discription ?A} ?X my:identifier ?Y. }";
            }else if(location.equals("")){
                sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?Y ?X ?Z ?A WHERE { ?subject my:identifier ?object. FILTER REGEX (str(?object), \""+service+"\", \"i\"). ?X my:isSupplierOf ?subject. ?X my:identifier ?Y.?subject my:hasCost ?V. FILTER REGEX(str(?V),\""+cost+"\"). ?X my:isSupplierOf ?subject. ?X my:identifier ?Y.optional {?X my:imageURL ?Z} optional {?X my:discription ?A}}";
            }else if(cost.equals("")){
                sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?Y ?X ?Z ?A WHERE { ?subject my:identifier ?object. FILTER REGEX (str(?object), \""+service+"\", \"i\"). ?X my:isSupplierOf ?subject. ?X my:identifier ?Y.?X my:address ?W. FILTER REGEX(str(?W),\""+location+"\",\"i\").optional { ?subject my:hasLocation ?object. FILTER REGEX (str(?object), \""+location+"\" , \"i\").} optional {?X my:imageURL ?Z} optional {?X my:discription ?A} ?X my:identifier ?Y. }";
            }else {
                sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?Y ?X ?Z ?A WHERE { ?subject my:identifier ?object. FILTER REGEX (str(?object), \""+service+"\", \"i\"). ?subject my:hasCost ?V.	FILTER REGEX(str(?V),\""+cost+"\").	?X my:isSupplierOf ?subject. ?X my:address ?W. FILTER REGEX(str(?W), \""+location+"\", \"i\"). optional { ?subject my:hasLocation ?object. FILTER REGEX (str(?object), \""+location+"\" , \"i\").} optional {?X my:imageURL ?Z} optional {?X my:discription ?A} ?X my:identifier ?Y. }";
            }
            System.out.print(sprql);
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                
                obj.put("name",solution.get("Y").toString());
                obj.put("id",solution.get("X").toString());
                RDFNode a = solution.get("Z");
                if(a!=null){
                    obj.put("image",a.toString());
                }else{
                    obj.put("image","");
                }
                RDFNode b = solution.get("A");
                if(b!=null){
                    obj.put("discription",b.toString());
                }else{
                    obj.put("discription","");
                }
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getProfile",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getProfile(@RequestParam("name") String name) {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            System.out.println(name);
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            
            String sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  PREFIX my: <http://www.semanticweb.org/construction.owl#> SELECT DISTINCT  ?subject ?object ?email ?address ?type ?city ?registrationNumber ?started ?phone ?image	WHERE { ?subject my:identifier ?object. FILTER REGEX (str(?subject), \""+name+"\").  optional {?subject my:email  ?email.} optional {?subject   my:address ?address.} optional {?subject my:type ?type.} optional {?subject my:city ?city.} optional {?subject my:phone ?phone.} optional {?subject my:registrationNumber ?registrationNumber.} optional {?subject my:started ?started.} optional {?subject my:imageURL ?image.}}";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                RDFNode[] res = {solution.get("email"),solution.get("address"),solution.get("type"),solution.get("city"),solution.get("registrationNumber"),solution.get("started"),solution.get("phone"),solution.get("subject"),solution.get("object"),solution.get("image")};
                String[] lables = {"email","address","type","city","registrationNumber","started","phone","subject","object","image"};
                for(int i=0;i<lables.length;i++){
                    if(res[i]!=null){
                        obj.put(lables[i],res[i].toString());
                    }
                }
                
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    
    
    
    
    
    @RequestMapping(value = "/Individus",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getIndividus() {
        List<JSONObject> list=new ArrayList();
        String fileName = "gym_semantic.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            Iterator individus = model.listIndividuals();
            while (individus.hasNext()) {
                Individual   sub = (Individual) individus.next();
                JSONObject obj = new JSONObject();
                obj.put("name",sub.getLocalName());
                obj.put("uri",sub.getURI());
                list.add(obj);

            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/superClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getSuperClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "gym_semantic.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/opendev/ontologies/2017/10/untitled-ontology-8#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listSuperClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("URI",sub.getURI());
                list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getClasProperty",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getClasProperty(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "gym_semantic.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/opendev/ontologies/2017/10/untitled-ontology-8#".concat(className);

            OntClass ontClass = model.getOntClass(classURI );
            Iterator subIter = ontClass.listDeclaredProperties();
            while (subIter.hasNext()) {
                OntProperty property = (OntProperty) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("propertyName",property.getLocalName());

                    obj.put("propertyType",property.getRDFType().getLocalName());

                if(property.getDomain()!=null)
                    obj.put("domain", property.getDomain().getLocalName());
                if(property.getRange()!=null)
                    obj.put("range",property.getRange().getLocalName());

                list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/equivClasses",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getequivClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "gym_semantic.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/opendev/ontologies/2017/10/untitled-ontology-8#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listEquivalentClasses();
            while (subIter.hasNext()) {
                OntClass sub = (OntClass) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("URI",sub.getURI());
                list.add(obj);


            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/Instances",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> getInstancesClasses(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "gym_semantic.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/opendev/ontologies/2017/10/untitled-ontology-8#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );
            Iterator subIter = personne.listInstances();
            while (subIter.hasNext()) {
                Individual   sub = (Individual) subIter.next();
                JSONObject obj = new JSONObject();
                obj.put("name",sub.getLocalName());
                obj.put("uri",sub.getURI());
                list.add(obj);

            }

            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/isHierarchyRoot",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public   List<JSONObject> isHirarchieroot(@RequestParam("classname") String className) {
        List<JSONObject> list=new ArrayList();
        String fileName = "gym_semantic.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            String classURI = "http://www.semanticweb.org/opendev/ontologies/2017/10/untitled-ontology-8#".concat(className);
            System.out.println(classURI);
            OntClass personne = model.getOntClass(classURI );

          if (personne != null){
              JSONObject obj = new JSONObject();
              if (personne.isHierarchyRoot()){
                  obj.put("isroot","true");
              }else {
                  obj.put("isroot","false");
              }

              list.add(obj);

          }



            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> query() {
        List<JSONObject> list=new ArrayList();
        String fileName = "construction.owl";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_DL_MEM);
            model.read(reader,null);
            
            String sprql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX con: <http://www.semanticweb.org/construction.owl#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT DISTINCT ?subject ?object WHERE { ?subject <http://www.semanticweb.org/construction.owl#identifier> ?object} ";
            Query query = QueryFactory.create(sprql);
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet resultSet = qe.execSelect();
           int x=0;
            while (resultSet.hasNext()) {
                x++;
                JSONObject obj = new JSONObject();
                QuerySolution solution = resultSet.nextSolution();
                System.out.println(solution.get("subject").toString());
                obj.put("subject",solution.get("subject").toString());
                // obj.put("property",solution.get("y").toString());
                obj.put("object",solution.get("object").toString());
                list.add(obj);
            }
            System.out.println(x);
            return list;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
