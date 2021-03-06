package org.luksze;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Objects;

@Entity
public class VersionedPerson {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    @Version
    private long version;

    protected VersionedPerson() {}

    public VersionedPerson(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void changeFirstName(String name) {
        this.name = name;
    }

    public void changeFamilyName(String surname) {
        this.surname = surname;
    }

    public boolean hasEqualContent(VersionedPerson p) {
        return this == p || Objects.equals(name, p.name) && Objects.equals(surname, p.surname);
    }

    @Override
    public String toString() {
        return "VersionedPerson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", version=" + version +
                '}';
    }

    public boolean versionHasBeenIncrementedByOne(VersionedPerson versionedPerson) {
        return version == versionedPerson.version + 1;
    }
}
