import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PersonTest {

    @Rule
    public DatabaseRule database = new DatabaseRule();
    @Test
    public void person_instantiatesCorrectly_true() {
        Person testPerson = new Person("Henry", "benruwel21@gmail.com");
        assertEquals(true, testPerson instanceof Person);
    }

    @Test
    public void getName_personInstantiatesWithName_Henry() {
        Person testPerson = new Person("Henry", "benruwel21@gmail.com");
        assertEquals("Henry", testPerson.getUsername());
    }

    @Test
    public void getEmail_personInstantiatesWithEmail_String() {
        Person testPerson = new Person("Henry", "benruwel21@gmail.com");
        assertEquals("benruwel21@gmail.com", testPerson.getEmail());
    }

    @Test
    public void all_returnsAllInstancesOfPerson_true() {
        Person firstPerson = new Person("Henry", "henry@henry.com");
        firstPerson.save();
        Person secondPerson = new Person("Harriet", "harriet@harriet.com");
        secondPerson.save();
        assertEquals(true, Person.all().get(0).equals(firstPerson));
        assertEquals(true, Person.all().get(1).equals(secondPerson));
    }
    @Test
    public void getMonsters_retrievesAllMonstersFromDatabase_monstersList() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Monster firstMonster = new Monster("Bubbles", testPerson.getId());
        firstMonster.save();
        Monster secondMonster = new Monster("Spud", testPerson.getId());
        secondMonster.save();
        Monster[] monsters = new Monster[] { firstMonster, secondMonster };
        assertTrue(testPerson.getMonsters().containsAll(Arrays.asList(monsters)));
    }

    @Test
    public void save_assignsIdToObject() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Person savedPerson = Person.all().get(0);
        assertEquals(testPerson.getId(), savedPerson.getId());
    }

    @Test
    public void find_returnsPersonWithSameId_secondPerson() {
        Person firstPerson = new Person("Henry", "henry@henry.com");
        firstPerson.save();
        Person secondPerson = new Person("Harriet", "harriet@harriet.com");
        secondPerson.save();
        assertEquals(Person.find(secondPerson.getId()), secondPerson);
    }

//    @Override
//    public boolean equals(Object otherPerson){
//        if (!(otherPerson instanceof Person)) {
//            return false;
//        } else {
//            Person newPerson = (Person) otherPerson;
//            return this.getUsername.equals(newPerson.getUsername()) &&
//                    this.getEmail().equals(newPerson.getEmail());
//        }
//    }
}