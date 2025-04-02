class Person {       //Person class represents a person with a name, surname, and email.

    // Private fields to store the person's name, surname, and email
    private String name;
    private String surname;
    private String email;

    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public void printInfo() {
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Email: " + email);
    }

    public String getName() {   //Retrieves the name of the person.
        return name;
    }    //Retrieves the Name of the person.

    public String getSurname() {
        return surname;
    }     //Retrieves the surname of the person.


    public String getEmail() {   //Retrieves the email of the person
        return email;
    }    //Retrieves the email of the person.
}