import org.sql2o.*;

import java.util.ArrayList;
import java.util.List;

public class Person {

    private String username;
    private String email;
    private int id;


    public Person(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getId(){
        return id;
    }

    public List<Community> getCommunities() {
        try(Connection con = DB.sql2o.open()){
            String joinQuery = "SELECT community_id FROM communities_persons WHERE person_id = :person_id";
            List<Integer> communityIds = con.createQuery(joinQuery)
                    .addParameter("person_id", this.getId())
                    .executeAndFetch(Integer.class);

            List<Community> communities = new ArrayList<Community>();

            for (Integer communityId : communityIds) {
                String communityQuery = "SELECT * FROM communities WHERE id = :communityId";
                Community community = con.createQuery(communityQuery)
                        .addParameter("communityId", communityId)
                        .executeAndFetchFirst(Community.class);
                communities.add(community);
            }
            return communities;
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO persons (name, email) VALUES (:name, :email)";
            con.createQuery(sql)
                    .addParameter("name", this.username)
                    .addParameter("email", this.email)
                    .executeUpdate();
        }
    }

    public static Person find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM persons where id = :id";
            Person person = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Person.class);
            return person;
        }
    }
    public static List<Person> all() {
        String sql = "SELECT * FROM persons";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(Person.class);
        }
    }

    public List<Object> getMonsters() {
        List<Object> allMonsters = new ArrayList<Object>();

        try(Connection con = DB.sql2o.open()) {
            String sqlFire = "SELECT * FROM monsters WHERE personId=:id AND type='fire';";
            List<FireMonster> fireMonsters = con.createQuery(sqlFire)
                    .addParameter("id", this.id)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(FireMonster.class);
            allMonsters.addAll(fireMonsters);

            String sqlWater = "SELECT * FROM monsters WHERE personId=:id AND type='water';";
            List<WaterMonster> waterMonsters = con.createQuery(sqlWater)
                    .addParameter("id", this.id)
                    .throwOnMappingFailure(false)
                    .executeAndFetch(WaterMonster.class);
            allMonsters.addAll(waterMonsters);
        }

        return allMonsters;
    }

    public void leaveCommunity(Community community){
        try(Connection con = DB.sql2o.open()){
            String joinRemovalQuery = "DELETE FROM communities_persons WHERE community_id = :communityId AND person_id = :personId;";
            con.createQuery(joinRemovalQuery)
                    .addParameter("communityId", community.getId())
                    .addParameter("personId", this.getId())
                    .executeUpdate();
        }
    }

    @Override
    public boolean equals(Object otherPerson){
        if (!(otherPerson instanceof Person)) {
            return false;
        } else {
            Person newPerson = (Person) otherPerson;
            return this.getUsername().equals(newPerson.getUsername()) &&
                    this.getEmail().equals(newPerson.getEmail());
        }
    }
}
