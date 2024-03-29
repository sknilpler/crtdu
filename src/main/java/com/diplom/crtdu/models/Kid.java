package com.diplom.crtdu.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Kid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String surname;
    private String name;
    private String patronymic;

    private Date birthday;


    /**
     * true - мужской <br>
     * false - женский
     */
    private boolean sex;

    private String grazhdanstvo;
    private String adres;
    private String phone;
    private String school;
    private String klas;

    private Date dateIn;
    private Date dateOut;
    private String reasonOut;

    private int rang;
    /**
     * true - воспитанник не ходит в кружки (в архиве) <br>
     * false - воспитанник числиться
     */
    private boolean archive;

    private String username;


    @ManyToMany
    @JoinTable(name = "kid_parent",
            joinColumns = @JoinColumn(name = "kid_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"))
    private List<Parents> parents;

    @ManyToMany
    @JoinTable(name = "kid_krujok",
            joinColumns = @JoinColumn(name = "kid_id"),
            inverseJoinColumns = @JoinColumn(name = "krujok_id"))
    private List<Krujok> krujki;

//    @ManyToMany
//    @JoinTable(name = "kid_meropriyatie",
//            joinColumns = @JoinColumn(name = "kid_id"),
//            inverseJoinColumns = @JoinColumn(name = "meropriyatie_id"))
//    private List<Meropriyatie> meropriyatiya;

//    @ManyToMany
//    @JoinTable(name = "kid_zanyatie",
//            joinColumns = @JoinColumn(name = "kid_id"),
//            inverseJoinColumns = @JoinColumn(name = "zanyatie_id"))
//    private List<Zanyatie> zanyatiya;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid_id")
    private List<KidZanyatie> kidZanyatia;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid_id")
    private List<Dostijenie> dostList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid_id")
    private List<Uchastnik> meropriyatiya;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid_id")
    private List<Document> documents;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "kid_id")
    private List<Ocenka> marks;

    public Kid(String surname, String name, String patronymic, Date birthday, boolean sex, String grazhdanstvo, String adres, String phone, String school, String klas) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.grazhdanstvo = grazhdanstvo;
        this.adres = adres;
        this.phone = phone;
        this.school = school;
        this.klas = klas;
        this.archive = false;
        this.dateIn = new Date();
    }

    public Kid(String surname, String name, String patronymic, Date birthday, boolean sex, String grazhdanstvo, String adres, String phone, String school, String klas, String username) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.sex = sex;
        this.grazhdanstvo = grazhdanstvo;
        this.adres = adres;
        this.phone = phone;
        this.school = school;
        this.klas = klas;
        this.username = username;
        this.archive = false;
        this.dateIn = new Date();
    }


    @Override
    public String toString() {
        return "Kid{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", sex=" + sex +
                ", grazhdanstvo='" + grazhdanstvo + '\'' +
                ", adres='" + adres + '\'' +
                ", phone='" + phone + '\'' +
                ", school='" + school + '\'' +
                ", klas='" + klas + '\'' +
                ", dateIn=" + dateIn +
                ", dateOut=" + dateOut +
                ", reasonOut='" + reasonOut + '\'' +
                ", rang=" + rang +
                ", archive=" + archive +
                ", username='" + username + '\'' +
                '}';
    }

    public String getFullFIO() {
        return surname + " " + name + " " + patronymic;
    }

    public String getShortFIO() {
        return surname + " " + name.charAt(0) + " " + patronymic.charAt(0);
    }

    public String getStudy(){
        return "ГОУ СОШ "+school+" кл.: " + klas;
    }

    /**
     * Метод возвращает возраст в целых годах
     *
     * @return int возраст (лет)
     */
    public int getAge() {
        LocalDate cDate = LocalDate.now();
        LocalDate bDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(bDate, cDate);
        return period.getYears();
    }
}
