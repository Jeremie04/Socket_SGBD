package sgbd;

import java.util.*;
import object.*;
import java.io.*;
import java.lang.reflect.Method;

public class Request{
    // the name of the action and its procedure method
    Vector<Vector<String>> grammar;
    Vector<Table> relation; 
    String request;
    String database;

    public void setRelation (Vector<Table> r)throws Exception{
        if(r!=null){
            this.relation=r;
        }else{
            throw new Exception("Relation invalid");
        }
    }

    public Vector<Vector<String>> getGrammar()throws Exception{
        Vector<Vector<String>> list = new Vector<>();
        Vector<String> g1 = new Vector<>();
        g1.add("substract"); g1.add("differenceProcedure");
        Vector<String> g2 = new Vector<>();
        g2.add("select"); g2.add("selectProcedure");
        Vector<String> g3 = new Vector<>();
        g3.add("divide"); g3.add("divisionProcedure");
        Vector<String> g4 = new Vector<>();
        g4.add("unify"); g4.add("unionProcedure");
        Vector<String> g5 = new Vector<>();
        g5.add("join"); g5.add("joinProcedure");
        Vector<String> g6 = new Vector<>();
        g6.add("intersect"); g6.add("intersectProcedure");
        list.add(g1);
        list.add(g2);
        list.add(g3);
        list.add(g4);
        list.add(g5);
        list.add(g6);
        return list;
    }

    public Table ExcecuteProcedure (int idProcedure )throws Exception{
            Method m = this.getClass().getDeclaredMethod(this.getGrammar().get(idProcedure).get(1));
            return (Table) m.invoke(this);
    }

    public Vector<Table> getRelation()throws Exception{
        if(this.relation==null){
            TableManager tm = new TableManager(this.getDatabase());
            this.setRelation(tm.TakeRelation());
        }
        return this.relation;
    }

    public Table getRelation(String nom)throws Exception{
        for(int i=0; i<this.getRelation().size(); i++){
            if(this.getRelation().get(i).getNom().equalsIgnoreCase(nom)){
                return this.getRelation().get(i);
            }
        }
        throw new Exception(nom+" not found");
    }

    public String getDatabase(){
        return this.database;
    }
    public void setDatabase(String value){
        this.database=value;
    }

    public Request(String req, String dtb){
        this.database=dtb;
        this.request = req;
    }

    public Vector<Table> getRelationfromRequest( String action)throws Exception{
        Vector<Table> rel = new Vector<>();
        String[] element = request.split(action+" ");
        String[] tableName = element[1].split(" with ");
        rel.add (this.getRelation(tableName[0]));
        rel.add (this.getRelation(tableName[1]));
        return rel;
    }

    public Table selectProcedure ()throws Exception{
        /*  SELECT * FROM table WHERE col=value */
            String[] req = request.split("select "); 
            String[] element = req[1].split(" from ");
            String[] el = element[1].split(" where ");
            String table = el[0];
            String[] condition = el[1].split("=");
            String column = condition[0];
            String value = condition[1];
    
            Table relation = getRelation(table);
            return relation.Selection(column, value); 

    }

    public Table differenceProcedure ()throws Exception{
        /* SUBSTRACT table1 with table2 */
            Vector<Table> rel = getRelationfromRequest("substract");
            return rel.get(0).Difference(rel.get(1));
    }

    public Table divisionProcedure()throws Exception{
        /*Divide tab1 with tab2 */
        Vector<Table> rel = getRelationfromRequest("divide");
        return rel.get(0).Division(rel.get(1));
    }

    public Table unionProcedure()throws Exception{
        /*Unify table with table */
        Vector<Table> rel = getRelationfromRequest("unify");
        return rel.get(0).Union(rel.get(1));
    }

    public Table joinProcedure ()throws Exception{
        /*Join table1 with table2 on colonne */
        String[] element = request.split("join ");
        String[] table_colonne = element[1].split(" on ");
        String[] tables = table_colonne[0].split(" with ");
        Table r1 = this.getRelation(tables[0]);
        Table r2 = this.getRelation(tables[1]);
        return r1.Join(r2, table_colonne[1]);
    }

    public Table intersectionProcedure ()throws Exception{
        /*intersect table1 with table2*/
        Vector<Table> rel = getRelationfromRequest("intersect");
        return rel.get(0).Intersection(rel.get(1));
    }

    public Table ExcecuteRequest ()throws Exception{
        if(request.startsWith("select ")){
            return this.selectProcedure();
        }else if(request.startsWith("substract ")){
            return this.differenceProcedure();
        }else if(request.startsWith("divide ")){
            return this.divisionProcedure();
        }else if(request.startsWith("unify ")){
            return this.unionProcedure();
        }else if(request.startsWith("join ")){
            return this.joinProcedure();
        }else if(request.startsWith("intersect ")){
            return this.intersectionProcedure();
        }else{
            throw new Exception("Syntax error");
        }
    }


/* 
    public Table ExcecuteRequest ()throws Exception{
        try{
        for(int i=0; i<this.getGrammar().size(); i++){
            if(request.startsWith(this.getGrammar().get(i).get(0)+" ")){
                Table result = this.ExcecuteProcedure(i);
                return result;
            }
        }
        if(request.startsWith("create ")){
            this.createProcedure();
            return null;
        }else{
            throw new Exception("Syntax error");
        }
        }catch(Exception e){throw e;}
    }
*/
}